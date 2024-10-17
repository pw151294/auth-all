package com.iflytek.auth.manager.service;


import com.iflytek.auth.common.dto.SysAuditDto;
import com.iflytek.itsc.web.response.RestResponse;

import java.util.Map;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface IAuditService {

    RestResponse audit(SysAuditDto sysAuditDto);

    Boolean hasAudit(Integer targetId, Integer targetType, Integer operationType);

    Boolean hasSameRoleName(String roleName);

    Boolean hasSameUserInfo(String username, String mail, String telephone);

    boolean hasSameNameOrSeqDept(String deptName, Integer seq);

    boolean hasSameNameOrSeqAclModule(String aclModuleName, Integer seq);

    <T> boolean hasSameFieldValue(Class<T> clazz, Integer targetType, Map<String, String> fieldKvMap);
}
