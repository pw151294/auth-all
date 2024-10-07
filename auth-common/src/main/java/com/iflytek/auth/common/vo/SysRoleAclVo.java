package com.iflytek.auth.common.vo;

import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 * 角色权限树 权限模块对应的权限节点
 */
@Data
public class SysRoleAclVo {

    /**
     * 权限ID
     */
    private Integer aclId;


    /**
     * 所属权限模块ID
     */
    private Integer aclModuleId;

    /**
     * 权限名称
     */
    private String aclName;


    /**
     * 是否和当前角色绑定
     */
    private boolean hasRole;
}
