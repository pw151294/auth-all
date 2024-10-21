package com.iflytek.auth.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
@Configuration
public class AppProperties {

    @Value("${jwt.access-token.id}")
    private String accessTokenId;

    @Value("${jwt.access-token.expire-time}")
    private long accessTokenExpireTime;

    @Value("${jwt.refresh-token.id}")
    private String refreshTokenId;

    @Value("${jwt.refresh-token.expire-time}")
    private long refreshTokenExpireTime;
}
