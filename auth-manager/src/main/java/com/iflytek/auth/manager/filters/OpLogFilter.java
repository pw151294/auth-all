package com.iflytek.auth.manager.filters;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.manager.common.utils.IpUtils;
import com.iflytek.auth.manager.common.utils.LogHolder;
import com.iflytek.auth.server.auth.Authentication;
import com.iflytek.auth.server.utils.SecurityContextHolder;
import com.iflytek.auth.server.auth.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 校验用户登录状态的拦截器
 */
@Slf4j
@Component
public class OpLogFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter(AuthConstant.exclusion_urls_key);
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        log.info("init log filter success");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //判断是否放行
        log.info("exclusionUrlSet:{}, url:{}", exclusionUrlSet, request.getServletPath());
        if (exclusionUrlSet.contains(request.getServletPath())) {
            chain.doFilter(request, servletResponse);
            return;
        }

        //TODO 判断具体需要实现哪两个方案
        //从Session里获取用户认证信息
//        HttpSession session = request.getSession(true);
//        SysUser sysUser = (SysUser) session.getAttribute("USER");
//        SysLog sysLog = new SysLog();
//        if (sysUser != null) {
//            sysLog.setOperator(sysUser.getUsername());
//        } else {
//            sysLog.setOperator("UNKNOWN");
//        }
//        sysLog.setOperateIp(request.getRemoteAddr());
//        LogHolder.setLog(sysLog);
//        chain.doFilter(servletRequest, servletResponse);
        // 从SecurityContextHolder里获取认证信息
        Authentication authentication = SecurityContextHolder.getAuthentication();
        UserDetails userDetails = authentication.getDetails();
        SysLog sysLog = new SysLog();
        sysLog.setOperator(userDetails.getUsername());
        sysLog.setOperateIp(IpUtils.getIpAddress(request));
        LogHolder.setLog(sysLog);
        chain.doFilter(request, servletResponse);
    }
}
