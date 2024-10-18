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
 * @TableName sys_grant_log
 */
@TableName(value ="sys_grant_log")
@Data
public class SysGrantLog implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 授权对象id
     */
    @TableField(value = "target_id")
    private Integer targetId;

    /**
     * 授权类型：角色
     */
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 被授权内容类型：用户、权限
     */
    @TableField(value = "grant_type")
    private Integer grantType;

    /**
     * 被授权对象id列表
     */
    @TableField(value = "grant_ids")
    private String grantIds;

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

    /**
     * 授权结果 0失败 1成功
     */
    @TableField(value = "grant_result")
    private Integer grantResult;

    /**
     * 异常信息
     */
    @TableField(value = "exception")
    private String exception;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}