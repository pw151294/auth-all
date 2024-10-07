package com.iflytek.auth.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.dao.SysAclMapper;
import com.iflytek.auth.common.dao.SysRoleAclMapper;
import com.iflytek.auth.common.dto.SysAclDto;
import com.iflytek.auth.common.dto.SysLogDto;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.vo.SysAclVo;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.auth.common.common.enums.TargetType;
import com.iflytek.auth.manager.service.IAclService;
import com.iflytek.auth.manager.service.ILogService;
import com.iflytek.itsc.web.exception.BaseBizException;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class AclServiceImpl implements IAclService {

    @Autowired
    private SysAclMapper aclMapper;

    @Autowired
    private SysRoleAclMapper roleAclMapper;

    @Autowired
    private ILogService logService;

    @Override
    public RestResponse<PageInfo<SysAclVo>> pageAcls(SysAclDto sysAclDto) {
        Preconditions.checkNotNull(sysAclDto.getAclModuleId(), "权限所在权限模块ID不能为空");
        PageHelper.startPage(sysAclDto.getPageNum(), sysAclDto.getPageSize());
        List<SysAclVo> sysAclVos = aclMapper.pageAcls(sysAclDto);
        return RestResponse.buildSuccess(new PageInfo<>(sysAclVos));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addAcl(SysAclDto sysAclDto) {
        Validator.validateAclAdd(sysAclDto);
        //校验所属权限模块下是否有相同名称的权限
        if (aclMapper.findByNameAndSeq(sysAclDto) > 0) {
            throw new BaseBizException("存在同名或者同序的权限");
        }
        SysAcl sysAcl = new SysAcl();
        PoCommonUtils.copyAclProperties(sysAclDto, sysAcl);
        PoCommonUtils.setOperationInfo(sysAcl);
        aclMapper.insert(sysAcl);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ACL.getType());
        sysLogDto.setTargetId(sysAcl.getId());
        sysLogDto.setOldValue("");
        sysLogDto.setNewValue(JSON.toJSONString(sysAcl));
        logService.addLog(sysLogDto);

        return RestResponse.buildSuccess("新增权限成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse updateAcl(SysAclDto sysAclDto) {
        Validator.validateAclUpdate(sysAclDto);
        SysAcl sysAcl = aclMapper.selectById(sysAclDto.getId());
        Preconditions.checkNotNull(sysAcl, "被更新的权限不存在");
        //校验所属权限模块下是否有相同名称的权限
        if (aclMapper.findByNameAndSeq(sysAclDto) > 0) {
            throw new BaseBizException("存在同名或者同序的权限");
        }
        String oldValue = JSON.toJSONString(sysAcl);
        PoCommonUtils.copyAclProperties(sysAclDto, sysAcl);
        PoCommonUtils.setOperationInfo(sysAcl);
        String newValue = JSON.toJSONString(sysAcl);
        aclMapper.updateById(sysAcl);

        //记录权限更新日志
        SysLogDto sysLogDto = new SysLogDto();
        sysLogDto.setType(TargetType.ACL.getType());
        sysLogDto.setTargetId(sysAcl.getId());
        sysLogDto.setOldValue(oldValue);
        sysLogDto.setNewValue(newValue);
        logService.addLog(sysLogDto);

        aclMapper.updateById(sysAcl);

        return RestResponse.buildSuccess("更新权限成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteAcl(Integer aclId) {
        Preconditions.checkNotNull(aclId, "被删除的权限ID为空");
        roleAclMapper.deleteByAclId(aclId);
        aclMapper.deleteById(aclId);

        return RestResponse.buildSuccess("删除权限成功");
    }

    @Override
    public RestResponse<List<SysUserVo>> relationUsers(Integer aclId) {
        Preconditions.checkNotNull(aclId, "权限ID不能为空");
        List<SysUserVo> sysUserVos = aclMapper.findUsersByAclId(aclId);
        return RestResponse.buildSuccess(sysUserVos);
    }

    @Override
    public RestResponse<List<String>> relationRoles(Integer aclId) {
        Preconditions.checkNotNull(aclId, "角色ID不能为空");
        List<String> roles = aclMapper.findRolesByAclId(aclId);
        return RestResponse.buildSuccess(roles);
    }

}
