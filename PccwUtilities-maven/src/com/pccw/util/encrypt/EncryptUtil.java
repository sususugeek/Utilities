package com.pccw.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {
	
    public static String encryptBase64 (String pSecretKey, String pString) throws Exception {
        DESKeySpec key = new DESKeySpec(pSecretKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        //Instantiate the encrypter/decrypter
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(key));

        byte[] unencryptedByteArray = pString.getBytes("UTF8");

        // Encrypt
        byte[] encryptedBytes = cipher.doFinal(unencryptedByteArray);

        // Encode bytes to base64 to get a string
        byte [] encodedBytes = Base64.encodeBase64(encryptedBytes);

        return new String(encodedBytes, "UTF8");
    }
    
    public static String decryptBase64 (String pSecretKey, String pString) throws Exception {
        DESKeySpec key = new DESKeySpec(pSecretKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        //Instantiate the encrypter/decrypter
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generateSecret(key));

        byte [] decodedBytes = Base64.decodeBase64(pString.getBytes("UTF8"));

        // Decrypt
        byte[] unencryptedByteArray = cipher.doFinal(decodedBytes);

        // Decode using utf-8
        return new String(unencryptedByteArray, "UTF8");
    }

}
