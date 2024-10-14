package com.iflytek.auth.manager.controller;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysUserDto;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.auth.manager.service.IUserService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/page")
    public RestResponse<PageInfo<SysUserVo>> pageUsers(@RequestBody SysUserDto sysUserDto) {
        return userService.pageUsers(sysUserDto);
    }

    @PostMapping("/add")
    public RestResponse addUser(@RequestBody SysUserDto sysUserDto) {
        return userService.addUser(sysUserDto);
    }

    @PostMapping("/update")
    public RestResponse updateUser(@RequestBody SysUserDto sysUserDto) {
        return userService.updateUser(sysUserDto);
    }

    @PostMapping("/delete/{userId}")
    public RestResponse deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }
}
