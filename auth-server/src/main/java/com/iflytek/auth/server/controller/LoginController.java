package com.iflytek.auth.server.controller;

import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.auth.server.service.ILoginService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/mfa")
    public ResponseEntity mfa(@Valid @RequestBody LoginDto loginDto) {
        return loginService.multiFactorAuthentication(loginDto);
    }

    @PostMapping("/verify")
    public RestResponse<AuthenticationToken> verify(HttpServletRequest request) {
        return loginService.verify(request.getHeader("verifyCode"));
    }
}
