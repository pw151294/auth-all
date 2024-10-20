package com.iflytek.auth.server;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface UserDetailsService {

    UserDetails loadUserByUsername(String username);
}
