package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.enums.AuthErrorCodeEnum;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.dao.SysDeptMapper;
import com.iflytek.auth.common.dto.SysDeptDto;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysDept;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.common.vo.SysDeptVo;
import com.iflytek.auth.manager.common.task.SysTask;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.auth.manager.service.IDeptService;
import com.iflytek.itsc.web.exception.BaseBizException;
import com.iflytek.itsc.web.response.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class DeptServiceImpl implements IDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private IAuditService auditService;

    @Autowired
    @Qualifier(value = "auditTask")
    private SysTask auditTask;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addDept(SysDeptDto sysDeptDto) {
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        SysDept sysDept = new SysDept();
        transferProperties(sysDept, sysDeptDto);
        deptMapper.insert(sysDept);

        return RestResponse.buildSuccess("新建部门成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateDept(SysDeptDto sysDeptDto) {
        //查询对应的部门是否存在
        Integer deptId = sysDeptDto.getId();
        Preconditions.checkNotNull(sysDeptDto.getId(), "被更新部门ID不能为空");
        SysDept sysDept = deptMapper.selectById(deptId);
        if (sysDept == null) {
            return RestResponse.buildError(String.format("待更新部门不存在，id:%s", deptId));
        }
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        transferProperties(sysDept, sysDeptDto);
        deptMapper.updateById(sysDept);

        return RestResponse.buildSuccess("部门更新成功");
    }

    @Override
    public RestResponse deletDept(Integer deptId) {
        Preconditions.checkNotNull(deptId, "被删除部门的ID不能为空");
        //校验部门下是否有成员或者子部门
        if (deptMapper.countUsersByDeptId(deptId) > 0) {
            return RestResponse.buildError("该部门下还有成员存在，不能删除！");
        }
        if (deptMapper.countSubDepts(deptId) > 0) {
            return RestResponse.buildError("该部门下还有子部门，不能删除！");
        }
        deptMapper.deleteById(deptId);

        return RestResponse.buildSuccess("删除部门成功！");
    }

    @Override
    public RestResponse<List<SysDeptVo>> deptTree() {
        //查询出所有部门和子部门的映射关系
        Map<Integer, List<SysDept>> deptIdChildMap =
                deptMapper.findAll().stream().collect(Collectors.groupingBy(SysDept::getParentId));
        //获取一级部门
        List<SysDeptVo> deptTree = deptIdChildMap.get(0)
                .stream()
                .map(sysDept -> {
                    SysDeptVo deptVo = new SysDeptVo();
                    BeanUtils.copyProperties(sysDept, deptVo);
                    return deptVo;
                }).collect(Collectors.toList());
        //逐个构建各部门的子部门
        deptTree.forEach(sysDeptVo -> TreeUtils.buildTree(sysDeptVo, deptIdChildMap));

        return RestResponse.buildSuccess(deptTree);
    }

    @Override
    public RestResponse submitAddDept(SysDeptDto sysDeptDto) {
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        //校验待审核的新增计划里 是否有同名或同序的部门
        if (auditService.hasSameNameOrSeq(sysDeptDto.getName(), sysDeptDto.getSeq())) {
            return RestResponse.buildError("待审核的新增计划里存在同名或同序的部门！");
        }
        SysDept sysDept = new SysDept();
        transferProperties(sysDept, sysDeptDto);
        String newValue = JSON.toJSONString(sysDept);
        SysLog sysLog = PoCommonUtils.buildSysLog(null, "", newValue, TargetType.DEPT.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.ADD.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("添加部门计划提交成功！");
    }

    @Override
    public RestResponse submitUpdateDept(SysDeptDto sysDeptDto) {
        //查询对应的部门是否存在
        Integer deptId = sysDeptDto.getId();
        Preconditions.checkNotNull(sysDeptDto.getId(), "被更新部门ID不能为空");
        //校验该部门是否已经有待审核的修改或者删除计划
        if (auditService.hasAudit(sysDeptDto.getId(), TargetType.DEPT.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(sysDeptDto.getId(), TargetType.DEPT.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在待审核的修改删除该部门的计划！");
        }

        SysDept sysDept = deptMapper.selectById(deptId);
        if (sysDept == null) {
            return RestResponse.buildError(String.format("待更新部门不存在，id:%s", deptId));
        }
        //校验上级部门下 是否有同名或者同序部门
        if (deptMapper.findByNameAndSeq(sysDeptDto) > 0) {
            throw new BaseBizException(AuthErrorCodeEnum.DEPT_DUPLICATE_NAME_OR_SEQ);
        }
        String oldValue = JSON.toJSONString(sysDept);
        transferProperties(sysDept, sysDeptDto);
        String newValue = JSON.toJSONString(sysDept);
        SysLog sysLog = PoCommonUtils.buildSysLog(sysDeptDto.getId(), oldValue, newValue, TargetType.DEPT.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.UPDATE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("更新部门计划提交成功");
    }

    @Override
    public RestResponse submitDeleteDept(Integer deptId) {
        Preconditions.checkNotNull(deptId, "被删除部门的ID不能为空");
        //校验该部门是否已经有待审核的修改或者删除计划
        if (auditService.hasAudit(deptId, TargetType.DEPT.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(deptId, TargetType.DEPT.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在待审核的修改删除该部门的计划！");
        }
        //校验部门下是否有成员或者子部门
        if (deptMapper.countUsersByDeptId(deptId) > 0) {
            return RestResponse.buildError("该部门下还有成员存在，不能删除！");
        }
        if (deptMapper.countSubDepts(deptId) > 0) {
            return RestResponse.buildError("该部门下还有子部门，不能删除！");
        }
        SysLog sysLog = PoCommonUtils.buildSysLog(deptId, "", "", TargetType.DEPT.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.DELETE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("删除部门计划提交成功！");
    }


    private void transferProperties(SysDept sysDept, SysDeptDto sysDeptDto) {
        sysDept.setName(sysDeptDto.getName());
        sysDept.setParentId(sysDeptDto.getParentId());
        sysDept.setSeq(sysDeptDto.getSeq());
        String parentLevel = deptMapper.findLevelById(sysDeptDto.getParentId());
        sysDept.setLevel(TreeUtils.getLevel(parentLevel, sysDeptDto.getParentId()));
        if (StringUtils.isNotBlank(sysDeptDto.getRemark())) {
            sysDept.setRemark(sysDeptDto.getRemark());
        }
        PoCommonUtils.setOperationInfo(sysDept);
    }
}
