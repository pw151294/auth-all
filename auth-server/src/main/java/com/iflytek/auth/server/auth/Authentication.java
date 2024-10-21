package com.iflytek.auth.server.auth;

import com.iflytek.auth.common.pojo.SysAcl;

import java.util.Collection;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface Authentication {

    Collection<SysAcl> getAuthorities();

    UserDetails getDetails();

    boolean isAuthenticated();

    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
