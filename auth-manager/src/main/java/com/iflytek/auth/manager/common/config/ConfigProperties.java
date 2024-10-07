package com.iflytek.auth.manager.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Configuration
@Data
public class ConfigProperties {

    @Value("${auth.manager.filter.login.excludeUrls}")
    private String excludeUrls;

    @Value("${auth.manager.login.url}")
    private String loginUrl;

    @Value("${server.servlet.context-path}")
    private String contextPath;
}
