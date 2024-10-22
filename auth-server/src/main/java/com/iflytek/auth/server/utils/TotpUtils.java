package com.iflytek.auth.server.utils;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

/**
 * @author weipan4
 * weipan4@iflytek.com Exp $
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TotpUtils {

    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_LENGTH = 6;
    private KeyGenerator keyGenerator;
    private TimeBasedOneTimePasswordGenerator totp;

    /*
     * 初始化代码块，Java 8 开始支持。这种初始化代码块的执行在构造函数之前
     * 准确说应该是 Java 编译器会把代码块拷贝到构造函数的最开始。
     */ {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            // SHA-1 and SHA-256 需要 64 字节 (512 位) 的 key; SHA512 需要 128 字节 (1024 位) 的 key
            keyGenerator.init(512);
        } catch (NoSuchAlgorithmException e) {
            log.error("没有找到算法 {}", e.getLocalizedMessage());
        }
    }

    /**
     * @param time 用于生成 TOTP 的时间
     * @return 一次性验证码
     * @throws InvalidKeyException 非法 Key 抛出异常
     */
    public String createTotp(final Key key, final Instant time) throws InvalidKeyException {
        val format = "%0" + PASSWORD_LENGTH + "d";
        return String.format(format, totp.generateOneTimePassword(key, time));
    }

    public Optional<String> createTotp(final String strKey) {
        try {
            return Optional.of(createTotp(decodeKeyFromString(strKey), Instant.now()));
        } catch (InvalidKeyException e) {
            return Optional.empty();
        }
    }

    /**
     * 验证 TOTP
     *
     * @param code 要验证的 TOTP
     * @return 是否一致
     * @throws InvalidKeyException 非法 Key 抛出异常
     */
    public boolean validateTotp(final Key key, final String code) throws InvalidKeyException {
        val now = Instant.now();
        return createTotp(key, now).equals(code);
    }

    public Key generateKey() {
        return keyGenerator.generateKey();
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    public Key decodeKeyFromString(String strKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(strKey), totp.getAlgorithm());
    }

    public long getTimeStepInLong() {
        return TIME_STEP;
    }

    public Duration getTimeStep() {
        return totp.getTimeStep();
    }

    public static String randomAlphanumeric(int targetStringLength) {
        int leftLimit = 48; // 数字 '0'
        int rightLimit = 122; // 字母 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // 过滤掉 Unicode 65 和 90 之间的字符
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
