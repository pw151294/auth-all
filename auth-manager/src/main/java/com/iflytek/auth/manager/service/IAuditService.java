package com.iflytek.auth.manager.service;


import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAuditService {

    RestResponse audit(SysAuditDto sysAuditDto);
}
