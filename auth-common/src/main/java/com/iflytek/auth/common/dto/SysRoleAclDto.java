package com.iflytek.auth.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Data
public class SysRoleAclDto {

    private Integer roleId;

    private List<Integer> aclIds;
}
