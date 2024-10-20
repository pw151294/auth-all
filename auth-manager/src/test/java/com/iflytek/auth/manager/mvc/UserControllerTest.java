package com.iflytek.auth.manager.mvc;

import com.iflytek.auth.manager.AuthManagerApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testPageUsers() throws Exception {
        mockMvc.perform(
                post("/user/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("NAME", "ADMIN")
                        .queryParam("keyWord", "@qq.com")
        ).andExpect(status().isOk());
    }
}
