package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.utils.TreeUtils;
import com.iflytek.auth.common.dao.*;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.dto.SysRoleAclDto;
import com.iflytek.auth.common.dto.SysRoleDto;
import com.iflytek.auth.common.dto.SysRoleUserDto;
import com.iflytek.auth.common.pojo.*;
import com.iflytek.auth.common.vo.SysRoleAclModuleVo;
import com.iflytek.auth.common.vo.SysRoleAclVo;
import com.iflytek.auth.manager.task.SysTask;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.auth.manager.service.ILogService;
import com.iflytek.auth.manager.service.IRoleService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private SysRoleAclMapper roleAclMapper;

    @Autowired
    private SysAclMapper aclMapper;

    @Autowired
    private SysAclModuleMapper aclModuleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleUserMapper roleUserMapper;

    @Autowired
    private ILogService logService;


    @Autowired
    @Qualifier(value = "auditTask")
    private SysTask auditTask;

    @Autowired
    private IAuditService auditService;

    @Override
    public RestResponse<List<SysRoleAclModuleVo>> roleAclTree(Integer roleId, Integer userId) {
        Preconditions.checkNotNull(roleId, "角色ID不能为空");
        Preconditions.checkNotNull(userId, "用户ID不能为空");
        //查询出当前登录用户所具备的权限
        Set<Integer> aclIds = aclMapper.findAclsByUserId(userId)
                .stream().map(SysAcl::getId).collect(Collectors.toSet());
        //查询出当前角色ID对应的所有权限  并筛除掉不属于当前用户的权限 然后根据权限模块ID分组
        Map<Integer, List<SysRoleAclVo>> moduleIdAclMap = roleAclMapper.findAllByRoleId(roleId)
                .stream().filter(sysRoleAclVo -> aclIds.contains(sysRoleAclVo.getAclId()))
                .collect(Collectors.groupingBy(SysRoleAclVo::getAclModuleId));
        //查找所有的权限模块 并按照父权限模块ID来分组
        Map<Integer, List<SysAclModule>> idChildMap = aclModuleMapper.findAll()
                .stream().collect(Collectors.groupingBy(SysAclModule::getParentId));

        //获取根权限模块 并逐个构建树结构
        List<SysRoleAclModuleVo> roleAclTree = idChildMap.get(0).stream()
                .map(sysAclModule -> {
                    SysRoleAclModuleVo roleAclModuleVo = new SysRoleAclModuleVo();
                    roleAclModuleVo.setId(sysAclModule.getId());
                    roleAclModuleVo.setName(sysAclModule.getName());
                    roleAclModuleVo.setParentId(sysAclModule.getParentId());
                    return roleAclModuleVo;
                }).collect(Collectors.toList());
        roleAclTree.forEach(roleAclModuleVo -> TreeUtils.buildTree(roleAclModuleVo, idChildMap));

        //填充每个权限模块的权限点
        roleAclTree.forEach(roleAclModuleVo -> TreeUtils.fillAcls(roleAclModuleVo, moduleIdAclMap));

        return RestResponse.buildSuccess(roleAclTree);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addRole(SysRoleDto sysRoleDto) {
        Validator.validateRoleAdd(sysRoleDto);
        if (roleMapper.countByName(sysRoleDto.getName()) > 0) {
            return RestResponse.buildError("存在同名的角色");
        }
        SysRole sysRole = new SysRole();
        PoCommonUtils.copyRoleProperties(sysRoleDto, sysRole);
        PoCommonUtils.setOperationInfo(sysRole);
        roleMapper.insert(sysRole);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ROLE.getType());
        sysLogDto.setTargetId(sysRole.getId());
        sysLogDto.setOldValue("");
        sysLogDto.setNewValue(JSON.toJSONString(sysRole));
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("新增角色成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateRole(SysRoleDto sysRoleDto) {
        Validator.validateRoleUpdate(sysRoleDto);
        SysRole sysRole = roleMapper.selectById(sysRoleDto.getId());
        if (sysRole == null) {
            return RestResponse.buildError("被更新的角色不存在");
        }
        String oldValue = JSON.toJSONString(sysRole);
        PoCommonUtils.copyRoleProperties(sysRoleDto, sysRole);
        PoCommonUtils.setOperationInfo(sysRole);
        String newValue = JSON.toJSONString(sysRole);
        roleMapper.updateById(sysRole);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ROLE.getType());
        sysLogDto.setTargetId(sysRole.getId());
        sysLogDto.setOldValue(oldValue);
        sysLogDto.setNewValue(newValue);
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("更新角色成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteRole(Integer roleId) {
        Preconditions.checkNotNull(roleId, "被删除角色ID不能为空");
        //删除角色绑定的用户和权限
        roleUserMapper.deleteByRoleId(roleId);
        roleAclMapper.deleteByRoleId(roleId);
        roleMapper.deleteById(roleId);

        return RestResponse.buildSuccess("角色删除成功");
    }

    @Override
    public RestResponse<List<SysRole>> listAll() {
        List<SysRole> sysRoles = roleMapper.selectList(null);
        return RestResponse.buildSuccess(sysRoles);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse saveOrUpdateRoleUser(SysRoleUserDto sysRoleUserDto) {
        Integer roleId = sysRoleUserDto.getRoleId();
        Preconditions.checkNotNull(roleId, "待分配用户关系的角色ID不能为空");
        //先删除后新增
        roleUserMapper.deleteByRoleId(roleId);
        if (!CollectionUtils.isEmpty(sysRoleUserDto.getUserIds())) {
            List<SysRoleUser> roleUsers = sysRoleUserDto.getUserIds().stream()
                    .map(userId -> {
                        SysRoleUser sysRoleUser = new SysRoleUser();
                        sysRoleUser.setRoleId(roleId);
                        sysRoleUser.setUserId(userId);
                        PoCommonUtils.setOperationInfo(sysRoleUser);
                        return sysRoleUser;
                    }).collect(Collectors.toList());
            roleUserMapper.insertBatch(roleUsers);
        }

        return RestResponse.buildSuccess("配置用户角色关系成功");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse saveOrUpdateRoleAcl(SysRoleAclDto sysRoleAclDto) {
        Integer roleId = sysRoleAclDto.getRoleId();
        Preconditions.checkNotNull(roleId, "待分配权限关系的角色ID不能为空");
        //先删除后新增
        roleAclMapper.deleteByRoleId(roleId);
        if (!CollectionUtils.isEmpty(sysRoleAclDto.getAclIds())) {
            List<SysRoleAcl> sysRoleAcls = sysRoleAclDto.getAclIds().stream()
                    .map(aclId -> {
                        SysRoleAcl sysRoleAcl = new SysRoleAcl();
                        sysRoleAcl.setRoleId(roleId);
                        sysRoleAcl.setAclId(aclId);
                        PoCommonUtils.setOperationInfo(sysRoleAcl);
                        return sysRoleAcl;
                    }).collect(Collectors.toList());
            roleAclMapper.insertBatch(sysRoleAcls);
        }

        return RestResponse.buildSuccess("配置角色权限关系成功");
    }

    @Override
    public RestResponse submitAddRole(SysRoleDto sysRoleDto) {
        Validator.validateRoleAdd(sysRoleDto);
        if (roleMapper.countByName(sysRoleDto.getName()) > 0) {
            return RestResponse.buildError("存在同名的角色");
        }
        //TODO 校验待审核的角色新增计划里 是否有同名的角色
        if (auditService.hasSameRoleName(sysRoleDto.getName())) {
            return RestResponse.buildError("在待审核的新增角色计划里，相同的角色名称已经被占用了");
        }

        SysRole sysRole = new SysRole();
        PoCommonUtils.copyRoleProperties(sysRoleDto, sysRole);
        PoCommonUtils.setOperationInfo(sysRole);
        String newValue = JSON.toJSONString(sysRole);

        //创建审核记录
        SysLog sysLog = PoCommonUtils.buildSysLog(null, "", newValue, TargetType.ROLE.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.ADD.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("提交新增角色计划成功!");
    }

    @Override
    public RestResponse submitUpdateRole(SysRoleDto sysRoleDto) {
        Validator.validateRoleUpdate(sysRoleDto);
        //校验是否有对该角色的更新或者删除计划待审核
        if (auditService.hasAudit(sysRoleDto.getId(), TargetType.ROLE.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(sysRoleDto.getId(), TargetType.ROLE.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在待审核的更新或删除角色的计划！");
        }

        SysRole sysRole = roleMapper.selectById(sysRoleDto.getId());
        if (sysRole == null) {
            return RestResponse.buildError("被更新的角色不存在");
        }
        String oldValue = JSON.toJSONString(sysRole);
        PoCommonUtils.copyRoleProperties(sysRoleDto, sysRole);
        PoCommonUtils.setOperationInfo(sysRole);
        String newValue = JSON.toJSONString(sysRole);

        //创建审核记录
        SysLog sysLog = PoCommonUtils.buildSysLog(sysRoleDto.getId(), oldValue, newValue, TargetType.ROLE.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.UPDATE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("更新角色计划提交成功！");
    }

    @Override
    public RestResponse submitDeleteRole(Integer roleId) {
        Preconditions.checkNotNull(roleId, "被删除角色ID不能为空");
        //校验是否有对该角色的更新或者删除计划待审核
        if (auditService.hasAudit(roleId, TargetType.ROLE.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(roleId, TargetType.ROLE.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在待审核的更新或删除角色的计划！");
        }

        SysLog sysLog = PoCommonUtils.buildSysLog(roleId, "", "", TargetType.ROLE.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.DELETE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("删除角色计划提交成功！");
    }

    @Override
    public RestResponse submitSaveOrUpdateRoleUser(SysRoleUserDto sysRoleUserDto) {
        Integer roleId = sysRoleUserDto.getRoleId();
        //校验是否有对该角色所关联用户关系的修改计划待审核
        if (auditService.hasAudit(sysRoleUserDto.getRoleId(), TargetType.ROLE_USER.getType(), OperationType.UPDATE.getType())) {
            return RestResponse.buildError("存在对该角色所关联用户关系的修改计划待审核");
        }

        Preconditions.checkNotNull(roleId, "待分配用户关系的角色ID不能为空");
        String oldValue = JSON.toJSONString(roleUserMapper.findUserIdsByRoleId(roleId));
        String newValue = JSON.toJSONString(sysRoleUserDto.getUserIds());
        SysLog sysLog = PoCommonUtils.buildSysLog(roleId, oldValue, newValue, TargetType.ROLE_USER.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.UPDATE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("修改角色用户关系计划提交成功！");
    }

    @Override
    public RestResponse submitSaveOrUpdateRoleAcl(SysRoleAclDto sysRoleAclDto) {
        Integer roleId = sysRoleAclDto.getRoleId();
        //校验是否有对该角色所关联的权限关系的修改计划待审核
        if (auditService.hasAudit(sysRoleAclDto.getRoleId(), TargetType.ROLE_ACL.getType(), OperationType.UPDATE.getType())) {
            return RestResponse.buildError("存在对该角色所关联的权限关系的修改计划待审核");
        }

        Preconditions.checkNotNull(roleId, "待分配权限关系的角色ID不能为空");
        String oldValue = JSON.toJSONString(roleAclMapper.findAclIdsByRoleId(roleId));
        String newValue = JSON.toJSONString(sysRoleAclDto.getAclIds());
        SysLog sysLog = PoCommonUtils.buildSysLog(roleId, oldValue, newValue, TargetType.ROLE_ACL.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.UPDATE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("修改角色权限关系计划提交成功！");
    }
}
