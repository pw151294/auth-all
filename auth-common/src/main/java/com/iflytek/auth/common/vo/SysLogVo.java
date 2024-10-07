package com.iflytek.auth.common.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysLogVo {

    /**
     * 日志ID
     */
    private String id;

    /**
     * 权限更新的类型，1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系
     */
    private Integer type;

    /**
     * 基于type后指定的对象名称，比如用户、权限、角色的名称
     */
    private String targetName;

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
     * 操作时间
     */
    private Date operateTime;
}
