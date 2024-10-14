package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.dto.SysTenantDto;
import com.iflytek.auth.common.dto.SysTenantUserDto;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface ITenantService {

    RestResponse add(SysTenantDto sysTenantDto);

    RestResponse update(SysTenantDto sysTenantDto);

    RestResponse delete(Integer tenantId);

    RestResponse alloUsers(SysTenantUserDto sysTenantUserDto);
}
