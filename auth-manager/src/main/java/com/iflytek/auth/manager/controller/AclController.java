package com.iflytek.auth.manager.controller;

import com.github.pagehelper.PageInfo;
import com.iflytek.auth.common.dto.SysAclDto;
import com.iflytek.auth.common.vo.SysAclVo;
import com.iflytek.auth.common.vo.SysUserVo;
import com.iflytek.auth.manager.service.IAclService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/acl")
public class AclController {

    @Autowired
    private IAclService aclService;

    @PostMapping("/page")
    public RestResponse<PageInfo<SysAclVo>> pageAcls(@RequestBody SysAclDto sysAclDto) {
        return aclService.pageAcls(sysAclDto);
    }

    @PostMapping("/add")
    public RestResponse addAcl(@RequestBody SysAclDto sysAclDto) {
        return aclService.addAcl(sysAclDto);
    }

    @PostMapping("/update")
    public RestResponse updateAcl(@RequestBody SysAclDto sysAclDto) {
        return aclService.updateAcl(sysAclDto);
    }

    @PostMapping("/delete/{aclId}")
    public RestResponse deleteAcl(@PathVariable Integer aclId) {
        return aclService.deleteAcl(aclId);
    }

    @PostMapping("/relation/users/{aclId}")
    public RestResponse<List<SysUserVo>> relationusers(@PathVariable Integer aclId) {
        return aclService.relationUsers(aclId);
    }

    @PostMapping("/relation/roles/{aclId}")
    public RestResponse<List<String>> relationRoles(@PathVariable Integer aclId) {
        return aclService.relationRoles(aclId);
    }
}
