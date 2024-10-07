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
 * @TableName sys_audit
 */
@TableName(value ="sys_audit")
@Data
public class SysAudit implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 目标类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
     */
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 基于target_type后指定的对象id，比如用户、权限、角色等表的主键
     */
    @TableField(value = "target_id")
    private Integer targetId;

    /**
     * 操作类型，1：新增，2：修改，3：删除
     */
    @TableField(value = "operation_type")
    private Integer operationType;

    /**
     * 旧值
     */
    @TableField(value = "old_value")
    private String oldValue;

    /**
     * 新值
     */
    @TableField(value = "new_value")
    private String newValue;

    /**
     * 操作详情
     */
    @TableField(value = "detail")
    private String detail;

    /**
     * 提交操作者
     */
    @TableField(value = "submitter")
    private String submitter;

    /**
     * 最后一次提交操作的时间
     */
    @TableField(value = "submit_time")
    private Date submitTime;

    /**
     * 最后一次提交操作者的ip地址
     */
    @TableField(value = "submitter_ip")
    private String submitterIp;

    /**
     * 审核人
     */
    @TableField(value = "auditor")
    private String auditor;

    /**
     * 审核时间
     */
    @TableField(value = "audit_time")
    private Date auditTime;

    /**
     * 审核人ip地址
     */
    @TableField(value = "auditor_ip")
    private String auditorIp;

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
     * 审核状态，0：未审核，1：已审核
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 审核结果，0：审核通过，1：审核未通过
     */
    @TableField(value = "result")
    private Integer result;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}