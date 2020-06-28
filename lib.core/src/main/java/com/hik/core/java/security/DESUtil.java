package com.hik.core.java.security;

import android.app.Application;
import android.content.Context;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Des加解密工具
 *
 * @author gongwei
 * @date 2019/2/12
 */
public class DESUtil {

    private static final String CHAR_SET = "UTF-8";

    /**
     * 获取so动态库中的KEY
     *
     * @see SecurityFactory's getKeyFromNative()
     *
     * @param context
     * @return
     */
    public static String getKeyFromNative(Context context) {
        return SecurityFactory.getKeyFromNative(context);
    }

    /**
     * 加密
     *
     * @param srcStr
     * @return 加密串
     */
    public static String encrypt(String srcStr, Application application) {
        byte[] src = srcStr.getBytes(Charset.forName(CHAR_SET));
        return encrypt(src, getKeyFromNative(application));
    }

    /**
     * 解密
     *
     * @param hexStr
     * @return 解密串
     */
    public static String decrypt(String hexStr, Application Application) throws Exception {
        return decrypt(hexStr, getKeyFromNative(Application));
    }

    /**
     * 加密
     *
     * @param data
     * @param sKey
     * @return
     */
    public static String encrypt(byte[] data, String sKey) {
        try {
            byte[] key = sKey.getBytes();
            // 初始化向量
            IvParameterSpec iv = new IvParameterSpec(key);
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 执行加密操作：加密-Base64-URLEncoder
            return URLEncoder.encode(Base64.encode(cipher.doFinal(data)), CHAR_SET);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src
     * @param sKey
     * @return
     */
    public static String decrypt(String src, String sKey) throws Exception {
        byte[] key = sKey.getBytes();
        // 初始化向量
        IvParameterSpec iv = new IvParameterSpec(key);
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key);
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 开始解密操作：URLDecoder-Base64-解密
        return new String(cipher.doFinal(Base64.decode(URLDecoder.decode(src, CHAR_SET))));
    }
}
