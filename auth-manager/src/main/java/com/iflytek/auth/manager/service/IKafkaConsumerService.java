package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.pojo.SysGrantLog;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IKafkaConsumerService {

    void saveLog(SysGrantLog sysGrantLog);
}
