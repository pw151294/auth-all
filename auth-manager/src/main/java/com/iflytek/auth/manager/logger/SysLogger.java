package com.iflytek.auth.manager.logger;

import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.common.pojo.SysOpLog;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface SysLogger {

    Integer getLogType();

    Integer getTargetType();

    SysOpLog getOpLog(SysAudit sysAudit);

    SysGrantLog getGrantLog(SysAudit sysAudit);
}
