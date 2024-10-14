package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.pojo.SysTenantUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author panwei
 * @description 针对表【sys_tenant_user】的数据库操作Mapper
 * @createDate 2024-10-14 14:33:43
 * @Entity com.iflytek.auth.common.pojo.SysTenantUser
 */
public interface SysTenantUserMapper extends BaseMapper<SysTenantUser> {

    void deleteByTenantId(@Param("tenantId") Integer tenantId);

    void insertAll(@Param("tenantUsers") List<SysTenantUser> tenantUsers);
}




