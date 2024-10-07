package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.pojo.SysRoleUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_role_user】的数据库操作Mapper
* @createDate 2024-10-03 15:03:01
* @Entity com.iflytek.shiro.manager.pojo.SysRoleUser
*/
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

    void deleteByUserId(@Param("userId") Integer userId);

    void deleteByRoleId(@Param("roleId") Integer roleId);

    void insertBatch(@Param("sysRoleUsers") List<SysRoleUser> sysRoleUsers);

    List<Integer> findUserIdsByRoleId(@Param("roleId") Integer roleId);
}
