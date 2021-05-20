package com.allinfinance.dev.ccs.securityConfig.handler.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Mutz
 */
@Component
public class JwtUtil {
    /**
     * 过期时间一天，单位毫秒
     */

    private static long EXPIRE_TIME;
    /**
     * token私钥
     */

    private static String TOKEN_SECRET;

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的用户名
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取用户ID
     * @param token
     * @return token中包含的用户id
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获取用户角色
     * @param token
     * @return token中包含的用户角色
     */
    public static String getRole(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("roleId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成用户登录后的身份签名
     *
     * @param userName 用户名
     * @param userId 用户id
     * @param role 用户角色
     * @return 加密的token
     */
    public static String sign(String userName,String userId, String role) {
        try {
//            过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
//            私钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
//            设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            // 附带username，userId信息，生成签名
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userName", userName)
                    .withClaim("userId", userId)
                    .withClaim("roleId", role)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public  long getExpireTime() {
        return EXPIRE_TIME;
    }
    @Value("${token.expire_time}")
    public  void setExpireTime(long expireTime) {
        expireTime= expireTime==0?1:expireTime;
        EXPIRE_TIME = expireTime *60 * 60 * 24 * 1000;
    }

    public  String getTokenSecret() {
        return TOKEN_SECRET;
    }
    @Value("${token.token_secret:f26e587c28064d0e855e72c0a6a0e618}")
    public  void setTokenSecret(String tokenSecret) {
        TOKEN_SECRET = tokenSecret;
    }
}