package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.server.AuthenticationToken;
import com.iflytek.auth.server.ILoginService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @PostMapping("/token")
    public RestResponse<AuthenticationToken> token(@Valid @RequestBody LoginDto loginDto) {
        return loginService.createToken(loginDto);
    }

    @PostMapping("/refreshToken")
    private RestResponse<AuthenticationToken> refreshToken() {
        return loginService.refreshToken();
    }
}
