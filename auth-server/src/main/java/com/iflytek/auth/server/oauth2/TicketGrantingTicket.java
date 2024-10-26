package com.iflytek.auth.server.oauth2;

import com.iflytek.auth.server.auth.Authentication;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface TicketGrantingTicket {

    /** The prefix to use when generating an id for a TicketGrantingTicket. */
    String PREFIX = "TGT";

    String getId();

    boolean isExpired();

    long getCreationTime();

    /**
     * 获取用户认证信息
     * @return the authentication
     */
    Authentication getAuthentication();
}
