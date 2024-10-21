package com.iflytek.auth.manager.logger.loggers;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.common.enums.LogType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
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
public class SysAclModuleLogger implements SysLogger {

    @Override
    public Integer getLogType() {
        return LogType.OP.getLogType();
    }

    @Override
    public Integer getTargetType() {
        return TargetType.ACL_MODULE.getType();
    }

    @Override
    public SysOpLog buildOpLog(SysAudit sysAudit) {
        SysOpLog sysOpLog = new SysOpLog();
        PoCommonUtils.copyOpLogProperties(sysAudit, sysOpLog);
        switch (sysAudit.getOperationType()) {
            case 1:
                sysOpLog.setInterfaceName(AuthConstant.aclModuleAddMethodName);
                break;
            case 2:
                sysOpLog.setInterfaceName(AuthConstant.aclModuleUpdateMethodName);
                break;
            case 3:
                sysOpLog.setInterfaceName(AuthConstant.aclModuleDeleteMethodName);
                break;
            default:
                break;
        }
        log.info("create sys op log success:{}", JSON.toJSONString(sysOpLog));
        return sysOpLog;
    }

    @Override
    public SysGrantLog buildGrantLog(SysAudit sysAudit) {
        return null;
    }
}