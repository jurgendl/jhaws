package org.jhaws.common.io.security;

import java.security.SecureRandom;
import java.util.Random;

import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.params.DESedeParameters;

public class Seed {
    static {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static byte[] generateKey(byte[] seed) {
        DESedeKeyGenerator kg = new DESedeKeyGenerator();
        kg.init(new KeyGenerationParameters(new SecureRandom(seed), DESedeParameters.DES_EDE_KEY_LENGTH * 8));
        return kg.generateKey();
    }

    public static byte[] rndSeed() {
        Random rnd = new Random(System.currentTimeMillis());
        byte[] seed = new byte[128 + (rnd.nextInt(64) * 2)];
        rnd.nextBytes(seed);

        return seed;
    }

    protected byte[] key;

    public Seed() {
        this.key = Seed.generateKey(Seed.rndSeed());
    }
}
