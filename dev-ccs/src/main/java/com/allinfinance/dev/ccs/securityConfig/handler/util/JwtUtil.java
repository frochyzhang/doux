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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Mutz
 */
@Component
public class JwtUtil {
    /**
     * 过期时间，单位毫秒
     */

    private static long EXPIRE_TIME;
    /**
     * 过期时间，单位毫秒,用做token备用
     */
    private static long REFRESH_EXPIRE_TIME;
    /**
     * token私钥
     */

    private static String TOKEN_SECRET;

    private static long EXPIRE_END_TIME;
    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public static boolean verify(String token) {

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Date expiresAt = jwt.getExpiresAt();
            Date date = new Date();
            if(date.after(expiresAt)){
                throw  new TokenExpiredExeption("token过期");
            }
            return true;
        } catch (Exception e) {
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
     * 签发access  token
     * @param userName
     * @param userId
     * @param role
     * @param org
     * @return
     */
    public static String sign(String userName,String userId, String role,String org) {
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        setExpireEndTime(date.getTime());
        return sign(userName,userId,role,org,date);
    }


    /**
     * 签发refresh token
     * @param userName
     * @param userId
     * @param role
     * @param org
     * @return
     */
    public static String signRefresh(String userName,String userId, String role,String org) {
        return sign(userName,userId,role,org,new Date(System.currentTimeMillis() + EXPIRE_TIME));
    }
    /**
     * 生成用户登录后的身份签名
     *
     * @param userName 用户名
     * @param userId 用户id
     * @param role 用户角色
     * @return 加密的token
     */
    public static String sign(String userName,String userId, String role,String org,Date date) {
        try {
//
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
                    .withIssuedAt(new Date())
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
     * 判断token是否过期
     * @param
     * @return
     */
    public static  boolean isJwtExpired (String token){
        return  JWT.decode(token).getExpiresAt().before(new Date());
    }

    /**
     * 判断是否马上过期
     * @param token
     * @return
     */
    public static  boolean isWillExpired (String token){
        Date expires = JWT.decode(token).getExpiresAt();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(expires);
        calendar.add(Calendar.HOUR,-1);
        Date calendarTime = calendar.getTime();
        return calendarTime.before(new Date());
    }

    public static DefaultJws  setJwtExpired (Jws<Claims> jws){
        Claims claims = jws.getBody().setExpiration((new Date(System.currentTimeMillis() + EXPIRE_TIME)));
        DefaultJws defaultJws = new DefaultJws(jws.getHeader(), claims, jws.getSignature());
        return defaultJws;
    }

    public static long getExpireTime() {
        return EXPIRE_TIME;
    }
    @Value("${token.expire_time:7}")
    public  void setExpireTime(long expireTime) {
        expireTime= expireTime==0?1:expireTime;
        EXPIRE_TIME = expireTime  *60* 1000;
    }

    public static String getTokenSecret() {
        return TOKEN_SECRET;
    }
    @Value("${token.token_secret:f26e587c28064d0e855e72c0a6a0e618}")
    public  void setTokenSecret(String tokenSecret) {
        TOKEN_SECRET = tokenSecret;
    }
    public static long getRefreshExpireTime() {
        return REFRESH_EXPIRE_TIME;
    }

    @Value("${token.refresh_expire_time:30}")
    public static void setRefreshExpireTime(long refreshExpireTime) {
        REFRESH_EXPIRE_TIME = refreshExpireTime;
    }

    public static long getExpireEndTime() {
        return EXPIRE_END_TIME;
    }

    public static void setExpireEndTime(long expireEndTime) {
        EXPIRE_END_TIME = expireEndTime;
    }
}