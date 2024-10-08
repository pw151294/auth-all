package com.iflytek.auth.common.common.utils;

import cn.hutool.core.util.ReflectUtil;
import com.iflytek.auth.common.dto.SysAclDto;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.dto.SysRoleDto;
import com.iflytek.auth.common.dto.SysUserDto;
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
        if (StringUtils.isNotBlank(sysRoleDto.getName())) {
            sysRole.setName(sysRoleDto.getName());
        }
    }

    public static void copyLogProperties(SysLogDto sysLogDto, SysLog sysLog) {
        sysLog.setType(sysLogDto.getType());
        sysLog.setTargetId(sysLogDto.getTargetId());
        sysLog.setOldValue(sysLogDto.getOldValue());
        sysLog.setNewValue(sysLogDto.getNewValue());
        sysLog.setStatus(sysLogDto.getStatus() == null ? 0 : sysLogDto.getStatus());
    }

    /**
     * 设置提交人的信息
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
     * @param sysAudit
     */
    public static void setAuditorInfo(SysAudit sysAudit) {
        PoCommonUtils.setOperationInfo(sysAudit);
        sysAudit.setAuditor(sysAudit.getOperator());
        sysAudit.setAuditTime(sysAudit.getOperateTime());
        sysAudit.setAuditorIp(sysAudit.getOperateIp());
    }
}
