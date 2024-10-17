package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.dao.SysAclMapper;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.pojo.SysAudit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
public class AclHandler implements PojoHandler {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.ACL.getType();
    }

    @Override
    public void handle(SysAudit sysAudit) {
        log.info("begin to handler acl operation >>>>>>>>>>");
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
        SysAcl sysAcl = JSON.parseObject(sysAudit.getNewValue(), SysAcl.class);
        sysAclMapper.insert(sysAcl);
        log.info("add acl success！");
    }

    private void doUpdate(SysAudit sysAudit) {
        SysAcl sysAcl = JSON.parseObject(sysAudit.getNewValue(), SysAcl.class);
        sysAclMapper.updateById(sysAcl);
        log.info("update acl success!");
    }

    private void doDel(SysAudit sysAudit) {
        sysAclMapper.deleteById(sysAudit.getTargetId());
        log.info("delete acl success!");
    }
}
