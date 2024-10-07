package com.iflytek.auth.common.common.enums;

import com.iflytek.itsc.web.enums.BaseErrorCodeEnum;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public enum AuthErrorCodeEnum implements BaseErrorCodeEnum {
    USER_NOT_FOUND("10001", "用户不存在"),
    DEPT_DUPLICATE_NAME_OR_SEQ("10002", "存在同名或者同序的同级部门"),
    ID_NOT_NULL("10003", "被操作对象的ID不能为空"),
    ;


    private String errorMsg;

    private String errorCode;

    AuthErrorCodeEnum(String errorMsg, String errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }
}
