package com.iflytek.auth.common.dto;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class OAuth2LoginResultDto {

    private String ticket;

    private String state;
}
