package com.mountain.tool.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Random;

public class SecurityKits {

    public static final String ALGORITHM_SHA1 = "SHA1";

    public static final String ALGORITHM_MD5 = "MD5";

    /**
     * AES对称加密算法
     */
    public static final String ALGORITHM_AES = "AES";

    private final static String CHAR_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_*#$%()!";

    private final static int CHAR_STRING_LEN = CHAR_STRING.length();

    public static String sha1Encode(String str) {
        return hashEncode(ALGORITHM_SHA1, str);
    }

    public static String md5Encode(String str) {
        return hashEncode(ALGORITHM_MD5, str);
    }

    /**
     * 带盐的MD5加密
     *
     * @param source 原始明文，待加密的字符串
     * @param salt   加密的盐
     * @return String 返回加密之后的结果
     */
    public static String md5CryptWithSalt(String source, String salt) {
        return md5CryptWithSalt(source.getBytes(StandardCharsets.UTF_8), salt);
    }

    /**
     * AES加密
     *
     * @param source
     * @param secret
     * @return
     */
    public static String encryptAES(String source, String secret) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM_AES);
            // seeds
            byte[] seeds = secret.getBytes(StandardCharsets.UTF_8);
            // 初始化
            generator.init(128, new SecureRandom(seeds));
            // 生成随机密钥
            SecretKey secretKey = generator.generateKey();
            // 生成密钥规范
            byte[] encoded = secretKey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(encoded, ALGORITHM_AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 转换为字节数组
            byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(bytes);
            return byteToHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * SHA256加密
     *
     * @param source 原始明文，待加密的字符串
     * @return 返回加密之后的结果
     */
    public static String sha256Crypt(String source) {
        // 获取信息摘要
        MessageDigest ctx = DigestUtils.getSha256Digest();
        final byte[] bytes = source.getBytes(StandardCharsets.UTF_8);
        ctx.update(bytes);
        byte[] digest = ctx.digest();
        return byteToHex(digest);
    }

    /**
     * 带盐的MD5加密
     *
     * @param keyBytes source 原始明文的byte字节
     * @param salt     加密的盐
     * @return 返回加密之后的结果
     */
    public static String md5CryptWithSalt(final byte[] keyBytes, String salt) {
        // 获取字节数组的长度
        final int keyLen = keyBytes.length;
        // 获取盐值的字节数组
        // Extract the real salt from the given string which can be a complete hash
        // string.
        String saltString = salt;
        if (salt == null) {
            saltString = CHAR_STRING;
        }
        final byte[] saltBytes = saltString.getBytes(StandardCharsets.UTF_8);
        // 获取信息摘要
        MessageDigest ctx = DigestUtils.getMd5Digest();
        /*
		 * ketBytes first
		 */
        ctx.update(keyBytes);
		/*
		 * Then the raw salt
		 */
        ctx.update(saltBytes);
		/*
		 * Then just as many characters of the MD5(pw,salt,pw)
		 */
        MessageDigest ctx1 = DigestUtils.getMd5Digest();
        ctx1.update(keyBytes);
        ctx1.update(saltBytes);
        ctx1.update(keyBytes);
        byte[] finalb = ctx1.digest();
        int ii = keyLen;
        while (ii > 0) {
            ctx.update(finalb, 0, ii > 16 ? 16 : ii);
            ii -= 16;
        }

		/*
		 * Don't leave anything around in vm they could use.
		 */
        Arrays.fill(finalb, (byte) 0);

        ii = keyLen;
        final int j = 0;
        while (ii > 0) {
            if ((ii & 1) == 1) {
                ctx.update(finalb[j]);
            } else {
                ctx.update(keyBytes[j]);
            }
            ii >>= 1;
        }

		/*
		 * Now make the output string
		 */
        finalb = ctx.digest();
        return byteToHex(finalb);
    }

    public static String sha1EncodeBase64(String str) {
        return hashEncodeBase64(ALGORITHM_SHA1, str, false);
    }

    public static String md5EncodeBase64(String str) {
        return hashEncodeBase64(ALGORITHM_MD5, str, false);
    }

    public static String sha1EncodeYMBase64(String str) {
        return hashEncodeBase64(ALGORITHM_SHA1, str, true);
    }

    public static String md5EncodeYMBase64(String str) {
        return hashEncodeBase64(ALGORITHM_MD5, str, true);
    }

    public static String hashEncode(String algorithm, String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashEncodeBase64(String algorithm, String str, boolean ym) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(str.getBytes());
            return getBase64Text(messageDigest.digest(), ym);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBase64Text(byte[] bytes, boolean ym) {
        String text = Base64.encodeBase64String(bytes);
        return ym ? text.replace("=", "").replace("+", "x").replace("/", "7") : text;
    }

    final static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getFormattedText(byte[] bytes) {
        return byteToHex(bytes);
		/*
		 * int len = bytes.length; StringBuilder buf = new StringBuilder(len * 2); for
		 * (int j = 0; j < len; j++) { buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
		 * buf.append(HEX_DIGITS[bytes[j] & 0x0f]); } return buf.toString();
		 */
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String randomString(int length) {
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(CHAR_STRING_LEN);
            buf.append(CHAR_STRING.charAt(num));
        }
        return buf.toString();
    }

    static class B64 {

        /**
         * Table with characters for Base64 transformation.
         */
        static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        /**
         * Base64 like conversion of bytes to ASCII chars.
         *
         * @param b2     A byte from the result.
         * @param b1     A byte from the result.
         * @param b0     A byte from the result.
         * @param outLen The number of expected output chars.
         * @param buffer Where the output chars is appended to.
         */
        static void b64from24bit(final byte b2, final byte b1, final byte b0, final int outLen,
                                 final StringBuilder buffer) {
            // The bit masking is necessary because the JVM byte type is signed!
            int w = ((b2 << 16) & 0x00ffffff) | ((b1 << 8) & 0x00ffff) | (b0 & 0xff);
            // It's effectively a "for" loop but kept to resemble the original C code.
            int n = outLen;
            while (n-- > 0) {
                buffer.append(B64T.charAt(w & 0x3f));
                w >>= 6;
            }
        }

        /**
         * Generates a string of random chars from the B64T set.
         *
         * @param num Number of chars to generate.
         */
        static String getRandomSalt(final int num) {
            final StringBuilder saltString = new StringBuilder();
            for (int i = 1; i <= num; i++) {
                saltString.append(B64T.charAt(new Random().nextInt(B64T.length())));
            }
            return saltString.toString();
        }
    }
}
