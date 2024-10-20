package com.iflytek.auth.manager.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.regex.Pattern;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class IpUtils {

    public final static Logger LOGGER = LoggerFactory.getLogger(IpUtils.class);

    private final static String UNKNOWN = "unknown";

    /**
     * 字符串切分点
     * {@link Pattern} for a comma delimited string that support whitespace characters
     */
    private static final Pattern commaSeparatedValuesPattern = Pattern.compile("\\s*,\\s*");

    private static final String proxiesHeader = "X-Forwarded-By";

    private static final String remoteIpHeader = "X-Forwarded-For";

    /**
     * 协议头
     */
    private final String protocolHeader = null;

    /**
     * https协议头
     */
    private final String protocolHeaderHttpsValue = "https";

    /**
     * https默认端口
     */
    private static final int httpsServerPort = 443;

    /**
     * http协议默认端口
     */
    private static final int httpServerPort = 80;

    /**
     * 是否可设置request属性
     */
    private static final boolean requestAttributesEnabled = true;

    /**
     * 请求头端口
     */
    private static final String portHeader = null;

    /**
     * 是否变更本地端口
     */
    private static final boolean changeLocalPort = false;

    /**
     * IP判断正则(内网IP)
     */
    private static final Pattern internalProxies = Pattern.compile(
            "10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "192\\.168\\.\\d{1,3}\\.\\d{1,3}|" +
                    "169\\.254\\.\\d{1,3}\\.\\d{1,3}|" +
                    "127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                    "0:0:0:0:0:0:0:1|::1");

    /**
     * 可信代理
     */
    private static final Pattern trustedProxies = null;

    /**
     * 获取nginx代理中的真实IP
     */
    private static final String remoteAddr = "X-Real-IP";

    /**
     * 获取ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = getRemoteIp(request);
        if (!StringUtils.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                ip = ip.substring(0, index);
            }
        }
        //修复ip伪造问题
        if (ip != null) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (!ip.matches(regex)) {
                ip = null;
            }
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //Proxy-Client-IP：apache 服务代理
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            //X-Real-IP：nginx服务代理
            ip = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ip != null && ip.length() != 0) {
            ip = ip.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        //由于V3.0X之后的版本需要使用nginx代理，则request.getRemoteAddr()获取的是nginx地址
        /*if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }*/
        return ip;
    }

    public static boolean serverNameContainsPort(boolean containsScheme, String serverName) {
        if (!containsScheme && serverName.contains(":")) {
            return true;
        } else {
            int schemeIndex = serverName.indexOf(":");
            int portIndex = serverName.lastIndexOf(":");
            return schemeIndex != portIndex;
        }
    }

    public static String getRemoteIp(HttpServletRequest request) {
        /**
         * 定义最终返回ip
         */
        String remoteIp = null;
        /**
         * 获取客户端的IP地址(真实请求源)
         */
        final String originalRemoteAddr = request.getRemoteAddr();

        /**
         * 获得客户端的主机名(如经过多级代理，则返回最近)
         */
        final String originalRemoteHost = request.getRemoteHost();

        /**
         * 返回当前页面使用的协议
         */
        final String originalScheme = request.getScheme();

        /**
         * 是否使用安全协议（如HTTPS）
         */
        final boolean originalSecure = request.isSecure();
        /**
         * 返回当前页面所在的服务器使用的端口
         */
        final int originalServerPort = request.getServerPort();

        /**
         * 返回X-Forwarded-By
         */
        final String originalProxiesHeader = request.getHeader(proxiesHeader);

        /**
         * 返回X-Forwarded-For
         */
        final String originalRemoteIpHeader = request.getHeader(remoteIpHeader);

        /**
         * 返回X-Real-IP
         */
        final String originalRealIpHeader = request.getHeader(remoteAddr);

        /**
         * 判断RemoteAddr是否为内网地址
         */
        boolean isInternal = internalProxies != null &&
                internalProxies.matcher(originalRemoteAddr).matches();

        /**
         * 如RemoteAddr为内网地址，且可信代理配置不为空，且地址为可信代理，则执行操作
         */
        if (isInternal || (trustedProxies != null &&
                trustedProxies.matcher(originalRemoteAddr).matches())) {
            // In java 6, proxiesHeaderValue should be declared as a java.util.Deque
            /**
             * 定义代理链
             */
            LinkedList<String> proxiesHeaderValue = new LinkedList<>();
            StringBuilder concatRemoteIpHeaderValue = new StringBuilder();

            /**
             * 获取X-Forwarded-For（代理转发记录）并迭代,返回转发记录，并用","分隔
             */
            for (Enumeration<String> e = request.getHeaders(remoteIpHeader); e.hasMoreElements(); ) {
                if (concatRemoteIpHeaderValue.length() > 0) {
                    concatRemoteIpHeaderValue.append(", ");
                }

                concatRemoteIpHeaderValue.append(e.nextElement());
            }

            /**
             * 将转发记录转换为数组
             */
            String[] remoteIpHeaderValue = commaDelimitedListToStringArray(concatRemoteIpHeaderValue.toString());
            int idx;
            /**
             * 如直接请求地址不是内网地址，则将
             */
            if (!isInternal) {
                proxiesHeaderValue.addFirst(originalRemoteAddr);
            }
            // loop on remoteIpHeaderValue to find the first trusted remote ip and to build the proxies chain
            /**
             * 从后往前循环转发记录，寻找第一个可信的remoteIp  并且放置到代理链头部
             */
            for (idx = remoteIpHeaderValue.length - 1; idx >= 0; idx--) {
                /**
                 * 从后往前寻找
                 */
                String currentRemoteIp = remoteIpHeaderValue[idx];
                remoteIp = currentRemoteIp;
                /**
                 * 如内网ip正则不为空且判定为当前ip为内网则继续寻找下一个
                 */
                if (internalProxies != null && internalProxies.matcher(currentRemoteIp).matches()) {
                    // do nothing, internalProxies IPs are not appended to the
                } else if (trustedProxies != null &&
                        trustedProxies.matcher(currentRemoteIp).matches()) {
                    /**
                     * 如可信代理列表不为空，且当前ip为可信代理，则将当前ip添加到代理链
                     */
                    proxiesHeaderValue.addFirst(currentRemoteIp);
                } else {
                    /**
                     * 抛弃其他不可信代理ip
                     */
                    idx--; // decrement idx because break statement doesn't do it
                    break;
                }
            }
            if(originalRealIpHeader!=null && originalRealIpHeader!=""){
                remoteIp = originalRealIpHeader;
            }else if (proxiesHeaderValue.size() > 0) {
                remoteIp = proxiesHeaderValue.getFirst();
            } else {
                remoteIp = originalRemoteAddr;
            }
        }
        return remoteIp;
    }

    /**
     * 给定字符串，通过字符串切分点进行切分，返回字符串数组
     * Convert a given comma delimited String into an array of String
     *
     * @param commaDelimitedStrings The string to convert
     * @return array of String (non <code>null</code>)
     */
    protected static String[] commaDelimitedListToStringArray(String commaDelimitedStrings) {
        return (commaDelimitedStrings == null || commaDelimitedStrings.length() == 0) ? new String[0] : commaSeparatedValuesPattern
                .split(commaDelimitedStrings);
    }
}
