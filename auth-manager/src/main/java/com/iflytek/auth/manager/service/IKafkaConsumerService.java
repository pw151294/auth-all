package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.common.pojo.SysOpLog;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IKafkaConsumerService {

    void saveLog(SysGrantLog sysGrantLog);

    void saveLog(SysOpLog sysOpLog);
}
