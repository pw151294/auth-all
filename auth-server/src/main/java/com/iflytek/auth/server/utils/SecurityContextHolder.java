package com.iflytek.auth.server.utils;

import com.iflytek.auth.server.auth.Authentication;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
public class SecurityContextHolder {

    private static final ThreadLocal<Authentication> threadLocal = new ThreadLocal<>();

    public static void setAuthentication(Authentication authentication) {
        threadLocal.set(authentication);
        log.info("set authentication success!");
    }

    public static Authentication getAuthentication() {
        return threadLocal.get();
    }

    public static void clearContext() {
        threadLocal.remove();
    }
}
