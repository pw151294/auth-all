package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.pojo.SysUser;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAuditService {

    void submitUserAdd(SysUser newUser);

    void submitUserUpdate(SysUser oldUser, SysUser newUser);

    void submitUserDelete(Integer userId);
}
