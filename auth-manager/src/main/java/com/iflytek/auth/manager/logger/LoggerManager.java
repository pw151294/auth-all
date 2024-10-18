package com.iflytek.auth.manager.logger;

import com.google.common.collect.Maps;
import com.iflytek.auth.common.common.enums.TargetType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
public class LoggerManager {

    private Map<Integer, SysLogger> typeLoggerMap = Maps.newHashMap();

    public void setLogger(SysLogger sysLogger) {
        typeLoggerMap.put(sysLogger.getTargetType(), sysLogger);
        log.info("set log success, targetType:{}, name:{}",
                TargetType.getTargetType(sysLogger.getTargetType()), sysLogger);
    }

    public SysLogger getLogger(Integer targetType) {
        return typeLoggerMap.get(targetType);
    }
}
