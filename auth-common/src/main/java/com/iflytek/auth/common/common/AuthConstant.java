package com.iflytek.auth.common.common;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class AuthConstant {

    public static final String exclusion_urls_key = "exclusionUrls";
    public static final String loginUrl = "http://172.31.186.189/login";

    public static final String grantLogTopic = "SYS_GRANT_LOG";
    public static final String grantLogGroupId = "sys_grant_log_group";

    public static final String opLogTopic = "SYS_OP_LOG";
    public static final String opLogGroupId = "sys_op_log_group";

    public static final String userAddMethodName = "addUser";
    public static final String userUpdateMethodName = "updateUser";
    public static final String userDeleteMethodName = "deleteUser";

    public static final String roleAddMethodName = "addRole";
    public static final String roleUpdateMethodName = "updateRole";
    public static final String roleDeleteMethodName = "deleteRole";

    public static final String deptAddMethodName = "addDept";
    public static final String deptUpdateMethodName = "updateDept";
    public static final String deptDeleteMethodName = "deleteDept";

    public static final String aclAddMethodName = "addAcl";
    public static final String aclUpdateMethodName = "updateAcl";
    public static final String aclDeleteMethodName = "deleteAcl";

    public static final String aclModuleAddMethodName = "addAclModule";
    public static final String aclModuleUpdateMethodName = "updateAclModule";
    public static final String aclModuleDeleteMethodName = "deleteAclModule";
}
