package com.example.eurekaclient.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author laibin
 */
public class JwtUtil {

//    @Value("${token.timeout}")
//    private static int tokenTimeout;
    /**
     * 过期时间24小时
     */
    private static long EXPIRE_TIME = 60 * 24 * 60 * 1000;
    /**
     * 密钥
     */
    private static final String SECRET = "WcrhJkCdqCWKJucDVj5oHk43gk2WcR21cw7Yp3uwGxk=";

    /**
     * 根据签发者生成JWT token
     * @param appName  签发者
     * @return
     */
    public static String getJwtByIssuer(String appName){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        String token = Jwts.builder()
                // 签发者
                .setIssuer(appName)
                //设置唯一编号。
                .setId(UUID.randomUUID().toString())
                //签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                //*(必须)设置签名 使用HS256算法，并设置SecretKey(字符串)
                .signWith(secretKey)
                .compact();
        return token;
    }

    /**
     * 使用配置的默认密钥，使用传入的载荷生成JWT token
     * @param claims 载荷
     * @return
     */
    public static String getJwt(Map<String,Object> claims){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        String token = Jwts.builder()
                // 载荷
                .setClaims(claims)
                //设置唯一编号。
                .setId(UUID.randomUUID().toString())
                //签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                //*(必须)设置签名 使用HS256算法，并设置SecretKey(字符串)
                .signWith(secretKey)
                .compact();
        return token;
    }

    /**
     * 使用配置的默认密钥，解析JWT token,获取载荷
     * @param jwt
     * @return
     */
    public static Claims parseJwt( String jwt){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    /**
     * 使用传入的密钥，传入的载荷生成JWT token
     * @param claims 载荷
     * @return
     */
    public static String getJwtWithKey(String key,Map<String,Object> claims){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        String token = Jwts.builder()
                // 载荷
                .setClaims(claims)
                //设置唯一编号。
                .setId(UUID.randomUUID().toString())
                //签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                //*(必须)设置签名 使用HS256算法，并设置SecretKey(字符串)
                .signWith(secretKey)
                .compact();
        return token;
    }

    /**
     * 使用传入的密钥，解析JWT token,获取载荷
     * @param jwt
     * @return
     */
    public static Claims parseJwtWithKey( String key,String jwt){
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    /**
     * 读取JWT 载荷内容。不使用密钥
     * @param token
     * @return
     */
    public static JSONObject parseSubscriber(String token){
        String[] parts = token.split("\\.");
        if(parts.length != 3) {
            System.out.println("获取载荷信息失败");
            return null;
        }

        //payload
        String p = parts[1];
        String s = "";
        try{
            s = new String(Base64.decodeBase64(p));
        }catch(Exception ex){
            System.out.println("解析载荷信息失败");
            return null;
        }
        return JSONObject.parseObject(s);
    }

    /**
     * 获取一个符合JWT安全要求的随机密钥
     * @return
     */
    public static String getSecretKey(){
        //HS256 or HS384 or HS512
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        return  secretString;
    }

}
