package com.iflytek.auth.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysTenantUserDto {

    private Integer tenantId;

    private List<Integer> userIds;
}
