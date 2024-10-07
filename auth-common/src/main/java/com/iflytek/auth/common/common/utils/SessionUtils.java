package com.iflytek.auth.common.common.utils;

import com.iflytek.auth.common.pojo.SysUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class SessionUtils {

    private static SessionUtils instance = getInstance();

    private static HttpServletRequest request;

    private static HttpSession session;

    private SessionUtils() {
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        session = request.getSession();
    }

    private static SessionUtils getInstance() {
        if (instance == null) {
            synchronized (SessionUtils.class) {
                if (instance == null) {
                    instance = new SessionUtils();
                }
            }
        }

        return instance;
    }

    public static void setUser(SysUser user) {
        session.setAttribute("USER", user);
    }

    public static SysUser getUser() {
        return (SysUser) session.getAttribute("USER");
    }

    public static void removeUser() {
        session.removeAttribute("USER");
    }
}
