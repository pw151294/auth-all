package com.iflytek.auth.manager.aspect;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.common.enums.LogType;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.common.pojo.SysOpLog;
import com.iflytek.auth.manager.logger.LoggerManager;
import com.iflytek.auth.manager.logger.SysLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Aspect
@Component
public class HandleLoggerAspect {

    @Autowired
    private LoggerManager loggerManager;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 1、com.iflytek.auth.manager.handler包下的所有PojoHandler接口的实现类
     * 2、1中所指向的类中doHandle方法
     */
    @Pointcut("execution(public * com.iflytek.auth.manager.handler.handlers..handle(..))")
    public void pointCut() {
    }

    // 2. 环绕通知
    @Around("pointCut()")
    public Object logOperation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs();
        SysAudit sysAudit = (SysAudit) args[0];
        SysLogger sysLogger = loggerManager.getLogger(sysAudit.getTargetType());
        if (sysLogger.getLogType().equals(LogType.OP.getLogType())) {
            SysOpLog sysOpLog = sysLogger.buildOpLog(sysAudit);
            try {
                Object result = proceedingJoinPoint.proceed();
                sysOpLog.setReturnValue(JSON.toJSONString(result));
                return result;
            } catch (Throwable e) {
                sysOpLog.setException(e.getLocalizedMessage());
                throw e;
            } finally {
                kafkaTemplate.send(AuthConstant.opLogTopic, JSON.toJSONString(sysOpLog));
            }
        } else {
            SysGrantLog sysGrantLog = sysLogger.buildGrantLog(sysAudit);
            try {
                Object result = proceedingJoinPoint.proceed();
                sysGrantLog.setGrantResult(1);
                return result;
            } catch (Throwable e) {
                sysGrantLog.setGrantResult(0);
                sysGrantLog.setException(e.getLocalizedMessage());
                throw e;
            } finally {
                kafkaTemplate.send(AuthConstant.grantLogTopic, JSON.toJSONString(sysGrantLog));
            }
        }
    }
}
