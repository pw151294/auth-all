package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.pojo.SysGrantLog;
import com.iflytek.auth.manager.service.IKafkaProduceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
@Slf4j
public class KafkaProduceService implements IKafkaProduceService {


    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProduceService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void sendLog(SysGrantLog sysGrantLog) {
        kafkaTemplate.send(AuthConstant.grantLogTopic, JSON.toJSONString(sysGrantLog));
        log.info("send grant log into kafka success!");
    }
}
