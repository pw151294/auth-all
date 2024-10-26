package com.iflytek.auth.server.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.iflytek.auth.common.common.AuthConstant;
import com.iflytek.auth.common.common.Validator;
import com.iflytek.auth.common.dao.SysUserMapper;
import com.iflytek.auth.common.dto.LoginDto;
import com.iflytek.auth.common.dto.OAuth2LoginResultDto;
import com.iflytek.auth.common.pojo.SysUser;
import com.iflytek.auth.server.auth.Authentication;
import com.iflytek.auth.server.auth.AuthenticationToken;
import com.iflytek.auth.server.auth.UserDetails;
import com.iflytek.auth.server.oauth2.SysCookieGenerator;
import com.iflytek.auth.server.oauth2.SysTicketGrantingTicket;
import com.iflytek.auth.server.oauth2.SysTicketIdGenerator;
import com.iflytek.auth.server.service.EmailService;
import com.iflytek.auth.server.service.ILoginService;
import com.iflytek.auth.server.utils.JwtUtils;
import com.iflytek.auth.server.utils.TotpUtils;
import com.iflytek.itsc.web.exception.BaseBizException;
import com.iflytek.itsc.web.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SysUserInfoService userInfoService;

    @Resource
    private TotpUtils totpUtils;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SysCookieGenerator cookieGenerator;
    @Autowired
    private SysCookieGenerator sysCookieGenerator;

    @Override
    public RestResponse<AuthenticationToken> createToken(LoginDto loginDto) {
        //加载用户信息
        UserDetails userDetails = userInfoService.loadUserByUsername(loginDto.getUsername());
        //校验用户名和密码
        if (!StringUtils.equals(userDetails.getUsername(), loginDto.getUsername())
                || !StringUtils.equals(userDetails.getPassword(), DigestUtil.md5Hex(loginDto.getRawPassword()))) {
            return RestResponse.buildError("用户名或者密码错误");
        }
        //创建认证信息
        String accessToken = jwtUtils.createAccessToken(userDetails);
        String refreshToken = jwtUtils.createRefreshToken(userDetails);
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        authenticationToken.setUserDetails(userDetails);

        return RestResponse.buildSuccess(authenticationToken);
    }

    @Override
    public RestResponse<AuthenticationToken> refreshToken() {
        //从请求头里获取到刷新令牌
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String refreshToken = request.getHeader(AuthConstant.refreshTokenKey);
        if (!JwtUtils.validateRefreshToken(refreshToken)) {
            return RestResponse.buildError("刷新令牌不合法或者已过期");
        }
        //根据刷新令牌生成访问令牌
        String accessToken = jwtUtils.createAccessTokenByRefreshToken(refreshToken);
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        return RestResponse.buildSuccess(authenticationToken);
    }

    @Override
    public ResponseEntity multiFactorAuthentication(LoginDto loginDto) {
        UserDetails userDetails = userInfoService.loadUserByUsername(loginDto.getUsername());
        if (!StringUtils.equals(userDetails.getUsername(), loginDto.getUsername())
                || !StringUtils.equals(userDetails.getPassword(), DigestUtil.md5Hex(loginDto.getRawPassword()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("用户名或密码错误！");
        }
        try {
            //生成验证码
            String reqId = TotpUtils.randomAlphanumeric(12);
            SysUser sysUser = sysUserMapper.findByUsername(userDetails.getUsername());
            Key key = totpUtils.decodeKeyFromString(sysUser.getRemark());
            String totp = totpUtils.createTotp(key, Instant.now());
            //存储验证码和用户信息
            RMapCache<Object, Object> rMapCache = redissonClient.getMapCache(reqId);
            rMapCache.put(totp, sysUser, totpUtils.getTimeStepInLong(), TimeUnit.SECONDS);
            //发送邮件
            if (!Validator.validateUserEmail(sysUser.getMail())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户邮件格式错误！");
            }
            emailService.sendEmail(sysUser.getMail(), String.format("验证码为：%s", totp));
            //请求标识reqId设置在响应头供前端获取
            return ResponseEntity.status(HttpStatus.OK)
                    .header("reqId", reqId)
                    .body("验证码已经发送！");
        } catch (InvalidKeyException e) {
            log.error("save user info failed:{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系统异常！");
        }
    }

    @Override
    public RestResponse<AuthenticationToken> verify(String verifyCode) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String reqId = request.getHeader("reqId");

        //查询出totp
        Boolean verified = Optional.ofNullable(redissonClient.getMapCache(reqId))
                .map(rMapCache -> rMapCache.containsKey(verifyCode))
                .orElse(false);
        if (!verified) {
            return RestResponse.buildError("验证码错误！");
        }
        //验证通过 开始生成访问令牌和刷新令牌
        SysUser sysUser = (SysUser) redissonClient.getMapCache(reqId).get(verifyCode);
        UserDetails userDetails = userInfoService.loadUserByUsername(sysUser.getUsername());
        AuthenticationToken authenticationToken = new AuthenticationToken(
                jwtUtils.createAccessToken(userDetails), jwtUtils.createRefreshToken(userDetails)
        );
        authenticationToken.setUserDetails(userDetails);
        authenticationToken.setAuthenticated(false);

        return RestResponse.buildSuccess(authenticationToken);
    }

    @Override
    public RestResponse<OAuth2LoginResultDto> oAuth2Login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        //验证用户信息
        UserDetails userDetails = Optional.ofNullable(userInfoService.loadUserByUsername(loginDto.getUsername()))
                .orElseThrow(() -> new BaseBizException(String.format("用户%s不存在！", loginDto.getUsername())));
        if (!StringUtils.equals(userDetails.getUsername(), loginDto.getUsername())
                || !StringUtils.equals(userDetails.getPassword(), DigestUtil.md5Hex(loginDto.getRawPassword()))) {
            return RestResponse.buildError("用户名或者密码错误！");
        }
        //生成TGT对象
        String accessToken = jwtUtils.createAccessToken(userDetails);
        String refreshToken = jwtUtils.createRefreshToken(userDetails);
        AuthenticationToken authenticationToken = new AuthenticationToken(accessToken, refreshToken);
        authenticationToken.setUserDetails(userDetails);
        authenticationToken.setAuthenticated(false);
        SysTicketGrantingTicket tgt = new SysTicketGrantingTicket(authenticationToken, 10 * 60 * 1000L,
                SysTicketIdGenerator.getNewTicketId("TGT"));
        //将TGT对象设置到缓存里
        RMapCache rMapCache = redissonClient.getMapCache("TGT");
        rMapCache.put(tgt.getId(), tgt, 60 * 60 * 1000L, TimeUnit.MILLISECONDS);
        //创建TGC
        cookieGenerator.setCookieName("TGC");
        cookieGenerator.addCookie(response, tgt.getId());
        //创建响应对象
        OAuth2LoginResultDto resultDto = new OAuth2LoginResultDto();
        resultDto.setTicket(tgt.getId());
        resultDto.setState(loginDto.getState());
        return RestResponse.buildSuccess(resultDto);
    }

    @Override
    public RestResponse<Authentication> oAuth2Token(HttpServletRequest request) {
        //判断是否有TGC
        String ticket = Arrays.stream(request.getCookies())
                .filter(cookie -> StringUtils.equals(cookie.getName(), "TGC"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new BaseBizException("TGC不存在！"));
        RMapCache<Object, Object> rMapCache = redissonClient.getMapCache("TGT");
        if (rMapCache == null) {
            throw new BaseBizException("TGT不存在！");
        }
        SysTicketGrantingTicket tgt = Optional.of(rMapCache.get(ticket))
                .map(SysTicketGrantingTicket.class::cast)
                .orElseThrow(() -> new BaseBizException("票据不存在！"));
        //判断票据是否超时
        if (tgt.isExpired()) {
            throw new BaseBizException("票据已超时！");
        }
        return RestResponse.buildSuccess(tgt.getAuthentication());
    }
}
