package com.iflytek.auth.server.service;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
public interface EmailService {

    void sendEmail(String to, String message);
}
