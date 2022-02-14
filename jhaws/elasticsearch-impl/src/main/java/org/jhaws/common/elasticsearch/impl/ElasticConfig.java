package org.jhaws.common.elasticsearch.impl;

public class ElasticConfig extends ElasticHelper {
    // @Value("${elastic.cluster.user:elastic}")
    private String user = "elastic";

    // @Value("${elastic.cluster.password:}")
    private String password;

    // @Value("${elastic.cluster.url:localhost}")
    private String url = "localhost";

    // @Value("${elastic.cluster.protocol:http}")
    private String protocol = "http";

    // @Value("${elastic.cluster.port:9200}")
    private int port = 9200;

    final public void setUser(String user) {
        this.user = user;
    }

    final public void setUrl(String url) {
        this.url = url;
    }

    final public void setPassword(String password) {
        this.password = password;
    }

    final public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    final public void setPort(int port) {
        this.port = port;
    }

    protected final String getUser() {
        return this.user;
    }

    protected final String getPassword() {
        return this.password;
    }

    protected final String getUrl() {
        return this.url;
    }

    protected final String getProtocol() {
        return this.protocol;
    }

    protected final int getPort() {
        return this.port;
    }
}
