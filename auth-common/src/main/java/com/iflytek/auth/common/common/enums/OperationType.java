package com.iflytek.auth.common.common.enums;

import lombok.Getter;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Getter
public enum OperationType {

    ADD(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除");

    private Integer type;
    private String desc;

    OperationType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
