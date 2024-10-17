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
 * @TableName sys_op_log
 */
@TableName(value ="sys_op_log")
@Data
public class SysOpLog implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作类型，0：查询，1：新增，2：修改，3：删除
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 接口名称
     */
    @TableField(value = "interface_name")
    private String interfaceName;

    /**
     * 接口参数
     */
    @TableField(value = "param")
    private String param;

    /**
     * 接口返回值
     */
    @TableField(value = "return_value")
    private String returnValue;

    /**
     * 接口抛出异常
     */
    @TableField(value = "exception")
    private String exception;

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