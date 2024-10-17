package com.iflytek.auth.manager.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysAuditMapper;
import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.auth.common.pojo.*;
import com.iflytek.auth.manager.common.task.AuditHandler;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.iflytek.auth.common.dto.SysAuditDto.SysAuditItem;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Service
public class AuditServiceImpl extends ServiceImpl<SysAuditMapper, SysAudit> implements IAuditService {

    @Autowired
    private SysAuditMapper auditMapper;

    @Autowired
    private AuditHandler auditHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse audit(SysAuditDto sysAuditDto) {
        //获取审核记录和审核结果之间的映射关系
        Map<Integer, Integer> idResultMap = sysAuditDto.getItems().stream()
                .collect(Collectors.toMap(SysAuditItem::getAuditId, SysAuditItem::getResult));
        List<SysAudit> sysAudits = auditMapper.selectBatchIds(idResultMap.keySet());
        sysAudits.forEach(sysAudit -> {
            sysAudit.setStatus(1); //设置审核状态为已审核
            sysAudit.setResult(idResultMap.get(sysAudit.getId()));
            PoCommonUtils.setAuditorInfo(sysAudit);
            PoCommonUtils.setOperationInfo(sysAudit);
            //TODO 在for循环内部执行sql 多次IO操作需要关注性能问题
//            auditMapper.updateById(sysAudit);
        });
        //更新审核表 并将审核信息提交队列
        this.saveOrUpdateBatch(sysAudits);
        auditHandler.offer(sysAudits);

        return RestResponse.buildSuccess("审核结果提交成功！");
    }

    @Override
    public Boolean hasAudit(Integer targetId, Integer targetType, Integer operationType) {
        return this.lambdaQuery()
                .eq(SysAudit::getTargetId, targetId)
                .eq(SysAudit::getTargetType, targetType)
                .eq(SysAudit::getOperationType, operationType)
                .eq(SysAudit::getStatus, 0)
                .count() > 0;
    }

    @Override
    public Boolean hasSameRoleName(String roleName) {
        //模糊搜索 待审核的新增角色的计划里 newValue中包含roleName的计划
        //只有这些新增计划里 才有可能出现新增角色名称等于roleName的场景
        List<SysAudit> sysAudits = this.lambdaQuery()
                .eq(SysAudit::getTargetType, TargetType.ROLE.getType())
                .eq(SysAudit::getOperationType, OperationType.ADD.getType())
                .eq(SysAudit::getStatus, 0)
                .like(SysAudit::getNewValue, roleName)
                .list();
        if (CollectionUtils.isEmpty(sysAudits)) {
            return false;
        }

        return sysAudits.stream().anyMatch(sysAudit -> {
            SysRole sysRole = JSON.parseObject(sysAudit.getNewValue(), SysRole.class);
            return StringUtils.equals(sysRole.getName(), roleName);
        });
    }

    @Override
    public Boolean hasSameUserInfo(String username, String mail, String telephone) {
        //模糊搜索 待审核的新增用户计划里 newValue包含username mail 或者telephone的计划
        //只有在这些新增计划里 才有可能出现新增用户的用户名、邮箱或者联系电话重复的场景
        List<SysAudit> sysAudits = this.lambdaQuery()
                .eq(SysAudit::getTargetType, TargetType.USER.getType())
                .eq(SysAudit::getOperationType, OperationType.ADD.getType())
                .eq(SysAudit::getStatus, 0)
                .and(wrapper ->
                        wrapper.like(SysAudit::getNewValue, username)
                                .or().like(SysAudit::getNewValue, mail)
                                .or().like(SysAudit::getNewValue, telephone))
                .list();
        if (CollectionUtils.isEmpty(sysAudits)) {
            return false;
        }

        return sysAudits.stream().anyMatch(sysAudit -> {
            SysUser sysUser = JSON.parseObject(sysAudit.getNewValue(), SysUser.class);
            return StringUtils.equals(sysUser.getUsername(), username)
                    || StringUtils.equals(sysUser.getMail(), mail)
                    || StringUtils.equals(sysUser.getTelephone(), telephone);
        });
    }

    @Override
    public boolean hasSameNameOrSeqDept(String deptName, Integer seq) {
        List<SysAudit> sysAudits = this.lambdaQuery()
                .eq(SysAudit::getTargetType, TargetType.DEPT.getType())
                .eq(SysAudit::getOperationType, OperationType.ADD.getType())
                .eq(SysAudit::getStatus, 0)
                .and(wrapper ->
                        wrapper.like(SysAudit::getNewValue, deptName)
                                .or().like(SysAudit::getNewValue, seq))
                .list();
        if (CollectionUtils.isEmpty(sysAudits)) {
            return false;
        }
        return sysAudits.stream().anyMatch(sysAudit -> {
            SysDept sysDept = JSON.parseObject(sysAudit.getNewValue(), SysDept.class);
            return StringUtils.equals(sysDept.getName(), deptName)
                    || seq.equals(sysDept.getSeq());
        });
    }

    @Override
    public boolean hasSameNameOrSeqAclModule(String aclModuleName, Integer seq) {
        List<SysAudit> sysAudits = this.lambdaQuery()
                .eq(SysAudit::getTargetType, TargetType.ACL_MODULE.getType())
                .eq(SysAudit::getOperationType, OperationType.ADD.getType())
                .eq(SysAudit::getStatus, 0)
                .and(wrapper ->
                        wrapper.like(SysAudit::getNewValue, aclModuleName)
                                .or().like(SysAudit::getNewValue, seq))
                .list();
        if (CollectionUtils.isEmpty(sysAudits)) {
            return false;
        }
        return sysAudits.stream().anyMatch(sysAudit -> {
            SysAclModule sysAclModule = JSON.parseObject(sysAudit.getNewValue(), SysAclModule.class);
            return StringUtils.equals(sysAclModule.getName(), aclModuleName)
                    || seq.equals(sysAclModule.getSeq());
        });
    }

    /**
     * 校验在待审核的新增实体计划里 是否存在重复的指定字段
     * @param clazz 实体对象对应的Class类
     * @param targetType 实体对象对应的枚举
     * @param fieldKvMap key需要校验的字段名称 value在当前的新增计划参数中该字段的值
     * @return
     * @param <T>
     */
    @Override
    public <T> boolean hasSameFieldValue(Class<T> clazz, Integer targetType, Map<String, String> fieldKvMap) {
        List<String> fieldNames = Lists.newArrayList(fieldKvMap.keySet());
        if (CollectionUtils.isEmpty(fieldNames)) {
            return false;
        }
        //查询出在待审核的新增计划里 newValue能模糊匹配上需要校验的参数值的审核记录
        //只有这些记录才有可能会出现重复参数值
        List<SysAudit> sysAudits = this.lambdaQuery()
                .eq(SysAudit::getStatus, 0)
                .eq(SysAudit::getOperationType, OperationType.ADD.getType())
                .eq(SysAudit::getTargetType, targetType)
                .and(wrapper -> {
                    wrapper.like(SysAudit::getNewValue, fieldKvMap.get(fieldNames.get(0)));
                    fieldNames.subList(1, fieldNames.size()).forEach(fieldName ->
                            wrapper.or().like(SysAudit::getNewValue, fieldKvMap.get(fieldName)));
                }).list();

        //对字段列表里的每个字段进行逐个校验
        return sysAudits.stream().anyMatch(sysAudit -> {
            T pojo = JSON.parseObject(sysAudit.getNewValue(), clazz);
            for (String fieldName : fieldNames) {
                String submittedValue = String.valueOf(ReflectUtil.getFieldValue(pojo, fieldName));
                String currentValue = String.valueOf(fieldKvMap.get(fieldName));
                if (StringUtils.equals(submittedValue, currentValue)) {
                    return true;
                }
            }
            return false;
        });
    }
}
