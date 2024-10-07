package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.dto.SysAclModuleDto;
import com.iflytek.auth.common.vo.SysAclModuleVo;
import com.iflytek.auth.manager.service.IAclModuleService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/acl/module")
public class AclModuleController {

    @Autowired
    private IAclModuleService aclModuleService;

    @PostMapping("/tree")
    public RestResponse<List<SysAclModuleVo>> aclModuleTree() {
        return aclModuleService.aclModuleTree();
    }

    @PostMapping("/add")
    public RestResponse addAclModule(@RequestBody SysAclModuleDto sysAclModuleDto) {
        return aclModuleService.addAclModule(sysAclModuleDto);
    }

    @PostMapping("/update")
    public RestResponse updateAclModule(@RequestBody SysAclModuleDto sysAclModuleDto) {
        return aclModuleService.updateAclModule(sysAclModuleDto);
    }

    @PostMapping("/delete/{aclModuleId}")
    public RestResponse deleteAclModule(@PathVariable Integer aclModuleId) {
        return aclModuleService.deleteAclModule(aclModuleId);
    }
}
