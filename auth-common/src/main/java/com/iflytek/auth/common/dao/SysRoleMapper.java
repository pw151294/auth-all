package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.pojo.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_role】的数据库操作Mapper
* @createDate 2024-10-03 15:01:41
* @Entity com.iflytek.shiro.manager.pojo.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> findRolesByUserId(@Param("userId") Integer userId);

    List<SysRole> findRoleByUserName(@Param("username") String username);

    int countByName(@Param("name") String name);
}
