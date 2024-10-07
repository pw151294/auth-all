package com.iflytek.auth.common.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysUserVo {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 用户所在部门的id
     */
    private Integer deptId;


    /**
     * 用户所在部门名称
     */
    private String deptName;

    /**
     * 状态，1：正常，0：冻结状态，2：删除
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 最后一次更新时间
     */
    private Date operateTime;

    /**
     * 最后一次更新者的ip地址
     */
    private String operateIp;

    /**
     * 用户对应的角色 多个用逗号分开
     */
    private String roleNames;

    /**
     * 用户具有的权限 多个用逗号分开
     */
    private String aclNames;
}
