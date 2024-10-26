package com.iflytek.auth.server.service;

import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.common.dto.OAuth2LoginResultDto;
import com.iflytek.auth.server.auth.Authentication;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface ILoginService {

    RestResponse<AuthenticationToken> createToken(LoginDto loginDto);

    RestResponse<AuthenticationToken> refreshToken();

    ResponseEntity multiFactorAuthentication(LoginDto loginDto);

    RestResponse<AuthenticationToken> verify(String verifyCode);

    RestResponse<OAuth2LoginResultDto> oAuth2Login(LoginDto loginDto, HttpServletResponse response);

    RestResponse<Authentication> oAuth2Token(HttpServletRequest request);
}
