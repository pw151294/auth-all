package com.iflytek.auth.common.vo;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysNodeVo {

    private Integer id;

    private Integer parentId;

    private String level;

    private Integer seq;
}
