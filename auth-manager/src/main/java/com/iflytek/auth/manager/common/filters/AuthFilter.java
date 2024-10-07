package com.iflytek.auth.manager.common.filters;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.manager.common.utils.ApplicationContextUtils;
import com.iflytek.auth.common.common.utils.SessionUtils;
import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.itsc.web.exception.BaseBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 由于没有前端页面 后端无法在会话设置用户信息
 * 设置过滤器将用户信息插入到会话里 不符合业务逻辑 只是单纯用于用户后端自测 代码封板时会删除
 */
@Component
public class AuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) {
        String exclusionUrls = filterConfig.getInitParameter(AuthConstant.exclusion_urls_key);
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        logger.info("init auth filter success!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //判断是否直接放行
        if (exclusionUrlSet.contains(httpServletRequest.getServletPath())) {
            chain.doFilter(request, response);
        }

        //将用户信息设置到会话中
        SysUserMapper userMapper = ApplicationContextUtils.getBean(SysUserMapper.class);
        String username = httpServletRequest.getHeader("NAME");
        logger.info("用户名：{}", username);
        SysUser sysUser = userMapper.findByUsername(username);
        if (sysUser == null) {
            throw new BaseBizException("用户不存在！");
        }
        logger.info("当前登录用户信息：{}", JSON.toJSONString(sysUser));
        SessionUtils.setUser(sysUser);
        chain.doFilter(request, response);
    }
}
