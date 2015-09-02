package org.jhaws.common.io.security;

import java.security.SecureRandom;

import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DESedeKeyGenerator;
import org.bouncycastle.crypto.params.DESedeParameters;
import org.bouncycastle.crypto.prng.ThreadedSeedGenerator;

public class Seed {
    static {
        BC.provide();
    }

    private static final ThreadedSeedGenerator sgen = new ThreadedSeedGenerator();

    public static byte[] randomKey(byte[] seed) {
        DESedeKeyGenerator kg = new DESedeKeyGenerator();
        kg.init(new KeyGenerationParameters(new SecureRandom(seed), DESedeParameters.DES_EDE_KEY_LENGTH * 8/* 24*8=192 // or 128 */));
        return kg.generateKey();
    }

    public static byte[] randomSeed(boolean fast) {
        int numBytes = 128 + (Math.abs(Seed.sgen.generateSeed(1, true)[0]) / 2) + (Math.abs(Seed.sgen.generateSeed(1, true)[0]) / 2);
        return Seed.sgen.generateSeed(numBytes, fast);
    }

    protected byte[] key;

    public Seed() {
        this.key = Seed.randomKey(Seed.randomSeed(false));
    }
}
