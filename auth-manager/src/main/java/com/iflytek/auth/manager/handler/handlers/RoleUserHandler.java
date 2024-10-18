package com.iflytek.auth.manager.handler.handlers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysRoleUserMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysRoleUser;
import com.iflytek.auth.manager.handler.PojoHandler;
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
@Slf4j
@Component
public class RoleUserHandler implements PojoHandler {

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public Integer getTargetType() {
        return TargetType.ROLE_USER.getType();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handle(SysAudit sysAudit) {
        log.info("beigin to handler role_user operation >>>>>>>>>>");
        doHandle(sysAudit);
    }

    public void doHandle(SysAudit sysAudit) {
        Integer roleId = sysAudit.getTargetId();
        List<Integer> userIds = JSON.parseArray(sysAudit.getNewValue(), Integer.class);
        List<SysRoleUser> roleUsers = userIds.stream().map(userId -> {
            SysRoleUser sysRoleUser = new SysRoleUser();
            sysRoleUser.setRoleId(roleId);
            sysRoleUser.setUserId(userId);
            PoCommonUtils.copyOperateInfo(sysAudit, sysRoleUser);
            return sysRoleUser;
        }).collect(Collectors.toList());

        //先删除后新增
        sysRoleUserMapper.deleteByRoleId(roleId);
        sysRoleUserMapper.insertBatch(roleUsers);
        log.info("update role user relation success!");
    }
}
