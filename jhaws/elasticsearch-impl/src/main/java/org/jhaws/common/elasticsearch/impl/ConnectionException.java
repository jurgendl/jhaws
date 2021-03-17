package org.jhaws.common.elasticsearch.impl;

@SuppressWarnings("serial")
public class ConnectionException extends RuntimeException {
    public ConnectionException(java.net.ConnectException cause) {
        super(cause);
    }
}
