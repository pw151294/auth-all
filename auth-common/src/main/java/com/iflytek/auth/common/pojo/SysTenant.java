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
 * @TableName sys_tenant
 */
@TableName(value ="sys_tenant")
@Data
public class SysTenant implements Serializable {
    /**
     * 租户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 状态，1：正常，0：冻结状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 租户编码
     */
    @TableField(value = "code")
    private String code;

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
     * 最后一次更新者的ip地址
     */
    @TableField(value = "operate_ip")
    private String operateIp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}