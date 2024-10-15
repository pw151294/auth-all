package com.iflytek.auth.manager.common.config;

import cn.hutool.extra.spring.SpringUtil;
import com.iflytek.auth.common.dao.SysAuditMapper;
import com.iflytek.auth.common.dao.SysLogMapper;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.manager.common.handlers.PojoHandler;
import com.iflytek.auth.manager.common.task.AuditHandler;
import com.iflytek.auth.manager.common.task.SysTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Configuration
public class TaskConfig {

    @Resource
    private ConfigProperties configProperties;

    @Bean
    public SysTask logTask() {
        SysLogMapper sysLogMapper = SpringUtil.getBean(SysLogMapper.class);
        SysTask<SysLog> logTask = new SysTask<>();
        logTask.init(configProperties.getQueueSize(), configProperties.getPoolSize(), sysLogMapper);

        return logTask;
    }

    @Bean
    public SysTask auditTask() {
        SysAuditMapper sysAuditMapper = SpringUtil.getBean(SysAuditMapper.class);
        SysTask<SysAudit> auditTask = new SysTask<>();
        auditTask.init(configProperties.getQueueSize(), configProperties.getPoolSize(), sysAuditMapper);

        return auditTask;
    }

    @Bean
    public AuditHandler auditHandler() {
        AuditHandler auditHandler = new AuditHandler();
        auditHandler.init(configProperties.getQueueSize());

        //插入实体类handler
        String[] handlerNames = SpringUtil.getBeanNamesForType(PojoHandler.class);
        Arrays.stream(handlerNames).forEach(handlerName -> {
            PojoHandler pojoHandler = SpringUtil.getBean(handlerName);
            auditHandler.setHandler(pojoHandler);
        });

        return auditHandler;
    }
}
