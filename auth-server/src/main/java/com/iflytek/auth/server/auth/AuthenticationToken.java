package com.iflytek.auth.server.auth;

import com.google.common.collect.Lists;
import com.iflytek.auth.common.pojo.SysAcl;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class AuthenticationToken implements Authentication, Serializable {

    private static final long serialVersionUID = 1L;

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
        return Optional.ofNullable(userDetails)
                .map(UserDetails::getAuthorities)
                .orElse(Lists.newArrayList());
    }

    @Override
    public UserDetails getUserDetails() {
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
