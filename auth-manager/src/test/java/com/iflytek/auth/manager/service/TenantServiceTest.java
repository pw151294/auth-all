package com.iflytek.auth.manager.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.dto.SysTenantDto;
import com.iflytek.auth.common.dto.SysTenantUserDto;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class TenantServiceTest {

    @Autowired
    private ITenantService tenantService;


    @Test
    public void testAdd() {
        SysTenantDto sysTenantDto = new SysTenantDto();
        sysTenantDto.setName("科技大脑");
        sysTenantDto.setStatus(1);
        sysTenantDto.setRemark("支撑");
        log.info("新增租户：{}", JSON.toJSONString(sysTenantDto));

        tenantService.add(sysTenantDto);
    }

    @Test
    public void testAlloUsers() {
        SysTenantUserDto sysTenantUserDto1 = new SysTenantUserDto();
        sysTenantUserDto1.setTenantId(23);
        sysTenantUserDto1.setUserIds(Lists.newArrayList(1));
        RestResponse resp1 = tenantService.alloUsers(sysTenantUserDto1);
        assert resp1.getSuccess();

        SysTenantUserDto sysTenantUserDto2 = new SysTenantUserDto();
        sysTenantUserDto2.setTenantId(24);
        sysTenantUserDto2.setUserIds(Lists.newArrayList(2, 3));
        RestResponse resp2 = tenantService.alloUsers(sysTenantUserDto2);
        assert resp2.getSuccess();

        SysTenantUserDto sysTenantUserDto3 = new SysTenantUserDto();
        sysTenantUserDto3.setTenantId(25);
        sysTenantUserDto3.setUserIds(Lists.newArrayList(4, 5));
        RestResponse resp3 = tenantService.alloUsers(sysTenantUserDto3);
        assert resp3.getSuccess();
    }

}
