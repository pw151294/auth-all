package com.iflytek.auth.manager.service.impl;

import com.google.common.base.Preconditions;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.common.utils.PoCommonUtils;
import com.iflytek.auth.common.dao.SysTenantMapper;
import com.iflytek.auth.common.dao.SysTenantUserMapper;
import com.iflytek.auth.common.dto.SysTenantDto;
import com.iflytek.auth.common.dto.SysTenantUserDto;
import com.iflytek.auth.common.pojo.SysTenant;
import com.iflytek.auth.common.pojo.SysTenantUser;
import com.iflytek.auth.manager.service.ITenantService;
import com.iflytek.itsc.web.response.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class TenantServiceImpl implements ITenantService {

    @Autowired
    private SysTenantMapper tenantMapper;

    @Autowired
    private SysTenantUserMapper tenantUserMapper;

    @Override
    public RestResponse add(SysTenantDto sysTenantDto) {
        Preconditions.checkNotNull(sysTenantDto, "请求参数不能为空");
        Validator.validateTenantAdd(sysTenantDto);
        SysTenant sysTenant = new SysTenant();
        sysTenant.setName(sysTenantDto.getName());
        sysTenant.setCode(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        if (StringUtils.isBlank(sysTenant.getRemark())) {
            sysTenant.setRemark(sysTenantDto.getRemark());
        }
        sysTenant.setStatus(sysTenantDto.getStatus() == null ? 1 : sysTenantDto.getStatus());
        PoCommonUtils.setOperationInfo(sysTenant);
        tenantMapper.insert(sysTenant);

        return RestResponse.buildSuccess("创建租户成功");
    }

    @Override
    public RestResponse update(SysTenantDto sysTenantDto) {
        Preconditions.checkNotNull(sysTenantDto, "请求参数不能为空");
        Validator.validateTenantUpdate(sysTenantDto);
        SysTenant sysTenant = tenantMapper.selectById(sysTenantDto.getId());
        Preconditions.checkNotNull(sysTenant, "待更新的租户不存在");
        PoCommonUtils.copyTenantProperties(sysTenantDto, sysTenant);
        PoCommonUtils.setOperationInfo(sysTenant);
        tenantMapper.updateById(sysTenant);

        return RestResponse.buildSuccess("更新租户信息成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse delete(Integer tenantId) {
        Preconditions.checkNotNull(tenantId, "被删除的租户ID不能为空");
        tenantMapper.deleteById(tenantId);
        //删除关联表的信息
        tenantUserMapper.deleteByTenantId(tenantId);

        return RestResponse.buildSuccess("删除租户成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse alloUsers(SysTenantUserDto sysTenantUserDto) {
        Preconditions.checkNotNull(sysTenantUserDto, "请求参数不能为空");
        Preconditions.checkNotNull(sysTenantUserDto.getTenantId(), "租户ID不能为空");
        //先删除后新增
        tenantUserMapper.deleteByTenantId(sysTenantUserDto.getTenantId());
        if (CollectionUtils.isEmpty(sysTenantUserDto.getUserIds())) {
            return RestResponse.buildSuccess("已清除租户下的所有用户");
        }
        List<SysTenantUser> tenantUsers = sysTenantUserDto.getUserIds().stream().map(userId -> {
            SysTenantUser sysTenantUser = new SysTenantUser();
            sysTenantUser.setTenantId(sysTenantUserDto.getTenantId());
            sysTenantUser.setUserId(userId);
            PoCommonUtils.setOperationInfo(sysTenantUser);
            return sysTenantUser;
        }).collect(Collectors.toList());
        tenantUserMapper.insertAll(tenantUsers);

        return RestResponse.buildSuccess("租户用户关联关系更新成功！");
    }


}
