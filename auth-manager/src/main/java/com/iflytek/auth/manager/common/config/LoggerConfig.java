package com.iflytek.auth.manager.common.config;

import cn.hutool.extra.spring.SpringUtil;
import com.iflytek.auth.manager.logger.LoggerManager;
import com.iflytek.auth.manager.logger.SysLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Configuration
public class LoggerConfig {

    @Bean
    public LoggerManager loggerManager() {
        log.info("begin init sys loggers >>>>>>>>>>");
        LoggerManager loggerManager = new LoggerManager();
        String[] loggerNames = SpringUtil.getBeanNamesForType(SysLogger.class);
        Arrays.stream(loggerNames).forEach(loggerName -> {
            SysLogger sysLogger = SpringUtil.getBean(loggerName);
            loggerManager.setLogger(sysLogger);
        });
        log.info("init sys loggers success!");
        return loggerManager;
    }
}
