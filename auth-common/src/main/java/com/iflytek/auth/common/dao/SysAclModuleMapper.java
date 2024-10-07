package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.dto.SysAclModuleDto;
import com.iflytek.auth.common.pojo.SysAclModule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author panwei
 * @description 针对表【sys_acl_module】的数据库操作Mapper
 * @createDate 2024-10-03 14:57:17
 * @Entity com.iflytek.shiro.manager.pojo.SysAclModule
 */
@Mapper
public interface SysAclModuleMapper extends BaseMapper<SysAclModule> {

    List<SysAclModule> findAll();

    String findLevelById(@Param("id") Integer id);

    /**
     * 查询是否存在是否存在同名或者同序的权限模块
     *
     * @param sysAclModuleDto
     * @return
     */
    int countByNameAndSeq(@Param("sysAclModuleDto") SysAclModuleDto sysAclModuleDto);

    int countChilds(@Param("moduleId") Integer moduleId);

    int countAcls(@Param("moduleId") Integer moduleId);
}
