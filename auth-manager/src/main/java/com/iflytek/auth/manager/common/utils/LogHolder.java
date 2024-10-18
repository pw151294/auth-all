package com.iflytek.auth.manager.common.utils;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.pojo.SysOpLog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * Holder类 用来在单线程内对操作日志对象进行操作
 */
@Slf4j
public class LogHolder {

    private static final ThreadLocal<SysOpLog> logThreadLocal = new ThreadLocal<>();

    public static void setLog(SysOpLog sysOpLog) {
        logThreadLocal.set(sysOpLog);
        log.info("save log success: {}", JSON.toJSONString(sysOpLog));
    }

    public static SysOpLog getLog() {
        return logThreadLocal.get();
    }

    public static void clear() {
        logThreadLocal.remove();
    }
}