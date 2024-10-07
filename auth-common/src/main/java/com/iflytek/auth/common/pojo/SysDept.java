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
 * @TableName sys_dept
 */
@TableName(value ="sys_dept")
@Data
public class SysDept implements Serializable {
    /**
     * 部门id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 部门名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 上级部门id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 部门层级
     */
    @TableField(value = "level")
    private String level;

    /**
     * 部门在当前层级下的顺序，由小到大
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
