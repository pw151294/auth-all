package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.dao.SysRoleAclMapper;
import com.iflytek.auth.common.dao.SysRoleMapper;
import com.iflytek.auth.common.dao.SysRoleUserMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
public class RoleHandler implements PojoHandler {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.ROLE.getType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SysAudit sysAudit) {
        log.info("begin to role handler operation >>>>>>>>>>");
        doHandle(sysAudit);
    }

    private void doHandle(SysAudit sysAudit) {
        switch (sysAudit.getOperationType()) {
            case 1:
                doAdd(sysAudit);
                break;
            case 2:
                doUpdate(sysAudit);
                break;
            case 3:
                doDel(sysAudit);
                break;
            default:
                break;
        }
    }

    private void doAdd(SysAudit sysAudit) {
        SysRole sysRole = JSON.parseObject(sysAudit.getNewValue(), SysRole.class);
        sysRoleMapper.insert(sysRole);
        log.info("add role success!");
    }

    private void doUpdate(SysAudit sysAudit) {
        SysRole sysRole = JSON.parseObject(sysAudit.getNewValue(), SysRole.class);
        sysRoleMapper.updateById(sysRole);
        log.info("update role success!");
    }

    private void doDel(SysAudit sysAudit) {
        Integer roleId = sysAudit.getTargetId();
        sysRoleUserMapper.deleteByRoleId(roleId);
        sysRoleAclMapper.deleteByRoleId(roleId);
        sysRoleMapper.deleteById(roleId);
        log.info("delete role success!");
    }
}
