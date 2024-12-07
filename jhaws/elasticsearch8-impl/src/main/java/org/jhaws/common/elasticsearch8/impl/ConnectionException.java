package org.jhaws.common.elasticsearch8.impl;

@SuppressWarnings("serial")
public class ConnectionException extends RuntimeException {
    public ConnectionException(java.net.ConnectException cause) {
        super(cause);
    }
}
