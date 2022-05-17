package com.example.eurekaclient;

import com.alibaba.fastjson.JSONObject;
import com.example.eurekaclient.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

//@SpringBootTest
class EurekaClientApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 根据slatKey获取公匙，传入的slatKey作为SecureRandom的随机种子
     * 若使用new SecureRandom()创建公匙，则需要记录下私匙，解密时使用
     */
    @Test
    public void getPublicKey() throws Exception {
        String slatKey = "test";    //生成密钥的盐
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);//or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey keyPairPublic = keyPair.getPublic();
        //将密钥转化成base64字符串，便于传输保存----------------------------------------------------------
        byte[] encoded = keyPair.getPublic().getEncoded();
        String PublicKeyBase64String = Base64.encodeBase64String(encoded); //转换为base64字符串
        System.out.println(PublicKeyBase64String);
        //加密过程----------------------------------------------------------
        Cipher cipher = Cipher.getInstance("RSA"); //加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keyPairPublic); //参数： 加密/解密，秘钥
        byte[] encrypted = cipher.doFinal("123456".getBytes());  //开始加密解密
        //密文化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeBase64String = Base64.encodeBase64String(encrypted); //转换为base64字符串
        System.out.println(encodeBase64String);


    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    @Test
    public void getPrivateKey() throws Exception {
        String slatKey = "test"; //生成密钥的盐
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        keyPairGenerator.initialize(1024, random);// or 2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair(); //得到密钥对，公钥私钥
        PrivateKey keyPairPrivate = keyPair.getPrivate(); //获取私钥
        //将密钥转化成base64字符串，便于传输保存----------------------------------------------------------
        byte[] encoded = keyPair.getPrivate().getEncoded();
        String PrivateKeyBase64String = Base64.encodeBase64String(encoded); //转换为base64字符串
        System.out.println(PrivateKeyBase64String);
        //解密过程----------------------------------------------------------
        String base64Content="QAbzVeBjcV6Iu4UVoMjKlFl6uDccGVMOdQB4Tn2h7zAzlL+/Y/4l/R7XWXNlOdLLfXSEt0gg/Y0i8l7M5OufzFgnlste02j0OYFclH0sNDqBscqiHuDg/JoTuoBxsUqJxzAIXPycaRC/9qo9NES5PmnAfVIYnETdo6xsmr4WMCg="; //密文（base64字符串)
        byte[] content = Base64.decodeBase64(base64Content); //base64字符串解码
        Cipher cipher = Cipher.getInstance("RSA"); //加密的方法 AES/CBC/PKCS5Padding ....
        cipher.init(Cipher.DECRYPT_MODE, keyPairPrivate); //参数：加密/解密，秘钥
        byte[] encrypted = cipher.doFinal(content); //解密
        String s = new String(encrypted);
        System.out.println(s);
    }

    /**
     * 简单加盐对称加密
     * @throws Exception
     */
    @Test
    public void EnTest() throws Exception {
        //获取秘钥---------------------------------------------------------
        String slatKey = "hahahaha"; ////生成密钥的盐
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "DES"); //参数：盐，加密方法
        //加密过程-------------------------------------------------------------------------
        String content="123456"; //加密内容
        Cipher cipher = Cipher.getInstance("DES"); //加密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); //参数：加密/解密，秘钥
        byte[] encrypted = cipher.doFinal(content.getBytes());  //开始加密解密
        //密文化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeBase64String = Base64.encodeBase64String(encrypted); //转换为base64字符串
        System.out.println(encodeBase64String);
    }

    /**
     * 简单加盐对称解密
     * @throws Exception
     */
    @Test
    public void DeTest() throws Exception {
        //获取秘钥---------------------------------------------------------
        String slatKey = "hahahaha"; ////生成密钥的盐
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(), "DES"); //参数：盐，加密方法
        //解密过程-------------------------------------------------------------------------
        String base64Content="nZTTCy9wmGU="; //密文（base64字符串)
        byte[] content = Base64.decodeBase64(base64Content); //base64字符串解码
        Cipher cipher = Cipher.getInstance("DES"); //加密的方法 AES/CBC/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE, secretKey); //参数：加密/解密，秘钥
        byte[] encrypted = cipher.doFinal(content); //解密
        String s = new String(encrypted);
        System.out.println(s);
    }

    /**
     * 获取加密的密匙，传入的slatKey可以是任意长度的，作为SecureRandom的随机种子，
     * 而在KeyGenerator初始化时设置密匙的长度128bit(16位byte)
     */
    @Test
    public void testEncryptAES() throws Exception {
        String slatKey = "test"; //用于生成密钥加的盐
        KeyGenerator kgen = KeyGenerator.getInstance("AES"); //加密算法
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        kgen.init(128, random);
        Key key = kgen.generateKey();
        //转化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeKeyBase64String = Base64.encodeBase64String(key.getEncoded());
        System.out.println(encodeKeyBase64String);
        //加密过程-------------------------------------------------------------------------
        String content="123456"; //加密内容
        Cipher cipher = Cipher.getInstance("AES"); //加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key); //参数：加密/解密，秘钥
        byte[] encrypted = cipher.doFinal(content.getBytes());  //开始加密解密
        //密文化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeBase64String = Base64.encodeBase64String(encrypted); //转换为base64字符串
        System.out.println(encodeBase64String);
    }
    @Test
    public void testDecryptAES() throws Exception {
        String slatKey = "test";
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(slatKey.getBytes());
        kgen.init(128, random);
        Key key = kgen.generateKey();
        //解密过程-------------------------------------------------------------------------
        String base64Content="a8MOBlAraD7hbgMwCZI4CA=="; //密文（base64字符串)
        byte[] content = Base64.decodeBase64(base64Content); //base64字符串解码
        Cipher cipher = Cipher.getInstance("AES"); //加密的方法 AES/CBC/PKCS5Padding
        cipher.init(Cipher.DECRYPT_MODE,key); //参数：加密/解密，秘钥
        byte[] encrypted = cipher.doFinal(content); //解密
        String s = new String(encrypted);
        System.out.println(s);
    }

    @Test
    public void jwtTest() throws Exception{
        JwtUtil jwtUtil = new JwtUtil();
        Map<String,Object> claims = new HashMap<>();
        claims.put("sub","aaaa");
        String jwt = JwtUtil.getJwtByClaims(claims);
        Claims claims1 = JwtUtil.parseJwt(jwt);
        JSONObject jsonObject = JwtUtil.parseSubscriber(jwt);
    }

}
