package com.iflytek.auth.manager.aspect;

import com.iflytek.auth.common.common.utils.SessionUtils;
import com.iflytek.auth.common.common.utils.UrlUtils;
import com.iflytek.auth.common.dao.SysAclMapper;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.itsc.web.exception.BaseBizException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Component
@Aspect
public class ValidateAclAspect {

    private static final Logger logger = LoggerFactory.getLogger(ValidateAclAspect.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private SysAclMapper aclMapper;

    /**
     * 定义切点 切点作用的范围：
     * com.iflytek.auth.manager.controller包下的类和方法
     * 类必须有@RequestMapping注解
     * 方法必须有@PostMapping注解和@AclValidate注解
     */
    @Pointcut("within(com.iflytek.auth.manager.controller..*) " +
            "&& @within(org.springframework.web.bind.annotation.RequestMapping) " +
            "&& @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "&& @annotation(com.iflytek.auth.manager.annotations.AclValidate)")
    public void pointCut() {
    }

    // 2.定义切面方法
    @Before("pointCut()")
    public void validateAcl(JoinPoint joinPoint) {
        //获取被拦截方法及其PostMapping注解的值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        String value = postMapping.value()[0];
        //获取方法所在的控制器类 及其RequestMapping注解的值
        Class<?> controllerClass = signature.getDeclaringType();
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        String prefix = requestMapping.value()[0];
        String requestPath = contextPath + prefix + UrlUtils.removePathVariables(value);//完整请求路径

        //查询当前用户所具有的权限
        SysUser sysUser = SessionUtils.getUser();
        List<String> urls = aclMapper.findAclsByUserId(sysUser.getId())
                .stream().map(SysAcl::getUrl)
                .collect(Collectors.toList());
        if (urls.stream().noneMatch(url -> UrlUtils.hasAcl(url, requestPath))) {
            logger.error("用户权限校验未通过，用户名:{}，权限url:{}", sysUser.getUsername(), requestPath);
            throw new BaseBizException("权限校验未通过");
        }
    }

}
