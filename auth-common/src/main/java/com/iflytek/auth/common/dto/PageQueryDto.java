package com.iflytek.auth.common.dto;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class PageQueryDto {

    private Integer pageNum = 1;

    private Integer pageSize = 10;
}
