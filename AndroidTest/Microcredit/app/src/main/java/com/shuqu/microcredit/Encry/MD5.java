package com.shuqu.microcredit.Encry;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class Info ：
 * Created by Lyndon on 16/6/13.
 */
public class MD5 {
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F' };

    public static String md5Encrypt(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder messageDigest32 = new StringBuilder(messageDigest.length * 2);
            for (int i = 0; i < messageDigest.length; i++) {
                messageDigest32.append(HEX_DIGITS[(messageDigest[i] & 0xf0) >>> 4]);
                messageDigest32.append(HEX_DIGITS[messageDigest[i] & 0x0f]);
            }

            return messageDigest32.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

}
