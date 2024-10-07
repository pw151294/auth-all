package com.iflytek.auth.common.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *
 * @TableName sys_acl
 */
@TableName(value ="sys_acl")
@Data
public class SysAcl implements Serializable {
    /**
     * 权限id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 权限名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 权限所在的权限模块id
     */
    @TableField(value = "acl_module_id")
    private Integer aclModuleId;

    /**
     * 请求的url, 可以填正则表达式
     */
    @TableField(value = "url")
    private String url;

    /**
     * 类型，1：菜单，2：按钮，3：其他
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 状态，1：正常，0：冻结
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 权限在当前模块下的顺序，由小到大
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 操作者
     */
    @TableField(value = "operator")
    private String operator;

    /**
     * 最后一次更新时间
     */
    @TableField(value = "operate_time")
    private Date operateTime;

    /**
     * 最后一个更新者的ip地址
     */
    @TableField(value = "operate_ip")
    private String operateIp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
