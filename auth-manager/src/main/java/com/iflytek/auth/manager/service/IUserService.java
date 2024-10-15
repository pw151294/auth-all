package com.iflytek.auth.manager.service;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysUserDto;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IUserService {

    RestResponse<PageInfo<SysUserVo>> pageUsers(SysUserDto sysUserDto);

    RestResponse addUser(SysUserDto sysUserDto);

    RestResponse updateUser(SysUserDto sysUserDto);

    RestResponse deleteUser(Integer userId);

    RestResponse login(SysUserDto sysUserDto);

    RestResponse submitAddUser(SysUserDto sysUserDto);

    RestResponse submitUpdateUser(SysUserDto sysUserDto);

    RestResponse submitDeleteUser(Integer userId);
}
