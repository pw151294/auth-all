package com.iflytek.auth.manager.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.auth.common.dto.SysAuditDto.SysAuditItem;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class AuditServiceTest {

    @Test
    public void mockReqResp() {
        SysAuditDto sysAuditDto = new SysAuditDto();
        sysAuditDto.setRemark("审核通过");

        SysAuditItem auditItem = new SysAuditItem();
        auditItem.setAuditId(25);
        auditItem.setResult(1);
        sysAuditDto.getItems().add(auditItem);

        log.info("进行审核：{}", JSON.toJSONString(sysAuditDto));
    }
}
