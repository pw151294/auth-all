package com.iflytek.auth.server.oauth2;

import java.util.UUID;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public class SysTicketIdGenerator {
    public static String getNewTicketId(String prefix) {
        return String.format("%s_%s", prefix,
                UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }
}
