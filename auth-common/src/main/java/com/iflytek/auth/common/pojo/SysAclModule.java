package com.iflytek.auth.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName sys_acl_module
 */
@TableName(value ="sys_acl_module")
@Data
public class SysAclModule implements Serializable {
    /**
     * 权限模块id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限模块名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 上级权限模块id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 权限模块层级
     */
    @TableField(value = "level")
    private String level;

    /**
     * 权限模块在当前层级下的顺序，由小到大
     */
    @TableField(value = "seq")
    private Integer seq;

    /**
     * 状态，1：正常，0：冻结
     */
    @TableField(value = "status")
    private Integer status;

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
     * 最后一次操作时间
     */
    @TableField(value = "operate_time")
    private Date operateTime;

    /**
     * 最后一次更新操作者的ip地址
     */
    @TableField(value = "operate_ip")
    private String operateIp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
