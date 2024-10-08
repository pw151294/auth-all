package com.iflytek.auth.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserDto extends PageQueryDto {

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
     * 状态，1：正常，0：冻结状态，2：删除
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关键词搜索
     */
    private String keyWord;
}
