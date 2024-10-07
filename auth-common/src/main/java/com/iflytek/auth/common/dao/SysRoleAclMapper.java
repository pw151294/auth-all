package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.pojo.SysRoleAcl;
import com.iflytek.auth.common.vo.SysRoleAclVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_role_acl】的数据库操作Mapper
* @createDate 2024-10-03 15:02:22
* @Entity com.iflytek.shiro.manager.pojo.SysRoleAcl
*/
@Mapper
public interface SysRoleAclMapper extends BaseMapper<SysRoleAcl> {

    void deleteByAclId(@Param("aclId") Integer aclId);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    List<SysRoleAclVo> findAllByRoleId(@Param("roleId") Integer roleId);

    void insertBatch(@Param("sysRoleAcls") List<SysRoleAcl> sysRoleAcls);

    List<Integer> findAclIdsByRoleId(@Param("roleId") Integer roleId);
}
