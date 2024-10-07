package com.iflytek.auth.common.dto;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysRoleDto {

    /**
     * 角色id
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色的类型，1：管理员角色，2：其他
     */
    private Integer type;

    /**
     * 状态，1：可用，0：冻结
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
