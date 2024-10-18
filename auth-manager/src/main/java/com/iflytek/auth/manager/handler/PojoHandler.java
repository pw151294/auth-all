package com.iflytek.auth.manager.handler;

import com.iflytek.auth.common.pojo.SysAudit;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface PojoHandler {

    Integer getTargetType();

    void handle(SysAudit sysAudit);
}
