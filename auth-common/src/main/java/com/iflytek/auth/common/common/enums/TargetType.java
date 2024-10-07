package com.iflytek.auth.common.common.enums;

import com.iflytek.itsc.web.exception.BaseBizException;
import lombok.Getter;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Getter
public enum TargetType {

    DEPT(1, "部门操作"),
    USER(2, "用户操作"),
    ACL_MODULE(3, "权限模块操作"),
    ACL(4, "权限操作"),
    ROLE(5, "角色操作"),
    ROLE_USER(6, "角色用户关系操作"),
    ROLE_ACL(7, "角色权限关系操作"),
    ;

    private Integer type;

    private String desc;

    TargetType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static TargetType getOperationType(Integer type) {
        for (TargetType targetType : values()) {
            if (targetType.getType().equals(type)) {
                return targetType;
            }
        }

        throw new BaseBizException("不存在这样的操作类型: " + type);
    }
}
