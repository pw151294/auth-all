package com.iflytek.auth.common.common.utils;

import java.util.regex.Pattern;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class UrlUtils {

    /**
     * 校验权限配置URL和请求URL是否匹配
     * @param url
     * @param requestPath
     * @return
     */
    public static boolean hasAcl(String url, String requestPath) {
        // 1. 精确匹配
        if (url.equals(requestPath)) {
            return true;
        }

        // 2. 正则匹配
        String regex = url.replace("*", ".*");
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(requestPath).matches();
    }

    /**
     * 对于路径中包含参数的请求 使用正则表达式去掉路径中的参数部分
     * @param path
     * @return
     */
    public static String removePathVariables(String path) {
        return path.replaceAll("/\\{[^/]*}", "");
    }
}
