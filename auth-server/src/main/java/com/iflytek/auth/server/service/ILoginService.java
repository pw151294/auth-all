package com.iflytek.auth.server.service;

import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface ILoginService {

    RestResponse<AuthenticationToken> createToken(LoginDto loginDto);

    RestResponse<AuthenticationToken> refreshToken();
}
