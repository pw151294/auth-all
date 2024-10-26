package com.iflytek.auth.server.oauth2;

import com.iflytek.auth.server.auth.Authentication;
import com.iflytek.auth.server.auth.AuthenticationToken;

import java.io.Serializable;
import java.util.Optional;


public class SysTicketGrantingTicket implements TicketGrantingTicket, Serializable {

    private static final long serialVersionUID = 1L;

    private String ticketId;

    private AuthenticationToken authenticationToken;

    /**
     * 最大生命周期 默认10分钟 单位毫秒
     */
    private long ttl;

    private long createTime;

    public SysTicketGrantingTicket(AuthenticationToken authenticationToken, long ttl, String ticketId) {
        this.authenticationToken = authenticationToken;
        this.ttl = Optional.of(ttl).orElse(10 * 60 * 1000L);
        this.ticketId = ticketId;
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public String getId() {
        return this.ticketId;
    }

    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() - createTime > ttl;
    }

    @Override
    public long getCreationTime() {
        return this.createTime;
    }

    @Override
    public Authentication getAuthentication() {
        return this.authenticationToken;
    }
}
