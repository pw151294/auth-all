package com.iflytek.auth.server;

import cn.hutool.crypto.digest.DigestUtil;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.itsc.web.response.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        AuthenticationToken authenticationToken = SessionUtils.getAuthentication();
        Preconditions.checkNotNull(authenticationToken, "用户认证信息不能为空！");
        String accessToken = authenticationToken.getAccessToken();
        String refreshToken = authenticationToken.getRefreshToken();
        if (!JwtUtils.validateAccessTokenIgnoreExpire(accessToken)) {
            return RestResponse.buildError("访问令牌不合法");
        }
        if (!JwtUtils.validateRefreshToken(refreshToken)) {
            return RestResponse.buildError("刷新令牌不合法或者已过期");
        }
        authenticationToken.setAccessToken(jwtUtils.createAccessTokenByRefreshToken(refreshToken));

        return RestResponse.buildSuccess(authenticationToken);
    }
}
