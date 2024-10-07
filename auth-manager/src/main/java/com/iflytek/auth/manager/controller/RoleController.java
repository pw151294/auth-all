package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.common.utils.SessionUtils;
import com.iflytek.auth.common.dto.SysRoleAclDto;
import com.iflytek.auth.common.dto.SysRoleDto;
import com.iflytek.auth.common.dto.SysRoleUserDto;
import com.iflytek.auth.common.pojo.SysRole;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.common.vo.SysRoleAclModuleVo;
import com.iflytek.auth.manager.annotations.AclValidate;
import com.iflytek.auth.manager.service.IRoleService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @PostMapping("/aclTree/{roleId}")
    @AclValidate
    public RestResponse<List<SysRoleAclModuleVo>> roleAclTree(@PathVariable Integer roleId) {
        //获取当前登录用户信息
        SysUser sysUser = SessionUtils.getUser();
        if (sysUser == null) {
            return RestResponse.buildError("当前登录用户信息为空！");
        }
        return roleService.roleAclTree(roleId, sysUser.getId());
    }

    @PostMapping("/add")
    public RestResponse addRole(@RequestBody SysRoleDto sysRoleDto) {
        return roleService.addRole(sysRoleDto);
    }

    @PostMapping("/update")
    public RestResponse updateRole(@RequestBody SysRoleDto sysRoleDto) {
        return roleService.updateRole(sysRoleDto);
    }

    @PostMapping("/delete/{roleId}")
    public RestResponse deleteRole(@PathVariable Integer roleId) {
        return roleService.deleteRole(roleId);
    }

    @GetMapping("/list")
    public RestResponse<List<SysRole>> listRoles() {
        return roleService.list();
    }

    @PostMapping("/user/saveOrUpdate")
    public RestResponse saveOrUpdateRoleUser(@RequestBody SysRoleUserDto sysRoleUserDto) {
        return roleService.saveOrUpdateRoleUser(sysRoleUserDto);
    }

    @PostMapping("/acl/saveOrUpdate")
    public RestResponse saveOrUpdateRoleAcl(@RequestBody SysRoleAclDto sysRoleAclDto) {
        return roleService.saveOrUpdateRoleAcl(sysRoleAclDto);
    }
}
