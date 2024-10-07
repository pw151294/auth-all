package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysAuditMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.manager.service.IAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class AuditServiceImpl implements IAuditService {

    @Autowired
    private SysAuditMapper auditMapper;

    @Override
    public void submitUserAdd(SysUser newUser) {
        SysAudit sysAudit = new SysAudit();
        sysAudit.setTargetType(TargetType.USER.getType());
        sysAudit.setOperationType(OperationType.ADD.getType());
        sysAudit.setNewValue(JSON.toJSONString(newUser));
        PoCommonUtils.setSubmitterInfo(sysAudit);
        sysAudit.setDetail(String.format("%s申请新增用户%s", sysAudit.getSubmitter(), newUser.getUsername()));
        auditMapper.insert(sysAudit);
    }

    @Override
    public void submitUserUpdate(SysUser oldUser, SysUser newUser) {
        SysAudit sysAudit = new SysAudit();
        sysAudit.setTargetType(TargetType.USER.getType());
        sysAudit.setTargetId(newUser.getId());
        sysAudit.setOperationType(OperationType.UPDATE.getType());
        sysAudit.setOldValue(JSON.toJSONString(oldUser));
        sysAudit.setNewValue(JSON.toJSONString(newUser));
        PoCommonUtils.setSubmitterInfo(sysAudit);
        sysAudit.setDetail(String.format("%s申请更新用户信息，用户ID:%s", sysAudit.getSubmitter(), newUser.getId()));
        auditMapper.insert(sysAudit);
    }

    @Override
    public void submitUserDelete(Integer userId) {
        SysAudit sysAudit = new SysAudit();
        sysAudit.setTargetType(TargetType.USER.getType());
        sysAudit.setTargetId(userId);
        sysAudit.setOperationType(OperationType.DELETE.getType());
        PoCommonUtils.setSubmitterInfo(sysAudit);
        sysAudit.setDetail(String.format("%s申请删除用户，用户ID:%s", sysAudit.getSubmitter(), userId));
        auditMapper.insert(sysAudit);
    }
}
