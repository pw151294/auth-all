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
 * @TableName sys_tenant_user
 */
@TableName(value ="sys_tenant_user")
@Data
public class SysTenantUser implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 操作者
     */
    @TableField(value = "operator")
    private String operator;

    /**
     * 最后一次更新的时间
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