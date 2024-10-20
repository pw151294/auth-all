package com.iflytek.auth.server;

import cn.hutool.extra.spring.SpringUtil;
import com.iflytek.auth.common.dao.SysAclMapper;
import com.iflytek.auth.common.dao.SysRoleMapper;
import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.pojo.SysUser;
import org.springframework.stereotype.Service;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class SysUserInfoService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUserMapper sysUserMapper = SpringUtil.getBean(SysUserMapper.class);
        SysUser sysUser = sysUserMapper.findByUsernameIgnoreStatus(username);
        if (sysUser == null) {
            return new SysUserInfo();
        }

        SysUserInfo sysUserInfo = new SysUserInfo(sysUser);
        SysRoleMapper sysRoleMapper = SpringUtil.getBean(SysRoleMapper.class);
        sysUserInfo.getRoles().addAll(sysRoleMapper.findRoleByUserName(username));
        SysAclMapper sysAclMapper = SpringUtil.getBean(SysAclMapper.class);
        sysUserInfo.getAuthorities().addAll(sysAclMapper.findAclByUserName(username));

        return sysUserInfo;
    }
}
