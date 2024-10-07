package com.iflytek.auth.common.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysRoleAclModuleVo {

    /**
     * 权限模块ID
     */
    private Integer id;

    /**
     * 权限模块名称
     */
    private String name;

    /**
     * 父权限模块ID
     */
    private Integer parentId;

    /**
     * 权限模块下的权限点列表
     */
    private List<SysRoleAclVo> aclVos = Lists.newArrayList();


    /**
     * 子权限模块
     */
    private List<SysRoleAclModuleVo> childs = Lists.newArrayList();
}
