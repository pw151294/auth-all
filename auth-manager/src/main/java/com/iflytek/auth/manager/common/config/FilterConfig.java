package com.iflytek.auth.manager.common.config;

import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.manager.common.filters.AuthFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Configuration
public class FilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    @Autowired
    private ConfigProperties configProperties;

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterFilterRegistrationBean() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter(AuthConstant.exclusion_urls_key, configProperties.getExcludeUrls());
        registrationBean.setOrder(0);
        logger.info("register auth filter success！");

        return registrationBean;
    }
}
