package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import com.iflytek.auth.server.service.EmailService;
import com.iflytek.auth.server.utils.TotpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Instant;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class EmailServiceTest {

    @Resource
    private EmailService emailService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private TotpUtils totpUtils;

    @Test
    public void testSendMessage() {
        emailService.sendEmail("pw151294@mail.ustc.edu.cn", "123456");
    }

    @Test
    public void generateKey() {
        sysUserMapper.selectList(null)
                .forEach(sysUser -> {
                    Key key = totpUtils.generateKey();
                    sysUser.setRemark(totpUtils.encodeKeyToString(key));
                    sysUserMapper.updateById(sysUser);
                });
    }

    // 生成验证码
    @Test
    public void generateCode() throws InvalidKeyException {
        SysUser sysUser = sysUserMapper.selectById(1);
        String keyStr = sysUser.getRemark();
        Key key = totpUtils.decodeKeyFromString(keyStr);
        String totp = totpUtils.createTotp(key, Instant.now());
    }
}
