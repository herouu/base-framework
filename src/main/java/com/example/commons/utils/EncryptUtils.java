package com.example.commons.utils;

import com.example.commons.exception.FrameworkUtilException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by gizmo on 15/12/5.
 */
public final class EncryptUtils {
    private static final int RANDOM_LENGTH = 48;
    private static final int SALT_LENGTH = 32;
    private static final int ITERATIONS = 1000;
    private static final int DESIRED_KEY_LENGTH = 256;
    private static final String SALT_TOKEN_ALGORITHMS = "SHA1PRNG";
    private static final String PASSWORD_ALGORITHMS = "PBKDF2WithHmacSHA1";

    private EncryptUtils() {
    }

    public static String generateRandom() {
        return generateSecureRandom(RANDOM_LENGTH);
    }

    public static String generateSalt() {
        return generateSecureRandom(SALT_LENGTH);
    }

    private static String generateSecureRandom(int length) {
        try {
            SecureRandom random = SecureRandom.getInstance(SALT_TOKEN_ALGORITHMS);
            byte[] bytes = random.generateSeed(length);

            return Base64.encodeBase64String(bytes).replaceAll("\\+", "A").replaceAll("/", "b").replaceAll("=", "C");
        } catch (NoSuchAlgorithmException e) {
            throw new FrameworkUtilException(e);
        }
    }

    public static String encryptPassword(String password, String salt) {
        try {
            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("密码不能为空");
            }
            if (StringUtils.isBlank(salt)) {
                throw new IllegalArgumentException("盐值不能为空");
            }

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_ALGORITHMS);
            SecretKey key = secretKeyFactory.generateSecret(new PBEKeySpec(
                    password.toCharArray(), Base64.decodeBase64(salt), ITERATIONS, DESIRED_KEY_LENGTH)
            );

            return Base64.encodeBase64String(key.getEncoded());
        } catch (Exception e) {
            throw new FrameworkUtilException(e);
        }
    }

    public static boolean validatePassword(String password, String token, String encryptPassword) {
        try {
            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("密码不能为空");
            }
            if (StringUtils.isBlank(token)) {
                throw new IllegalArgumentException("盐值不能为空");
            }
            if (StringUtils.isBlank(encryptPassword)) {
                throw new IllegalArgumentException("加密密码不能为空");
            }

            String verifyPassword = encryptPassword(password, token);
            return StringUtils.equals(verifyPassword, encryptPassword);
        } catch (IllegalArgumentException e) {
            throw new FrameworkUtilException(e);
        }
    }


    /**
     * 对字符串进行md5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                int v = (int) b[i];
                v = v < 0 ? 0x100 + v : v;
                String cc = Integer.toHexString(v);
                if (cc.length() == 1)
                    sb.append('0');
                sb.append(cc);
            }

            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 对字符串进行md5加密(16位）
     *
     * @param str
     * @return 返回MD5 16位字符串
     */
    public static String md516(String str) {
        String md5 = md5(str);
        if (StringUtils.isBlank(md5)) {
            return md5;
        }
        return md5.replaceAll("^\\w{8}(\\w{16})\\w+$", "$1");
    }

    /**
     * 对字符串进行sha256加密
     */
    public static String sha256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());

            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                int v = (int) b[i];
                v = v < 0 ? 0x100 + v : v;
                String cc = Integer.toHexString(v);
                if (cc.length() == 1)
                    sb.append('0');
                sb.append(cc);
            }

            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 对字符串进行sha1加密
     *
     * @param str
     * @return
     */
    public static String sha1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());

            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                int v = (int) b[i];
                v = v < 0 ? 0x100 + v : v;
                String cc = Integer.toHexString(v);
                if (cc.length() == 1)
                    sb.append('0');
                sb.append(cc);
            }

            return sb.toString();
        } catch (Exception e) {
        }
        return "";
    }
}
