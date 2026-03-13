package com.notwork.notwork_backend.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expire;


    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }


    /**
     * 生成 token（携带 userId 和 role）
     */
    public String generateToken(Long userId, String role) {

        return Jwts.builder()
                .subject(userId.toString())
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + expire))
                .signWith(getKey())
                .compact();
    }

    /**
     * 解析 token
     */
    private Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取用户ID
     */
    public Long getUserId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    /**
     * 获取用户角色
     */
    public String getRole(String token) {
        return parseToken(token).get("role", String.class);
    }

    /**
     * 判断 token 是否过期
     */
    public boolean isExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 校验 token
     */
    public boolean validate(String token) {

        try {

            return !isExpired(token);

        } catch (Exception e) {

            return false;
        }
    }
}