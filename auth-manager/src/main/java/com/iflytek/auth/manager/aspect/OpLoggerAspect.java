package com.iflytek.auth.manager.aspect;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.dao.SysOpLogMapper;
import com.iflytek.auth.common.pojo.SysOpLog;
import com.iflytek.auth.manager.annotations.OpLogger;
import com.iflytek.auth.manager.common.utils.LogHolder;
import com.iflytek.itsc.web.response.RestResponse;
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
@Aspect
@Component
public class OpLoggerAspect {

    @Autowired
    private SysOpLogMapper sysOpLogMapper;

    // 定义切点：所有带有@Logger注解的方法
    @Pointcut("@annotation(com.iflytek.auth.manager.annotations.OpLogger)")
    public void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取方法参数 操作类型 和响应结果
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        OpLogger opLogger = signatureMethod.getAnnotation(OpLogger.class);
        SysOpLog sysOpLog = LogHolder.getLog();
        sysOpLog.setInterfaceName(signatureMethod.getName());
        sysOpLog.setParam(JSON.toJSONString(proceedingJoinPoint.getArgs()));
        sysOpLog.setType(opLogger.opType());

        //执行方法
        try {
            Object result = proceedingJoinPoint.proceed();
            RestResponse response = (RestResponse) result;
            //响应成功设置数据 响应失败设置错误信息
            if (response.getSuccess()) {
                sysOpLog.setReturnValue(JSON.toJSONString(response.getData()));
            } else {
                sysOpLog.setReturnValue(String.format("错误码：%s, 错误信息：%s",
                        response.getErrorCode(), response.getErrorMessage()));
            }
            sysOpLogMapper.insert(sysOpLog);
            return response;
        } catch (Throwable e) {
            //补充异常信息 再抛出异常
            sysOpLog.setException(String.format("程序运行时抛出异常：%s", e.getLocalizedMessage()));
            sysOpLogMapper.insert(sysOpLog);
            throw e;
        } finally {
            LogHolder.clear();
        }
    }
}
