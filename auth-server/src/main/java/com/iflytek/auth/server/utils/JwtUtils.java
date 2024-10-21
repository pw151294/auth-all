package com.iflytek.auth.server.utils;

import com.alibaba.fastjson.JSON;
import com.iflytek.auth.common.pojo.SysAcl;
import com.iflytek.auth.server.auth.UserDetails;
import com.iflytek.auth.server.config.AppProperties;
import com.iflytek.itsc.web.exception.BaseBizException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Component
@Slf4j
public class JwtUtils {

    public final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 用于签名 Access Token
    public final static Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // 用于签名 Refresh Token

    @Autowired
    private AppProperties appProperties;

    public String createAccessToken(UserDetails userDetails) {
        return createToken(key, appProperties.getAccessTokenId(), userDetails, appProperties.getAccessTokenExpireTime());
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createToken(refreshKey, appProperties.getRefreshTokenId(), userDetails, appProperties.getRefreshTokenExpireTime());
    }

    public String createAccessTokenByRefreshToken(String refreshToken) {
        return parseClaims(refreshKey, refreshToken)
                .map(claims -> Jwts.builder()
                        .setId(appProperties.getAccessTokenId())
                        .setClaims(claims)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + appProperties.getAccessTokenExpireTime()))
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact())
                .orElseThrow(() -> new BaseBizException("拒绝访问"));
    }

    public static boolean validateAccessToken(String accessToken) {
        return validateToken(key, accessToken, true);
    }

    public static boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshKey, refreshToken, true);
    }

    public static boolean validateAccessTokenIgnoreExpire(String accessToken) {
        return validateToken(key, accessToken, false);
    }

    public static boolean validateRefreshTokenIgnoreExpire(String refreshToken) {
        return validateToken(refreshKey, refreshToken, false);
    }

    public static Optional<Claims> parseAccessJwtClaims(String accessToken) {
        return parseClaims(key, accessToken);
    }

    public static Optional<Claims> parseRefreshJwtClaims(String refreshToken) {
        return parseClaims(refreshKey, refreshToken);
    }

    private static String createToken(Key key, String tokenId, UserDetails userDetails, long expireTime) {
        List<String> urls = userDetails.getAuthorities().stream().map(SysAcl::getUrl).collect(Collectors.toList());
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(userDetails.getUsername())
                .claim("authorities", JSON.toJSONString(urls))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    /**
     * 校验Token是否合法
     *
     * @param key
     * @param token
     * @param isExpireInValid 标记超时的合法性 默认为true 表示超时非法
     * @return
     */
    private static boolean validateToken(Key key, String token, boolean isExpireInValid) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            if (e instanceof ExpiredJwtException) {
                log.error("token超时！");
                return !isExpireInValid;
            }
            log.error("解析token异常：{}", e.getMessage());
            return false;
        }
    }

    private static Optional<Claims> parseClaims(Key key, String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
