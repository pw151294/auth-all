package com.iflytek.auth.manager.controller;

import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.auth.manager.service.IAuditService;
import com.iflytek.itsc.web.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@RestController
@RequestMapping("/audit")
public class AuditController {

    @Autowired
    private IAuditService auditService;

    @PostMapping("/audit")
    public RestResponse audit(@RequestBody SysAuditDto sysAuditDto) {
        return auditService.audit(sysAuditDto);
    }
}
