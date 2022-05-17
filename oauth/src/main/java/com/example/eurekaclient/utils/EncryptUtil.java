package com.example.eurekaclient.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.*;

public class EncryptUtil {
    //用于生成密钥加入的盐
    private static final String SLAT_KEY = "test";


    /**
     * 根据slatKey获取公匙，RSA算法
     */
    public static Key getPublicKey() throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(SLAT_KEY.getBytes());
        keyPairGenerator.initialize(1024, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey keyPairPublic = keyPair.getPublic();
        return keyPairPublic;
    }
    /**
     * 获取公钥，base64字符串
     * @return
     * @throws Exception
     */
    public static String getPublicKeyString() throws Exception {
        Key publicKey = getPublicKey();
        //将密钥转化成base64字符串，便于传输保存----------------------------------------------------------
        byte[] encoded = publicKey.getEncoded();
        //转换为base64字符串
        String PublicKeyBase64String = Base64.encodeBase64String(encoded);
        return PublicKeyBase64String;
    }
    /**
     * 使用RAS算法生成的公钥加密内容
     * @param content
     * @return
     * @throws Exception
     */
    public static String getRSAEncryptedString(String content) throws Exception {
        Key publicKey = getPublicKey();
        //加密模式
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        //密文化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeBase64String = Base64.encodeBase64String(encrypted);
        return  encodeBase64String;
    }

    /**
     * 根据slatKey获取私匙，传入的slatKey作为SecureRandom的随机种子
     */
    public static Key getPrivateKey() throws Exception {
        KeyPairGenerator keyPairGenerator  = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(SLAT_KEY.getBytes());
        keyPairGenerator.initialize(1024, random);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey keyPairPrivate = keyPair.getPrivate();
        return keyPairPrivate;


    }

    /**
     * 获取私钥，base64字符串
     * @return
     * @throws Exception
     */
    public static String getPrivateKeyString() throws Exception {
        Key privateKey = getPrivateKey();
        //将密钥转化成base64字符串，便于传输保存----------------------------------------------------------
        byte[] encoded = privateKey.getEncoded();
        //转换为base64字符串
        String privateKeyBase64String = Base64.encodeBase64String(encoded);
        return privateKeyBase64String;
    }

    /**
     * 使用RSA算法生成的私钥，解密加密内容
     * @param base64Content
     * @return
     * @throws Exception
     */
    public static String getRSADecryptedString(String base64Content) throws Exception {
        //base64字符串解码
        byte[] content = Base64.decodeBase64(base64Content);
        Key privateKey = getPrivateKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encrypted = cipher.doFinal(content);
        String s = new String(encrypted);
        return s;
    }

    /**
     * 获取简单加盐对称加密(AES)的密钥
     * @throws Exception
     */
    public static Key getKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(SLAT_KEY.getBytes());
        kgen.init(128, random);
        Key key = kgen.generateKey();
        return key;
    }

    /**
     * 获取简单加盐对称加密(AES)的密钥,base64字符串
     * @throws Exception
     */
    public static String getKeyString() throws Exception {
        Key key = getKey();
        String encodeKeyBase64String = Base64.encodeBase64String(key.getEncoded());
        return encodeKeyBase64String;
    }

    /**
     * 使用AES算法生成的密钥加密内容
     * @param content
     * @return
     * @throws Exception
     */
    public static String getAESEncryptedString(String content) throws Exception {
        Key key = getKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        //密文化成base64字符串，便于传输保存----------------------------------------------------------
        String encodeBase64String = Base64.encodeBase64String(encrypted);
        return encodeBase64String;
    }

    /**
     * 使用AES算法生成的密钥解密内容
     * @param base64Content
     * @return
     * @throws Exception
     */
    public static String getAESDecryptedString(String base64Content) throws Exception {
        //base64字符串解码
        byte[] content = Base64.decodeBase64(base64Content);
        Key key = getKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] encrypted = cipher.doFinal(content);
        String s = new String(encrypted);
        return s;
    }
}
