package com.iflytek.auth.manager.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.common.enums.OperationType;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysRoleUserMapper;
import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.dto.SysUserDto;
import com.iflytek.auth.common.pojo.SysAudit;
import com.iflytek.auth.common.pojo.SysLog;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.auth.manager.annotations.OpLogger;
import com.iflytek.auth.manager.common.task.SysTask;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.auth.manager.service.IUserService;
import com.iflytek.itsc.web.exception.BaseBizException;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleUserMapper roleUserMapper;

    @Autowired
    private IAuditService auditService;

    @Autowired
    @Qualifier(value = "logTask")
    private SysTask logTask;

    @Autowired
    @Qualifier(value = "auditTask")
    private SysTask auditTask;

    @Override
    @OpLogger(opType = 0)
    public RestResponse<PageInfo<SysUserVo>> pageUsers(SysUserDto sysUserDto) {
        Preconditions.checkNotNull(sysUserDto.getDeptId(), "用户所在部门ID不能为空");
        PageHelper.startPage(sysUserDto.getPageNum(), sysUserDto.getPageSize());
        List<SysUserVo> sysUserVos = userMapper.pageUsers(sysUserDto);
        return RestResponse.buildSuccess(new PageInfo<>(sysUserVos));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addUser(SysUserDto sysUserDto) {
        Validator.validateUserAdd(sysUserDto);
        if (userMapper.countByKeyWord(sysUserDto) > 0) {
            throw new BaseBizException("用户名、邮箱或者联系电话不唯一");
        }
        SysUser sysUser = new SysUser();
        PoCommonUtils.copyUserProperties(sysUserDto, sysUser);
        PoCommonUtils.setOperationInfo(sysUser);
        userMapper.insert(sysUser);

        //记录权限更新日志
        SysLog sysLog = PoCommonUtils.buildSysLog(sysUser.getId(), "", JSON.toJSONString(sysUser), TargetType.USER.getType());
        logTask.offer(sysLog);

        return RestResponse.buildSuccess("新增用户成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateUser(SysUserDto sysUserDto) {
        Validator.validateUserUpdate(sysUserDto);
        if (userMapper.countByKeyWord(sysUserDto) > 0) {
            throw new BaseBizException("用户名、邮箱或者联系电话不唯一");
        }
        SysUser sysUser = userMapper.selectById(sysUserDto.getId());
        Preconditions.checkNotNull(sysUser, "用户不存在！");
        String oldValue = JSON.toJSONString(sysUser);
        PoCommonUtils.copyUserProperties(sysUserDto, sysUser);
        PoCommonUtils.setOperationInfo(sysUser);
        String newValue = JSON.toJSONString(sysUser);
        userMapper.updateById(sysUser);

        //记录权限更新日志
        SysLog sysLog = PoCommonUtils.buildSysLog(sysUser.getId(), oldValue, newValue, TargetType.USER.getType());
        logTask.offer(sysLog);

        return RestResponse.buildSuccess("更新用户信息成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteUser(Integer userId) {
        Preconditions.checkNotNull(userId, "被删除用户ID不能为空");
        SysUser sysUser = userMapper.selectById(userId);
        if (sysUser != null) {
            sysUser.setStatus(2);
            userMapper.updateById(sysUser);
        }
        //删除用户所在的角色关联关系
        roleUserMapper.deleteByUserId(userId);

        //记录权限更新日志
        SysLog sysLog = PoCommonUtils.buildSysLog(userId, "", "", TargetType.USER.getType());
        logTask.offer(sysLog);

        return RestResponse.buildSuccess("删除用户成功");
    }

    @Override
    public RestResponse login(SysUserDto sysUserDto) {
        Validator.validateUserLogin(sysUserDto);
        LambdaQueryWrapper<SysUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.eq(SysUser::getUsername, sysUserDto.getUsername());
        userQueryWrapper.eq(SysUser::getPassword, DigestUtil.md5Hex(sysUserDto.getPassword()));
        SysUser sysUser = userMapper.selectOne(userQueryWrapper);
        if (sysUser == null) {
            return RestResponse.buildError("用户名或者密码错误！");
        }

        return RestResponse.buildSuccess(sysUser);
    }

    @Override
    public RestResponse submitAddUser(SysUserDto sysUserDto) {
        Validator.validateUserAdd(sysUserDto);
        if (userMapper.countByKeyWord(sysUserDto) > 0) {
            return RestResponse.buildError("用户名、邮箱或者手机号不唯一");
        }
        if (auditService.hasSameUserInfo(sysUserDto.getUsername(), sysUserDto.getMail(), sysUserDto.getTelephone())) {
            return RestResponse.buildError("在待审核的新增用户计划里，用户名、邮箱或者手机号已经被占用");
        }

        //获取操作相关信息
        SysUser sysUser = new SysUser();
        PoCommonUtils.copyUserProperties(sysUserDto, sysUser);
        String newValue = JSON.toJSONString(sysUser);

        //创建审核记录
        SysLog sysLog = PoCommonUtils.buildSysLog(null, "", newValue, TargetType.USER.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.ADD.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("新增用户计划已经提交审核");
    }

    @Override
    public RestResponse submitUpdateUser(SysUserDto sysUserDto) {
        Validator.validateUserUpdate(sysUserDto);
        //校验是否有对该用户的修改或者删除计划待审核
        if (auditService.hasAudit(sysUserDto.getId(), TargetType.USER.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(sysUserDto.getId(), TargetType.USER.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在对该用户的更新或删除计划待审核");
        }
        SysUser sysUser = userMapper.selectById(sysUserDto.getId());
        Preconditions.checkNotNull(sysUser, "被操作的用户不能为空");
        if (userMapper.countByKeyWord(sysUserDto) > 0) {
            return RestResponse.buildError("用户名、邮箱或者手机号不唯一");
        }

        //获取操作相关信息
        String oldValue = JSON.toJSONString(sysUser);
        PoCommonUtils.copyUserProperties(sysUserDto, sysUser);
        String newValue = JSON.toJSONString(sysUser);

        //创建审核记录
        SysLog sysLog = PoCommonUtils.buildSysLog(sysUser.getId(), oldValue, newValue, TargetType.USER.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.UPDATE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("更新用户计划已经提交审核");
    }

    @Override
    public RestResponse submitDeleteUser(Integer userId) {
        Preconditions.checkNotNull(userId, "被删除的用户ID不能为空");
        ///校验是否有对该用户的修改或者删除计划待审核
        if (auditService.hasAudit(userId, TargetType.USER.getType(), OperationType.UPDATE.getType())
                || auditService.hasAudit(userId, TargetType.USER.getType(), OperationType.DELETE.getType())) {
            return RestResponse.buildError("存在对该用户的更新或删除计划待审核");
        }
        //创建审核记录/
        SysLog sysLog = PoCommonUtils.buildSysLog(userId, "", "", TargetType.USER.getType());
        SysAudit sysAudit = PoCommonUtils.buildSysAudit(sysLog, OperationType.DELETE.getType());
        auditTask.offer(sysAudit);

        return RestResponse.buildSuccess("删除用户计划已经提交审核");
    }
}
