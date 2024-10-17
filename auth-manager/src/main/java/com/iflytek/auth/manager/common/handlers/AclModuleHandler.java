package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.dao.SysAclModuleMapper;
import com.iflytek.auth.common.pojo.SysAclModule;
import com.iflytek.auth.common.pojo.SysAudit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
public class AclModuleHandler implements PojoHandler {

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.ACL_MODULE.getType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SysAudit sysAudit) {
        log.info("begin handle acl module operation >>>>>>>>>>");
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
        SysAclModule sysAclModule = JSON.parseObject(sysAudit.getNewValue(), SysAclModule.class);
        sysAclModuleMapper.insert(sysAclModule);
        log.info("add acl module success!");
    }

    private void doUpdate(SysAudit sysAudit) {
        SysAclModule oldValue = JSON.parseObject(sysAudit.getOldValue(), SysAclModule.class);
        SysAclModule newValue = JSON.parseObject(sysAudit.getNewValue(), SysAclModule.class);
        //更新子节点的level
        List<SysAclModule> sysAclModules = sysAclModuleMapper.findAll();
        List<SysAclModule> childs = TreeUtils.findChilds(newValue.getId(), sysAclModules);
        //更新所有子权限模块的level
        TreeUtils.changeChildLevel(childs, oldValue.getLevel(), newValue.getLevel());

        //更新权限模块
        sysAclModuleMapper.updateById(newValue);
        log.info("update acl module success!");

        //批量更新所有子权限模块
        childs.forEach(child -> PoCommonUtils.copyOperateInfo(sysAudit, child));
        sysAclModuleMapper.updateAll(childs);
        log.info("update level of childs success!");
    }

    private void doDel(SysAudit sysAudit) {
        Integer aclModuleId = sysAudit.getTargetId();
        sysAclModuleMapper.deleteById(aclModuleId);
        log.info("delete acl module success!");
    }
}
