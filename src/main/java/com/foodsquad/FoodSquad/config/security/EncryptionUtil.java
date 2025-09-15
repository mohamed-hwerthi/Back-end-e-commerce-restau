package com.foodsquad.FoodSquad.config.security;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGO = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_SIZE = 12;

    private static final String SECRET_KEY = "12345678901234567890123456789012";

    public static String encrypt(String data) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] encryptedIvAndText = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedIvAndText, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndText);
    }

    public static String decrypt(String encryptedData) throws Exception {
        byte[] encryptedIvAndText = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedIvAndText, 0, iv, 0, iv.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGO);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

        byte[] encryptedText = new byte[encryptedIvAndText.length - IV_SIZE];
        System.arraycopy(encryptedIvAndText, IV_SIZE, encryptedText, 0, encryptedText.length);

        byte[] decrypted = cipher.doFinal(encryptedText);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}

