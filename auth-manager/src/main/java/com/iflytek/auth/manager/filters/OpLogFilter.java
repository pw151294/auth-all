package com.iflytek.auth.manager.filters;

import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.manager.common.utils.LogHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 校验用户登录状态的拦截器
 */
@Slf4j
@Component
public class OpLogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init log filter success");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(true);
        SysUser sysUser = (SysUser) session.getAttribute("USER");
        SysLog sysLog = new SysLog();
        if (sysUser != null) {
            sysLog.setOperator(sysUser.getUsername());
        } else {
            sysLog.setOperator("UNKNOWN");
        }
        sysLog.setOperateIp(request.getRemoteAddr());
        LogHolder.setLog(sysLog);
        chain.doFilter(servletRequest, servletResponse);
    }
}
