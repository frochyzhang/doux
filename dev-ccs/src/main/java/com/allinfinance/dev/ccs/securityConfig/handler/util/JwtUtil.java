package com.allinfinance.dev.ccs.securityConfig.handler.util;

import com.allinfinance.dev.ccs.exception.TokenExpiredExeption;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultJws;
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
            Date expiresAt = jwt.getExpiresAt();
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
     * 获取用户所属机构
     * @param token
     * @return token中包含的用户所属机构
     */
    public static String getOrg(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("org").asString();
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
    public static String sign(String userName,String userId, String role,String org) {
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
                    .withClaim("org", org)
                    .withClaim("creatDate",System.currentTimeMillis())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
    public static Jws<Claims>  psrserAuthenticteToken(String token){
        try{
            Jws<Claims> parser = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            return parser;

        }catch (ExpiredJwtException e){
            return  new DefaultJws<>(null,e.getClaims(),"");
        }
    }


    /**
     * 判断jw是否过期
     * @param jws
     * @return
     */
    public static  boolean isJwtExpired (Jws<Claims> jws){
        return  jws.getBody().getExpiration().before(new Date());
    }

    public static DefaultJws  setJwtExpired (Jws<Claims> jws){
        Claims claims = jws.getBody().setExpiration((new Date(System.currentTimeMillis() + EXPIRE_TIME)));
        DefaultJws defaultJws = new DefaultJws(jws.getHeader(), claims, jws.getSignature());
        return defaultJws;
    }

    public  long getExpireTime() {
        return EXPIRE_TIME;
    }
    @Value("${token.expire_time:86400}")
    public  void setExpireTime(long expireTime) {
        expireTime= expireTime==0?1:expireTime;
        EXPIRE_TIME = expireTime  * 1000;
    }

    public  String getTokenSecret() {
        return TOKEN_SECRET;
    }
    @Value("${token.token_secret:f26e587c28064d0e855e72c0a6a0e618}")
    public  void setTokenSecret(String tokenSecret) {
        TOKEN_SECRET = tokenSecret;
    }
}