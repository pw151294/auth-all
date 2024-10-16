package com.iflytek.auth.manager.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.vo.SysDeptVo;
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
public class DeptServiceTest {

    @Test
    public void mockReq() {
        SysDeptVo sysDeptVo = new SysDeptVo();
        sysDeptVo.setParentId(0);
        sysDeptVo.setSeq(5);
        sysDeptVo.setName("技术平台与架构部");
        sysDeptVo.setRemark("二级部门");
        log.info("新增部门：{}", JSON.toJSONString(sysDeptVo));
    }
}
