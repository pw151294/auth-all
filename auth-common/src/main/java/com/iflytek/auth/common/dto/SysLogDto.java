package com.iflytek.auth.common.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysLogDto extends PageQueryDto{

    /**
     * 权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
     */
    private Integer type;

    /**
     * 基于type后指定的对象id，比如用户、权限、角色等表的主键
     */
    private Integer targetId;

    /**
     * 旧值
     */
    private String oldValue;

    /**
     * 新值
     */
    private String newValue;

    /**
     * 当前是否复原过，0：没有，1：复原过
     */
    private Integer status;

    /**
     * 操作时间范围（start）
     */
    private Date operationStartTime;

    /**
     * 操作时间范围（end）
     */
    private Date operationEndTime;
}
