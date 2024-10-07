package com.iflytek.auth.manager.service;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.vo.SysRoleAclModuleVo;
import com.iflytek.auth.manager.AuthManagerApplicationTest;
import com.iflytek.itsc.web.response.RestResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@SpringBootTest(classes = AuthManagerApplicationTest.class)
public class RoleServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceTest.class);

    private static final Integer userId = 1;
    private static final Integer roleId = 4;

    @Autowired
    private IRoleService roleService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void testRoleAclTree() {
        //查询出当前登录用户所具备的权限
        RestResponse<List<SysRoleAclModuleVo>> response = roleService.roleAclTree(roleId, userId);
        assert response.getSuccess();
        List<SysRoleAclModuleVo> roleAclTree = response.getData();
        logger.info("角色权限树：{}", JSON.toJSONString(roleAclTree));
    }
}
