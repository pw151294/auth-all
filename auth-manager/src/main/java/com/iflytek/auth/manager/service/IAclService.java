package com.iflytek.auth.manager.service;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysAclDto;
import com.iflytek.auth.common.vo.SysAclVo;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.itsc.web.response.RestResponse;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAclService {

    RestResponse<PageInfo<SysAclVo>> pageAcls(SysAclDto sysAclDto);

    RestResponse addAcl(SysAclDto sysAclDto);

    RestResponse updateAcl(SysAclDto sysAclDto);

    RestResponse deleteAcl(Integer aclId);

    /**
     * 查询权限点对应的所有用户
     * @param aclId
     * @return
     */
    RestResponse<List<SysUserVo>> relationUsers(Integer aclId);

    /**
     * 查询权限点对应的所有角色
     * @param aclId
     * @return
     */
    RestResponse<List<String>> relationRoles(Integer aclId);
}
