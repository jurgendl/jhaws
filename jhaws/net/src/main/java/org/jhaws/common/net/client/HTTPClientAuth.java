package org.jhaws.common.net.client;

import org.apache.http.auth.AuthScope;
import org.jhaws.common.io.security.SecureMe;
import org.jhaws.common.io.security.Security;

public class HTTPClientAuth {
    private transient String user;

    private transient byte[] pass;

    private final transient Security security;

    private transient String realm = AuthScope.ANY_REALM;

    private transient String host = AuthScope.ANY_HOST;

    private transient int port = AuthScope.ANY_PORT;

    private transient boolean preemptive = false;

    private transient String scheme = AuthScope.ANY_SCHEME;

    public HTTPClientAuth() {
        Security s;
        try {
            s = new org.jhaws.common.io.security.SecureMeBC();
        } catch (Exception ignore) {
            s = new SecureMe();
        }
        security = s;
    }

    public HTTPClientAuth(String user, String pass, String host) {
        this();
        setAuthentication(user, pass);
        setHost(host);
    }

    public HTTPClientAuth(String user, byte[] pass, String host) {
        this(user, new String(pass), host);
    }

    @Override
    public String toString() {
        return user + "@" + "<" + realm + ">" + host + ":" + port;
    }

    protected String getPass() {
        try {
            return getSecurity().decrypt(pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HTTPClientAuth resetAuthentication() {
        user = null;
        pass = null;
        return this;
    }

    public HTTPClientAuth setAuthentication(String user, String pass) {
        setUser(user);
        setPass(pass);
        return this;
    }

    protected String getUser() {
        return user;
    }

    public String getRealm() {
        return this.realm;
    }

    public HTTPClientAuth setRealm(String realm) {
        this.realm = realm;
        return this;
    }

    public int getPort() {
        return this.port;
    }

    public HTTPClientAuth setPort(int port) {
        this.port = port;
        return this;
    }

    public String getHost() {
        return this.host;
    }

    public HTTPClientAuth setHost(String host) {
        this.host = host;
        return this;
    }

    public boolean isPreemptive() {
        return this.preemptive;
    }

    public HTTPClientAuth setPreemptive(boolean preemptive) {
        this.preemptive = preemptive;
        return this;
    }

    public HTTPClientAuth setUser(String user) {
        this.user = user;
        return this;
    }

    public HTTPClientAuth setPass(String pass) {
        try {
            this.pass = getSecurity().encrypt(pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private Security getSecurity() {
        return this.security;
    }

    public HTTPClientAuth setPass(byte[] pass) {
        setPass(new String(pass));
        return this;
    }

    public String getScheme() {
        return this.scheme;
    }

    public HTTPClientAuth setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }
}
