
package com.ichliebephone.c2dm.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;

/**
 * 加密解密Utility
 * 
 * @author rabbit88
 */
public class EncryptUtil {
    private static final String AES_SEED = "COM.MEIZU.SNS";

    // ----------------------------AES加密解密--------------------------------------
    public static String encryptByAES(String source) {
//        return source;
        if (TextUtils.isEmpty(source)) {
            return source;
        }

        try {
            return encryptByAES(AES_SEED, source);
        } catch (Exception e) {
            e.printStackTrace();
            return source;
        }
    }

    public static String decryptByAES(String encrypted) {
//        return encrypted;
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }

        try {
            return decryptByAES(AES_SEED, encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return encrypted;
        }
    }

    /**
     * @param seed
     * @param cleartext
     * @return
     * @throws Exception
     */
    private static String encryptByAES(String seed, String cleartext) throws Exception {
        byte[] rawKey = getRawKeyByAES(seed.getBytes());
        byte[] result = encryptByAES(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    private static String decryptByAES(String seed, String encrypted) throws Exception {
        byte[] rawKey = getRawKeyByAES(seed.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decryptByAES(rawKey, enc);
        return new String(result);
    }

    private static byte[] getRawKeyByAES(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(128, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encryptByAES(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decryptByAES(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    /*
     * private static String toHex(String txt) { return toHex(txt.getBytes()); }
     * private static String fromHex(String hex) { return new
     * String(toByte(hex)); }
     */

    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    private static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    // ----------------------------AES加密解密--------------------------------------

    // ---------------------------MD5加密-------------------------------------
    public static String digestByMD5(String source) {
        String sign = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] signBytes = md5.digest(source.getBytes());
            sign = toHexString(signBytes);
        } catch (Exception e) {
            AndroidUtils.debug("MD5 digest catch error: " + e.getMessage());
        }
        return sign;
    }

    private static char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    // ---------------------------MD5加密-------------------------------------
}
