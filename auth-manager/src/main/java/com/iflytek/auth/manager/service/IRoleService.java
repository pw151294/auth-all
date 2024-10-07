package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.dto.SysRoleAclDto;
import com.iflytek.auth.common.dto.SysRoleDto;
import com.iflytek.auth.common.dto.SysRoleUserDto;
import com.iflytek.auth.common.pojo.SysRole;
import com.iflytek.auth.common.vo.SysRoleAclModuleVo;
import com.iflytek.itsc.web.response.RestResponse;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IRoleService {

    RestResponse<List<SysRoleAclModuleVo>> roleAclTree(Integer roleId, Integer userId);

    RestResponse addRole(SysRoleDto sysRoleDto);

    RestResponse updateRole(SysRoleDto sysRoleDto);

    RestResponse deleteRole(Integer roleId);

    RestResponse<List<SysRole>> list();

    RestResponse saveOrUpdateRoleUser(SysRoleUserDto sysRoleUserDto);

    RestResponse saveOrUpdateRoleAcl(SysRoleAclDto sysRoleAclDto);
}

