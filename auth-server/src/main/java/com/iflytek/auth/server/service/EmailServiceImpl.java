package com.iflytek.auth.server.service;

import com.iflytek.auth.server.config.AppProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private AppProperties appProperties;

    @Override
    public void sendEmail(String to, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("慕课网实战Spring Security 登录验证码");
        mailMessage.setFrom(appProperties.getMailUsername());
        mailMessage.setText(String.format("验证码为： %s", message));
        mailSender.send(mailMessage);
    }
}
