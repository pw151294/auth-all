package com.iflytek.auth.common.common.enums;

import lombok.Getter;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Getter
public enum LogType {

    OP(0, "操作日志"),
    GRANT(1, "授权日志"),
    AUDIT(2, "审核日志"),
    LOGIN(3, "登录日志");

    private Integer logType;

    private String desc;

    LogType(Integer logType, String desc) {
        this.logType = logType;
        this.desc = desc;
    }
}
