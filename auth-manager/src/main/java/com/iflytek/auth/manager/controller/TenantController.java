package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.dto.SysTenantDto;
import com.iflytek.auth.common.dto.SysTenantUserDto;
import com.iflytek.auth.manager.service.ITenantService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private ITenantService tenantService;

    @PostMapping("/add")
    public RestResponse add(@RequestBody SysTenantDto sysTenantDto) {
        return tenantService.add(sysTenantDto);
    }

    @PostMapping("/update")
    public RestResponse update(@RequestBody SysTenantDto sysTenantDto) {
        return tenantService.update(sysTenantDto);
    }

    @PostMapping("/delete/{id}")
    public RestResponse delete(@PathVariable("id") Integer id) {
        return tenantService.delete(id);
    }

    @PostMapping("/alloUsers")
    public RestResponse alloUsers(@RequestBody SysTenantUserDto sysTenantUserDto) {
        return tenantService.alloUsers(sysTenantUserDto);
    }
}
