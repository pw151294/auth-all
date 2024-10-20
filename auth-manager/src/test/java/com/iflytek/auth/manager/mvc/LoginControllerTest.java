package com.iflytek.auth.manager.mvc;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLogin() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("weipan4");
        loginDto.setRawPassword("123456");

        String resp = mockMvc.perform(
                        post("/login/token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSON.toJSONString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("登录成功：{}", resp);
    }
}
