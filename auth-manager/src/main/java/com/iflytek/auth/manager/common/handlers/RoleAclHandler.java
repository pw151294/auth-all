package com.iflytek.auth.manager.common.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysRoleAclMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysRoleAcl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Component
@Slf4j
public class RoleAclHandler implements PojoHandler {

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.ROLE_ACL.getType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SysAudit sysAudit) {
        log.info("begin to handler role acl operation >>>>>>>>>>");
        doHandle(sysAudit);
    }

    private void doHandle(SysAudit sysAudit) {
        Integer roleId = sysAudit.getTargetId();
        List<Integer> aclIds = JSON.parseArray(sysAudit.getNewValue(), Integer.class);
        List<SysRoleAcl> sysRoleAcls = aclIds.stream().map(aclId -> {
            SysRoleAcl sysRoleAcl = new SysRoleAcl();
            sysRoleAcl.setRoleId(roleId);
            sysRoleAcl.setAclId(aclId);
            PoCommonUtils.copyOperateInfo(sysAudit, sysRoleAcl);
            return sysRoleAcl;
        }).collect(Collectors.toList());

        //先删除后新增
        sysRoleAclMapper.deleteByRoleId(roleId);
        sysRoleAclMapper.insertBatch(sysRoleAcls);
        log.info("update role acl relation success!");
    }
}
