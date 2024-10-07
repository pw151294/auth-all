package com.iflytek.auth.common.dto;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysAclModuleDto {

    /**
     * 权限模块id
     */
    private Integer id;

    /**
     * 权限模块名称
     */
    private String name;

    /**
     * 上级权限模块id
     */
    private Integer parentId;

    /**
     * 权限模块在当前层级下的顺序，由小到大
     */
    private Integer seq;

    /**
     * 状态，1：正常，0：冻结
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
