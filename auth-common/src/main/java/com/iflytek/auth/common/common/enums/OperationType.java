package com.iflytek.auth.common.common.enums;

import com.iflytek.itsc.web.exception.BaseBizException;
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

    public static String getOperation(Integer type) {
        for (OperationType value : values()) {
            if (value.getType().equals(type)) {
                return value.getDesc();
            }
        }

        throw new BaseBizException(String.format("不存在这样的操作类型：%s", type));
    }
}
