package com.iflytek.auth.common.common.enums;

import com.iflytek.itsc.web.exception.BaseBizException;
import lombok.Getter;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Getter
public enum TargetType {

    DEPT(1, "部门"),
    USER(2, "用户"),
    ACL_MODULE(3, "权限模块"),
    ACL(4, "权限"),
    ROLE(5, "角色"),
    ROLE_USER(6, "角色用户关系"),
    ROLE_ACL(7, "角色权限关系"),
    ;

    private Integer type;

    private String desc;

    TargetType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getTargetType(Integer type) {
        for (TargetType value : values()) {
            if (value.getType().equals(type)) {
                return value.getDesc();
            }
        }

        throw new BaseBizException("不存在这样的类型: " + type);
    }
}
