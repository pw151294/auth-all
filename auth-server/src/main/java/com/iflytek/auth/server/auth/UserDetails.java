package com.iflytek.auth.server.auth;

import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.pojo.SysRole;

import java.util.Collection;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface UserDetails {

    Collection<SysAcl> getAuthorities();

    Collection<SysRole> getRoles();

    String getPassword();

    String getUsername();

    boolean isEnabled();
}
