package com.iflytek.auth.server.filters;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.auth.server.auth.SysUserInfo;
import com.iflytek.auth.server.auth.UserDetails;
import com.iflytek.auth.server.service.impl.SysUserInfoService;
import com.iflytek.auth.server.utils.JwtUtils;
import com.iflytek.auth.server.utils.SecurityContextHolder;
import com.iflytek.auth.server.utils.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 对于非登录请求 获取token进行身份认证 通过的话就将其认证信息设置到SecurityConntext上下文里
 */
@Component
public class AuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) {
        String exclusionUrls = filterConfig.getInitParameter(AuthConstant.exclusion_urls_key);
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        logger.info("exclusionUrlSet:{}", exclusionUrlSet);
        logger.info("init authentication filter success!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //判断是否需要认证
        logger.info("exclusionUrlSet:{}, url:{}", exclusionUrlSet, request.getServletPath());
        if (exclusionUrlSet.contains(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }

        //校验token的合法性
        String accessToken = request.getHeader(AuthConstant.accessTokenKey);
        String refreshToken = request.getHeader(AuthConstant.refreshTokenKey);
        if (!JwtUtils.validateAccessToken(accessToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "访问令牌不合法");
            return;
        }

        //解析accessToken获取用户信息
        UserDetails userDetails = JwtUtils.parseAccessJwtClaims(accessToken)
                .map(claims -> {
                    String username = claims.getSubject();
                    SysUserInfoService userInfoService = SpringUtil.getBean(SysUserInfoService.class);
                    return userInfoService.loadUserByUsername(username);
                }).orElse(new SysUserInfo());

        //将认证信息存储到Session和SecurityContextHolder里
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        authenticationToken.setUserDetails(userDetails);
        authenticationToken.setAuthenticated(true);
        SessionUtils.setAuthentication(authenticationToken);
        SecurityContextHolder.setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }
}
