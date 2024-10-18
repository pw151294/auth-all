package com.iflytek.auth.manager.aspect;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dto.SysRoleAclDto;
import com.iflytek.auth.common.dto.SysRoleUserDto;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.manager.annotations.GrantLogger;
import com.iflytek.auth.manager.common.utils.LogHolder;
import com.iflytek.auth.manager.service.IKafkaProduceService;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Aspect
@Component
public class GrantLoggerAspect {

    @Autowired
    private IKafkaProduceService produceService;

    // 定义切点：所有带有@Logger注解的方法
    @Pointcut("@annotation(com.iflytek.auth.manager.annotations.GrantLogger)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取授权类型 被授权内容类型
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        GrantLogger grantLogger = signatureMethod.getAnnotation(GrantLogger.class);
        int targetType = grantLogger.targetType();
        int grantType = grantLogger.grantType();
        SysLog sysLog = LogHolder.getLog();

        //创建授权日志
        SysGrantLog sysGrantLog = new SysGrantLog();
        PoCommonUtils.copyLogProperties(sysLog, sysGrantLog);
        sysGrantLog.setTargetType(targetType);
        sysGrantLog.setGrantType(grantType);
        //获取授权参数
        Object arg = proceedingJoinPoint.getArgs()[0];
        if (TargetType.USER.getType().equals(grantType)) {
            SysRoleUserDto roleUserDto = (SysRoleUserDto) arg;
            sysGrantLog.setTargetId(roleUserDto.getRoleId());
            sysGrantLog.setGrantIds(JSON.toJSONString(roleUserDto.getUserIds()));
        } else {
            SysRoleAclDto roleAclDto = (SysRoleAclDto) arg;
            sysGrantLog.setTargetId(roleAclDto.getRoleId());
            sysGrantLog.setGrantIds(JSON.toJSONString(roleAclDto.getAclIds()));
        }

        //执行方法获取结果
        try {
            Object result = proceedingJoinPoint.proceed();
            RestResponse response = (RestResponse) result;
            if (response.getSuccess()) {
                sysGrantLog.setGrantResult(1);
            } else {
                sysGrantLog.setGrantResult(0);
            }
            produceService.sendLog(sysGrantLog);

            return response;
        } catch (Throwable e) {
            sysGrantLog.setGrantResult(0);
            sysGrantLog.setException(e.getLocalizedMessage());
            throw e;
        } finally {
            LogHolder.clear();
        }

    }
}
