package com.iflytek.auth.manager.common;

import cn.hutool.crypto.digest.DigestUtil;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class MD5Test {

    private static final Logger logger = LoggerFactory.getLogger(MD5Test.class);

    private static final String password = "123456";

    @Test
    public void testEncrypt() {
        String dbPassword = DigestUtil.md5Hex(password);
        logger.info("密文:{}", dbPassword);
    }
}
