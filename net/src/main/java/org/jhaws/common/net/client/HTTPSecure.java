package org.jhaws.common.net.client;

public interface HTTPSecure {
    String decrypt(byte[] pass) throws Exception;

    byte[] encrypt(String pass) throws Exception;
}
