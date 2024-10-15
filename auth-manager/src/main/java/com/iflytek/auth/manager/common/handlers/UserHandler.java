package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
public class UserHandler implements PojoHandler{

    @Resource
    private SysUserMapper sysUserMapper;

    public void handle(SysAudit sysAudit) {
        doHandle(sysAudit);
        log.info("handle user operation success!");
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

    @Override
    public Integer getTargetType() {
        return TargetType.USER.getType();
    }

    private void doAdd(SysAudit sysAudit) {
        SysUser sysUser = JSON.parseObject(sysAudit.getNewValue(), SysUser.class);
        sysUserMapper.insert(sysUser);
        log.info("add user success!");
    }

    private void doUpdate(SysAudit sysAudit) {
        SysUser sysUser = JSON.parseObject(sysAudit.getNewValue(), SysUser.class);
        sysUserMapper.updateById(sysUser);
        log.info("update user success!");
    }

    private void doDel(SysAudit sysAudit) {
        SysUser sysUser = sysUserMapper.selectById(sysAudit.getTargetId());
        sysUser.setStatus(2);
        sysUserMapper.updateById(sysUser);
        log.info("delete user success!");
    }
}
