package com.iflytek.auth.manager.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.dao.SysGrantLogMapper;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.manager.service.IKafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Service
public class KafkaConsumerService implements IKafkaConsumerService {

    @KafkaListener(topics = AuthConstant.grantLogTopic, groupId = AuthConstant.grantLogGroupId)
    public void listen(ConsumerRecord<String, String> record) {
        String logJsonStr = record.value();
        SysGrantLog sysGrantLog = JSON.parseObject(logJsonStr, SysGrantLog.class);
        saveLog(sysGrantLog);
    }

    @Override
    public void saveLog(SysGrantLog sysGrantLog) {
        SysGrantLogMapper sysGrantLogMapper = SpringUtil.getBean(SysGrantLogMapper.class);
        sysGrantLogMapper.insert(sysGrantLog);
        log.info("save grant log success!");
    }
}
