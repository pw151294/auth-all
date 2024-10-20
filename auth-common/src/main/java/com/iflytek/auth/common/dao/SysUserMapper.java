package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.dto.SysUserDto;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.common.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author panwei
 * @description 针对表【sys_user】的数据库操作Mapper
 * @createDate 2024-10-03 15:03:59
 * @Entity com.iflytek.shiro.manager.pojo.SysUser
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser findByUsername(@Param("username") String username);

    SysUser findByUsernameIgnoreStatus(@Param("username") String username);

    /**
     * 判断是否有姓名邮箱或者手机号相同的用户
     * @param sysUserDto
     * @return
     */
    int countByKeyWord(@Param("sysUserDto") SysUserDto sysUserDto);

    List<SysUserVo> pageUsers(@Param("sysUserDto") SysUserDto sysUserDto);
}
