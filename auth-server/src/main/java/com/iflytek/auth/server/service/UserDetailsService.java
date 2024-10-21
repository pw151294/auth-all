package com.iflytek.auth.server.service;

import com.iflytek.auth.server.auth.UserDetails;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface UserDetailsService {

    UserDetails loadUserByUsername(String username);
}
