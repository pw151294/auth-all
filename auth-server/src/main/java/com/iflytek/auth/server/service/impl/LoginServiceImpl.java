package com.iflytek.auth.server.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.auth.server.auth.UserDetails;
import com.iflytek.auth.server.service.ILoginService;
import com.iflytek.auth.server.utils.JwtUtils;
import com.iflytek.itsc.web.response.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SysUserInfoService userInfoService;

    @Override
    public RestResponse<AuthenticationToken> createToken(LoginDto loginDto) {
        //加载用户信息
        UserDetails userDetails = userInfoService.loadUserByUsername(loginDto.getUsername());
        //校验用户名和密码
        if (!StringUtils.equals(userDetails.getUsername(), loginDto.getUsername())
                || !StringUtils.equals(userDetails.getPassword(), DigestUtil.md5Hex(loginDto.getRawPassword()))) {
            return RestResponse.buildError("用户名或者密码错误");
        }
        //创建认证信息
        String accessToken = jwtUtils.createAccessToken(userDetails);
        String refreshToken = jwtUtils.createRefreshToken(userDetails);
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        authenticationToken.setUserDetails(userDetails);

        return RestResponse.buildSuccess(authenticationToken);
    }

    @Override
    public RestResponse<AuthenticationToken> refreshToken() {
        //从请求头里获取到刷新令牌
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String refreshToken = request.getHeader(AuthConstant.refreshTokenKey);
        if (!JwtUtils.validateRefreshToken(refreshToken)) {
            return RestResponse.buildError("刷新令牌不合法或者已过期");
        }
        //根据刷新令牌生成访问令牌
        String accessToken = jwtUtils.createAccessTokenByRefreshToken(refreshToken);
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        return RestResponse.buildSuccess(authenticationToken);
    }
}
