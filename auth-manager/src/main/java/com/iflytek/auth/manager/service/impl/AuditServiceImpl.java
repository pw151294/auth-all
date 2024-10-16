package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysAuditMapper;
import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysRole;
import com.iflytek.auth.common.pojo.SysUser;
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
}
