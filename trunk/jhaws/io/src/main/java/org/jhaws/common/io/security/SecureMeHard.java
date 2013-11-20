package org.jhaws.common.io.security;

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

/**
 * security using BouncyCastle
 */
public class SecureMeHard implements Security {
    static {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static String decrypt(String name, byte[] keyString) throws Exception {
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
        StringBuffer result = new StringBuffer();
        KeyParameter key = new KeyParameter(keyString);
        cipher.init(false, key);
        byte out[] = Base64.decode(name);
        byte out2[] = new byte[cipher.getOutputSize(out.length)];
        int len2 = cipher.processBytes(out, 0, out.length, out2, 0);
        cipher.doFinal(out2, len2);
        String s2 = new String(out2);
        for (int i = 0; i < s2.length(); i++) {
            char c = s2.charAt(i);
            if (c != 0) {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String encrypt(String value, byte[] keyString) throws Exception {
        BlowfishEngine engine = new BlowfishEngine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine);
        KeyParameter key = new KeyParameter(keyString);
        cipher.init(true, key);
        byte in[] = value.getBytes();
        byte out[] = new byte[cipher.getOutputSize(in.length)];
        int len1 = cipher.processBytes(in, 0, in.length, out, 0);
        try {
            cipher.doFinal(out, len1);
        } catch (CryptoException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        String s = new String(Base64.encode(out));
        return s;
    }

    private Seed key;

    public SecureMeHard() {
        this.key = new Seed();
    }

    @Override
    public String decrypt(byte[] pass) throws Exception {
        return SecureMeHard.decrypt(new String(pass), this.key.key);
    }

    @Override
    public byte[] encrypt(String pass) throws Exception {
        return SecureMeHard.encrypt(pass, this.key.key).getBytes();
    }
}
