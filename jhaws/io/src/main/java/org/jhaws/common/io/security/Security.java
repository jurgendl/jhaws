package org.jhaws.common.io.security;

public interface Security {
    String decrypt(byte[] pass) throws Exception;

    byte[] encrypt(String pass) throws Exception;
}
