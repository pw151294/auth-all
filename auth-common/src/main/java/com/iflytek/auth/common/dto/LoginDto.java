package com.iflytek.auth.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class LoginDto {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String rawPassword;

}
