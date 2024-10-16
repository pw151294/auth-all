package com.iflytek.auth.manager.service;


import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.itsc.web.response.RestResponse;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAuditService {

    RestResponse audit(SysAuditDto sysAuditDto);

    Boolean hasAudit(Integer targetId, Integer targetType, Integer operationType);

    Boolean hasSameRoleName(String roleName);

    Boolean hasSameUserInfo(String username, String mail, String telephone);

    boolean hasSameNameOrSeq(String deptName, Integer seq);
}
