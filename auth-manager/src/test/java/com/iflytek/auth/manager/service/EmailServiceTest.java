package com.iflytek.auth.manager.service;

import com.iflytek.auth.manager.AuthManagerApplicationTest;
import com.iflytek.auth.server.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class EmailServiceTest {

    @Resource
    private EmailService emailService;

    @Test
    public void testSendMessage() {
        emailService.sendEmail("pw151294@mail.ustc.edu.cn", "123456");
    }
}
