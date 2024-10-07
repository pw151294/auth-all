package com.iflytek.auth.manager.service;

import com.iflytek.auth.common.dto.SysAclModuleDto;
import com.iflytek.auth.common.vo.SysAclModuleVo;
import com.iflytek.itsc.web.response.RestResponse;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAclModuleService {

    RestResponse<List<SysAclModuleVo>> aclModuleTree();

    RestResponse addAclModule(SysAclModuleDto sysAclModuleDto);

    RestResponse updateAclModule(SysAclModuleDto sysAclModuleDto);

    RestResponse deleteAclModule(Integer aclModuleId);
}
