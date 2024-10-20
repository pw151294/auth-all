package com.iflytek.auth.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iflytek.auth.common.dto.SysAclDto;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.vo.SysAclVo;
import com.iflytek.auth.common.vo.SysUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author panwei
* @description 针对表【sys_acl】的数据库操作Mapper
* @createDate 2024-10-03 14:54:03
* @Entity com.iflytek.shiro.manager.pojo.SysAcl
*/
@Mapper
public interface SysAclMapper extends BaseMapper<SysAcl> {

    int findByNameAndSeq(@Param("sysAclDto") SysAclDto sysAclDto);

    List<SysAcl> findAclsByUserId(@Param("userId") Integer userId);

    List<SysAcl> findAclByUserName(@Param("username") String username);

    List<SysAclVo> pageAcls(@Param("sysAclDto") SysAclDto sysAclDto);

    List<SysUserVo> findUsersByAclId(@Param("aclId") Integer aclId);

    List<String> findRolesByAclId(@Param("aclId") Integer aclId);
}
