package com.iflytek.auth.common.common;

import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.enums.AuthErrorCodeEnum;
import com.iflytek.auth.common.dto.*;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.itsc.web.exception.BaseBizException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class Validator {

    public static void userNotNull(SysUser user) {
        if (user == null) {
            throw new BaseBizException(AuthErrorCodeEnum.USER_NOT_FOUND);
        }
    }

    public static void validateUserAdd(SysUserDto sysUserDto) {
        Preconditions.checkNotNull(sysUserDto, "请求参数不能为空！");
        if (sysUserDto.getDeptId() == null) {
            throw new BaseBizException("用户所在部门ID不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getUsername())) {
            throw new BaseBizException("用户姓名不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getMail())) {
            throw new BaseBizException("用户邮箱不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getTelephone())) {
            throw new BaseBizException("用户电话不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getPassword())) {
            throw new BaseBizException("用户密码不能为空");
        }
    }

    public static void validateUserUpdate(SysUserDto sysUserDto) {
        validateUserAdd(sysUserDto);
        Preconditions.checkNotNull(sysUserDto.getId(), "被更新的用户ID不能为空");
    }

    public static void validateUserLogin(SysUserDto sysUserDto) {
        Preconditions.checkNotNull(sysUserDto, "请求参数不能为空！");
        if (StringUtils.isBlank(sysUserDto.getUsername())) {
            throw new BaseBizException("用户姓名不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getPassword())) {
            throw new BaseBizException("用户密码不能为空");
        }
    }

    public static void validateAclModuleAdd(SysAclModuleDto sysAclModuleDto) {
        Preconditions.checkNotNull(sysAclModuleDto, "请求参数不能为空！");
        if (sysAclModuleDto.getName() == null) {
            throw new BaseBizException("权限模块名称不能为空");
        }
        if (sysAclModuleDto.getParentId() == null) {
            throw new BaseBizException("权限模块的父模块ID不能为空");
        }
        if (sysAclModuleDto.getSeq() == null) {
            throw new BaseBizException("权限模块在当前层级下的顺序不能为空");
        }
    }

    public static void validateAclModuleUpdate(SysAclModuleDto sysAclModuleDto) {
        validateAclModuleAdd(sysAclModuleDto);
        Preconditions.checkNotNull(sysAclModuleDto, "被更新权限模块ID不能为空");
    }

    public static void validateAclAdd(SysAclDto sysAclDto) {
        Preconditions.checkNotNull(sysAclDto, "请求参数不能为空！");
        if (sysAclDto.getAclModuleId() == null) {
            throw new BaseBizException("所属权限ID不能为空");
        }
        if (StringUtils.isBlank(sysAclDto.getName())) {
            throw new BaseBizException("权限名称不能为空");
        }
        if (StringUtils.isBlank(sysAclDto.getCode())) {
            throw new BaseBizException("权限编码不能为空");
        }
        if (sysAclDto.getType() == null) {
            throw new BaseBizException("权限类型不能为空");
        }
        if (sysAclDto.getType() == 1 && StringUtils.isBlank(sysAclDto.getUrl())) {
            throw new BaseBizException("菜单类型的权限 URL不能为空");
        }
        if (sysAclDto.getSeq() == null) {
            throw new BaseBizException("权限在当前模块下的顺序不能为空");
        }
    }

    public static void validateAclUpdate(SysAclDto sysAclDto) {
        validateAclAdd(sysAclDto);
        Preconditions.checkNotNull(sysAclDto.getId(), "被更新的权限ID不能为空");
    }

    public static void validateRoleAdd(SysRoleDto sysRoleDto) {
        if (StringUtils.isBlank(sysRoleDto.getName())) {
            throw new BaseBizException("角色名称不能为空");
        }
        if (sysRoleDto.getType() == null) {
            throw new BaseBizException("角色类型不能为空");
        }
    }

    public static void validateRoleUpdate(SysRoleDto sysRoleDto) {
        validateRoleAdd(sysRoleDto);
        if (sysRoleDto.getId() == null) {
            throw new BaseBizException("被更新角色ID不能为空");
        }
    }

    public static void validateLogAdd(SysLogDto sysLogDto) {
        if (sysLogDto.getType() == null) {
            throw new BaseBizException("权限更新操作类型不能为空");
        }
        if (sysLogDto.getTargetId() == null) {
            throw new BaseBizException("基于type后指定的对象id不能为空");
        }
    }

    public static void validateTenantAdd(SysTenantDto sysTenantDto) {
        if (StringUtils.isBlank(sysTenantDto.getName())) {
            throw new BaseBizException("租户名称不能为空");
        }
    }

    public static void validateTenantUpdate(SysTenantDto sysTenantDto) {
        validateTenantAdd(sysTenantDto);
        if (sysTenantDto.getId() == null) {
            throw new BaseBizException("租户ID不能为空");
        }
    }

    public static boolean validateUserEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        // 正则表达式用于匹配邮箱格式
        String emailRegex = "^[a-zA-Z0-9_\\.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
