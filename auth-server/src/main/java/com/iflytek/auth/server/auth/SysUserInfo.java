package com.iflytek.auth.server.auth;

import com.google.common.collect.Lists;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.pojo.SysRole;
import com.iflytek.auth.common.pojo.SysUser;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@NoArgsConstructor
public class SysUserInfo implements UserDetails, Serializable {

    private String username;

    private String password;

    private Integer status;

    private List<SysRole> sysRoles = Lists.newArrayList();

    private List<SysAcl> sysAcls = Lists.newArrayList();

    public SysUserInfo(SysUser sysUser) {
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.status = sysUser.getStatus();
    }

    @Override
    public Collection<SysAcl> getAuthorities() {
        return sysAcls;
    }

    @Override
    public Collection<SysRole> getRoles() {
        return sysRoles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(1);
    }
}
