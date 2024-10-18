package com.iflytek.auth.common.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.dto.*;
import com.iflytek.auth.common.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class PoCommonUtils {

    /**
     * 设置实体类公共的属性
     *
     * @param pojo
     */
    public static void setOperationInfo(Object pojo) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysUser sysUser = (SysUser) request.getSession().getAttribute("USER");
        //设置操作信息相关的字段
        ReflectUtil.setFieldValue(pojo, "operateTime", new Date());
        ReflectUtil.setFieldValue(pojo, "operateIp", request.getRemoteAddr());
        if (sysUser != null) {
            ReflectUtil.setFieldValue(pojo, "operator", sysUser.getUsername());
        }
    }

    public static void copyUserProperties(SysUserDto sysUserDto, SysUser sysUser) {
        sysUser.setDeptId(sysUserDto.getDeptId());
        sysUser.setUsername(sysUserDto.getUsername());
        sysUser.setMail(sysUserDto.getMail());
        sysUser.setPassword(sysUserDto.getPassword());
        sysUser.setTelephone(sysUserDto.getTelephone());
        sysUser.setStatus(sysUserDto.getStatus() == null ? 1 : sysUserDto.getStatus());
        if (StringUtils.isNotBlank(sysUserDto.getRemark())) {
            sysUser.setRemark(sysUserDto.getRemark());
        }
    }

    public static void copyAclProperties(SysAclDto sysAclDto, SysAcl sysAcl) {
        sysAcl.setAclModuleId(sysAclDto.getAclModuleId());
        sysAcl.setName(sysAclDto.getName());
        sysAcl.setCode(sysAclDto.getCode());
        sysAcl.setSeq(sysAclDto.getSeq());
        sysAcl.setType(sysAclDto.getType());
        sysAcl.setUrl(sysAclDto.getUrl());
        sysAcl.setStatus(sysAclDto.getStatus() == null ? 1 : sysAclDto.getStatus());
        if (StringUtils.isNotBlank(sysAclDto.getRemark())) {
            sysAcl.setRemark(sysAclDto.getRemark());
        }
    }

    public static void copyRoleProperties(SysRoleDto sysRoleDto, SysRole sysRole) {
        sysRole.setName(sysRoleDto.getName());
        sysRole.setType(sysRoleDto.getType());
        sysRole.setStatus(sysRoleDto.getStatus() == null ? 1 : sysRoleDto.getStatus());
        if (StringUtils.isNotBlank(sysRoleDto.getRemark())) {
            sysRole.setRemark(sysRoleDto.getRemark());
        }
    }

    public static void copyLogProperties(SysLogDto sysLogDto, SysLog sysLog) {
        sysLog.setType(sysLogDto.getType());
        sysLog.setTargetId(sysLogDto.getTargetId());
        sysLog.setOldValue(sysLogDto.getOldValue());
        sysLog.setNewValue(sysLogDto.getNewValue());
        sysLog.setStatus(sysLogDto.getStatus() == null ? 0 : sysLogDto.getStatus());
    }

    public static void copyTenantProperties(SysTenantDto sysTenantDto, SysTenant sysTenant) {
        sysTenant.setName(sysTenantDto.getName());
        sysTenant.setStatus(sysTenantDto.getStatus() == null ? 1 : sysTenantDto.getStatus());
        if (StringUtils.isBlank(sysTenantDto.getRemark())) {
            sysTenant.setRemark(sysTenantDto.getRemark());
        }
    }

    /**
     * 设置提交人的信息
     *
     * @param sysAudit
     */
    public static void setSubmitterInfo(SysAudit sysAudit) {
        PoCommonUtils.setOperationInfo(sysAudit);
        sysAudit.setSubmitter(sysAudit.getOperator());
        sysAudit.setSubmitTime(sysAudit.getOperateTime());
        sysAudit.setSubmitterIp(sysAudit.getOperateIp());
    }

    /**
     * 设置审核人信息
     *
     * @param sysAudit
     */
    public static void setAuditorInfo(SysAudit sysAudit) {
        PoCommonUtils.setOperationInfo(sysAudit);
        sysAudit.setAuditor(sysAudit.getOperator());
        sysAudit.setAuditTime(sysAudit.getOperateTime());
        sysAudit.setAuditorIp(sysAudit.getOperateIp());
    }

    public static SysLog buildSysLog(Integer targetId, String oldValue, String newValue,
                                     Integer targetType) {
        SysLog sysLog = new SysLog();
        sysLog.setType(targetType);
        sysLog.setTargetId(targetId);
        sysLog.setOldValue(oldValue);
        sysLog.setNewValue(newValue);
        sysLog.setStatus(1);
        setOperationInfo(sysLog);

        return sysLog;
    }

    public static SysAudit buildSysAudit(SysLog sysLog, Integer operationType) {
        SysAudit sysAudit = new SysAudit();
        sysAudit.setTargetId(sysLog.getTargetId());
        sysAudit.setTargetType(sysLog.getType());
        sysAudit.setNewValue(sysLog.getNewValue());
        sysAudit.setOldValue(sysLog.getOldValue());
        sysAudit.setOperationType(operationType);
        sysAudit.setStatus(0);
        setOperationInfo(sysAudit);
        setSubmitterInfo(sysAudit);
        sysAudit.setDetail(String.format("用户%s对%s进行了%s", sysAudit.getSubmitter(),
                TargetType.getTargetType(sysAudit.getTargetType()), OperationType.getOperation(sysAudit.getOperationType())));

        return sysAudit;
    }

    public static void copyOperateInfo(SysAudit sysAudit, Object pojo) {
        ReflectUtil.setFieldValue(pojo, "operator", sysAudit.getSubmitter());
        ReflectUtil.setFieldValue(pojo, "operateTime", sysAudit.getSubmitTime());
        ReflectUtil.setFieldValue(pojo, "operateIp", sysAudit.getSubmitterIp());
    }

    public static void copyLogProperties(SysLog sysLog, Object log) {
        ReflectUtil.setFieldValue(log, "operator", sysLog.getOperator());
        ReflectUtil.setFieldValue(log, "operateTime", new Date());
        ReflectUtil.setFieldValue(log, "operateIp", sysLog.getOperateIp());
    }

}
