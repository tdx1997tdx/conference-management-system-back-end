package com.sustech.conferenceSystem.util;
import sun.misc.BASE64Encoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * 获取用户唯一token值
 */
public class Token {
    private Token(){};
    private static final Token instance = new Token();

    public static Token getInstance() {
        return instance;
    }

    /**
     * 生成Token
     * @return
     */
    public String makeToken(String username) {
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + username;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte md5[] =  md.digest(token.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
