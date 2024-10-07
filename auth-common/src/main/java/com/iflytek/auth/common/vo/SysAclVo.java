package com.iflytek.auth.common.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysAclVo {

    /**
     * 权限id
     */
    private Integer id;

    /**
     * 权限名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 权限所在的权限模块id
     */
    private Integer aclModuleId;


    /**
     * 权限所在权限模块名称
     */
    private String aclModuleName;

    /**
     * 请求的url, 可以填正则表达式
     */
    private String url;

    /**
     * 类型，1：菜单，2：按钮，3：其他
     */
    private Integer type;

    /**
     * 状态，1：正常，0：冻结
     */
    private Integer status;

    /**
     * 权限在当前模块下的顺序，由小到大
     */
    private Integer seq;

    /**
     * 备注
     */
    private String remark;
}
