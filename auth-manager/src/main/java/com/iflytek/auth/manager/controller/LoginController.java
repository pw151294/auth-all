package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.common.utils.SessionUtils;
import com.iflytek.auth.common.dto.SysUserDto;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.manager.service.IUserService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    public RestResponse login(@RequestBody SysUserDto sysUserDto) {
        RestResponse loginResp = userService.login(sysUserDto);
        if (loginResp.getSuccess()) {
            SysUser sysUser = (SysUser) loginResp.getData();
            SessionUtils.setUser(sysUser);
            return RestResponse.buildSuccess("用户登录成功");
        }
        return loginResp;
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) throws IOException {
        SessionUtils.removeUser();
        response.sendRedirect(AuthConstant.loginUrl);
    }
}
