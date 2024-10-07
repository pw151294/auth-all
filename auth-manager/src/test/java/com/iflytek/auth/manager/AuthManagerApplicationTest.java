package com.iflytek.auth.manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootApplication
@MapperScan(basePackages = "com.iflytek.auth.common.dao")
public class AuthManagerApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(AuthManagerApplicationTest.class, args);
    }
}
