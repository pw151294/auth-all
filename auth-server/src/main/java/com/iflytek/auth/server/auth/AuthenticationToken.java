package com.iflytek.auth.server.auth;

import com.iflytek.auth.common.pojo.SysAcl;
import lombok.Data;

import java.util.Collection;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class AuthenticationToken implements Authentication {

    private String accessToken;

    private String refreshToken;

    private UserDetails userDetails;

    private boolean isAuthenticated;

    public AuthenticationToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<SysAcl> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public UserDetails getDetails() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }
}
