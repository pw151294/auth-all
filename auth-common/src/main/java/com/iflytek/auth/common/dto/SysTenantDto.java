package com.iflytek.auth.common.dto;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysTenantDto {

    private Integer id;

    private String name;

    private Integer status;

    private String remark;
}
