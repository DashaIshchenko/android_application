package com.localmesh.utils;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

//Утилита для AES-шифрования сообщений (256-bit)
//Используется для защиты текста и файлов в mesh-сети

public class CryptoUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256; // 256-bit AES
    

     //Генерирует новый AES ключ

    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }
    
     //Конвертирует строку Base64 в SecretKey

    public static SecretKey base64ToKey(String base64Key) {
        byte[] decodedKey = Base64.decode(base64Key, Base64.DEFAULT);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
    
     //Конвертирует SecretKey в строку Base64

    public static String keyToBase64(SecretKey key) {
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }
    

     //Генерирует случайный IV (16 байт)

    public static byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    

     //Шифрует текст с использованием ключа и IV
    //Возвращает: Base64(IV + зашифрованные_данные)

    public static String encrypt(String plainText, SecretKey key) throws Exception {
        byte[] iv = generateIV();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        
        // Объединяем IV и зашифрованные данные
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        return Base64.encodeToString(combined, Base64.DEFAULT);
    }
    

     //Дешифрует текст
     //Ожидает: Base64(IV + зашифрованные_данные)

    public static String decrypt(String cipherText, SecretKey key) throws Exception {
        byte[] combined = Base64.decode(cipherText, Base64.DEFAULT);
        
        // Извлекаем IV (первые 16 байт)
        byte[] iv = new byte[16];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        // Извлекаем зашифрованные данные
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, "UTF-8");
    }
    

     //Шифрует файл (представленный как Base64 строка)

    public static String encryptFileData(String fileDataBase64, SecretKey key) throws Exception {
        byte[] fileData = Base64.decode(fileDataBase64, Base64.DEFAULT);
        byte[] iv = generateIV();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        
        byte[] encrypted = cipher.doFinal(fileData);
        
        // Объединяем IV и зашифрованные данные
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        return Base64.encodeToString(combined, Base64.DEFAULT);
    }
    

     //Дешифрует файл

    public static String decryptFileData(String encryptedFileData, SecretKey key) throws Exception {
        byte[] combined = Base64.decode(encryptedFileData, Base64.DEFAULT);
        
        byte[] iv = new byte[16];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);
        
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        
        byte[] decrypted = cipher.doFinal(encrypted);
        return Base64.encodeToString(decrypted, Base64.DEFAULT);
    }
}
