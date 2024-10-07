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
 * @TableName sys_log
 */
@TableName(value ="sys_log")
@Data
public class SysLog implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 基于type后指定的对象id，比如用户、权限、角色等表的主键
     */
    @TableField(value = "target_id")
    private Integer targetId;

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
     * 当前是否复原过，0：没有，1：复原过
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
