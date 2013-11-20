package org.jhaws.common.io.security;

import java.security.SecureRandom;
import java.util.Random;

import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.params.DESedeParameters;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;

public class Seed {
    static {
        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private static final ThreadedSeedGenerator sgen = new ThreadedSeedGenerator();

    public static byte[] randomKey(byte[] seed) {
        DESedeKeyGenerator kg = new DESedeKeyGenerator();
        kg.init(new KeyGenerationParameters(new SecureRandom(seed), DESedeParameters.DES_EDE_KEY_LENGTH * 8));
        return kg.generateKey();
    }

    public static byte[] randomSeed() {
        Random rnd = new Random(System.currentTimeMillis());
        return Seed.sgen.generateSeed(128 + rnd.nextInt(64) + rnd.nextInt(64), false);
    }

    protected byte[] key;

    public Seed() {
        this.key = Seed.randomKey(Seed.randomSeed());
    }
}
