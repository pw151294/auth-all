package com.iflytek.auth.manager.logger.loggers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.enums.LogType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.common.pojo.SysOpLog;
import com.iflytek.auth.manager.logger.SysLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Component
@Slf4j
public class SysRoleUserLogger implements SysLogger {

    @Override
    public Integer getLogType() {
        return LogType.GRANT.getLogType();
    }

    @Override
    public Integer getTargetType() {
        return TargetType.ROLE_USER.getType();
    }

    @Override
    public SysOpLog getOpLog(SysAudit sysAudit) {
        return null;
    }

    @Override
    public SysGrantLog getGrantLog(SysAudit sysAudit) {
        SysGrantLog sysGrantLog = new SysGrantLog();
        sysGrantLog.setTargetType(TargetType.ROLE.getType());
        sysGrantLog.setGrantType(TargetType.USER.getType());
        log.info("create sys grant log success:{}", JSON.toJSONString(sysGrantLog));
        return sysGrantLog;
    }
}
