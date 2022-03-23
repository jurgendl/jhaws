package org.jhaws.common.io.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

// https://stackoverflow.com/questions/1132567/encrypt-password-in-configuration-files/1133815#1133815
public class SecureMeAlt implements Security {
    private static final String SECRET_KEY_FACTORY = "PBKDF2WithHmacSHA512";

    private static final String SECRET_KEY = "AES";

    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    // private static final String SECRET_KEY = "Blowfish";
    //
    // private static final String CIPHER = "Blowfish";

    private SecretKeySpec key;

    public SecureMeAlt(byte[] seed, String masterPass) {
        byte[] salt = seed == null ? new Seed().key : seed;
        int iterationCount = 40000;
        int keyLength = 128;
        try {
            key = createSecretKey(masterPass.toCharArray(), salt, iterationCount, keyLength);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), SECRET_KEY);
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance(CIPHER);
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8.toString()));
        byte[] iv = ivParameterSpec.getIV();
        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(cryptoText);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance(CIPHER);
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Base64.getDecoder().decode(iv)));
        return new String(pbeCipher.doFinal(Base64.getDecoder().decode(property)), StandardCharsets.UTF_8.toString());
    }

    @Override
    public String decrypt(byte[] pass) throws Exception {
        return decrypt(new String(pass), key);
    }

    @Override
    public byte[] encrypt(String pass) throws Exception {
        return encrypt(pass, key).getBytes();
    }
}
