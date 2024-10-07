package com.iflytek.auth.manager.controller;

import com.iflytek.itsc.web.exception.BaseBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RequestMapping("/test")
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping("/interceptor")
    public void interceptor() {
        logger.info("request success!");
    }

    @PostMapping("/interceptor/ex")
    public void interceptorEx() {
        throw new BaseBizException("request fail!");
    }
}
