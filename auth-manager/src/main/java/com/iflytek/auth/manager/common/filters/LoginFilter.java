package com.iflytek.auth.manager.common.filters;

import com.iflytek.auth.common.pojo.SysUser;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 校验用户登录状态的拦截器
 */
//@Component
public class LoginFilter implements Filter {

    @Value("${auth.manager.login.url}")
    private String loginUrl;

//    @Value("${auth.manage.filter.login.excludeUrls}")
//    private String excludeUrls;

//    @Autowired
//    private SysAclMapper aclMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //获取用户信息 如果用户信息为空则跳转到登录页 如果非空则进行权限校验
        SysUser sysUser = (SysUser) httpServletRequest.getSession().getAttribute("USER");
        if (sysUser == null) {
            httpServletResponse.sendRedirect(loginUrl);
        }

//        //判断用户是否具有当前请求的权限 有则放行 没有则拦截
//        List<String> urls = aclMapper.findAclsByUserId(sysUser.getId())
//                .stream().map(SysAcl::getUrl)
//                .collect(Collectors.toList());
//        String requestPath = httpServletRequest.getServletPath();
//        if (urls.stream().anyMatch(url -> UrlUtils.hasAcl(url, requestPath))) {
//            chain.doFilter(request, response);
//        }
    }
}
