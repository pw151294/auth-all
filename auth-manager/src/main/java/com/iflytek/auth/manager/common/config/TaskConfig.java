package com.iflytek.auth.manager.common.config;

import com.iflytek.auth.manager.common.utils.LogUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Configuration
public class TaskConfig {

    @Resource
    private ConfigProperties configProperties;

    @Bean
    public LogUtils logUtils() {
        LogUtils logUtils = new LogUtils();
        logUtils.init(configProperties.getQueueSize(), configProperties.getPoolSize());

        return logUtils;
    }
}
