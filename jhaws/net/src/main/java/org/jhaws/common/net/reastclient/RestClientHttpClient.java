package org.jhaws.common.net.reastclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RestClientHttpClient extends RestClientParent {
    private CloseableHttpClient client;

    @Resource
    private ObjectMapper objectMapper;

    public RestClientHttpClient() {
        super();
    }

    public RestClientHttpClient(Properties p) {
        super(p);
        init();
    }

    private static String getProperty(final String key) {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                return System.getProperty(key);
            }
        });
    }

    private static String[] getSystemCipherSuits() {
        return split(getProperty("https.cipherSuites"));
    }

    private static String[] split(final String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return s.split(" *, *");
    }

    @SuppressWarnings("resource")
    @PostConstruct
    protected void init() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(//
                RegistryBuilder.<ConnectionSocketFactory>create()//
                        .register("http", PlainConnectionSocketFactory.getSocketFactory()//
                        )//
                        .register("https", new SSLConnectionSocketFactory(//
                                SSLContexts.createSystemDefault()//
                                , new String[] { "TLSv1.3", "TLSv1.2" }//
                                , getSystemCipherSuits()//
                                , new DefaultHostnameVerifier()//
                        )//
                        )//
                        .build()//
        );
        poolingConnectionManager.setMaxTotal(50);
        poolingConnectionManager.setDefaultMaxPerRoute(10);

        @SuppressWarnings("unused")
        java.net.URL b;
        try {
            b = new java.net.URL(base);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        CredentialsProvider defaultCredentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
        defaultCredentialsProvider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder.create()//
                .setDefaultCookieStore(new BasicCookieStore()) //
                .setDefaultRequestConfig(RequestConfig//
                        .custom()//
                        .setMaxRedirects(3)//
                        .setCircularRedirectsAllowed(true)//
                        .setConnectionRequestTimeout(60_000)//
                        .setConnectTimeout(60_000)//
                        .setSocketTimeout(60_000)//
                        .setExpectContinueEnabled(true)//
                        .setRedirectsEnabled(true)//
                        .setCookieSpec("standard")//
                        .setContentCompressionEnabled(true)//
                        .build())//
                .setConnectionManager(poolingConnectionManager)//
                .setDefaultCredentialsProvider(defaultCredentialsProvider)//
                .build();
    }

    // ======================

    @Override
    protected <B, R> R get(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, null, elementTypeRef, HttpMethod.GET);
    }

    @Override
    protected <B, R> R post(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, bodyValue, elementTypeRef, HttpMethod.POST);
    }

    @Override
    protected <B, R> R put(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, bodyValue, elementTypeRef, HttpMethod.PUT);
    }

    @Override
    protected <B, R> R delete(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, null, elementTypeRef, HttpMethod.DELETE);
    }

    @SuppressWarnings("unchecked")
    protected <B, R> R call(MediaType accept, Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef, HttpMethod method) {
        URI uri = uri(uriFunction);
        HttpRequest request = null;
        switch (method) {
            case DELETE:
                request = new HttpDelete(uri);
                break;
            case GET:
                request = new HttpGet(uri);
                break;
            case HEAD:
                throw new IllegalArgumentException();
            case OPTIONS:
                throw new IllegalArgumentException();
            case PATCH:
                throw new IllegalArgumentException();
            case POST:
                request = new HttpPost(uri);
                break;
            case PUT:
                request = new HttpPut(uri);
                break;
            case TRACE:
                throw new IllegalArgumentException();
            default:
                throw new IllegalArgumentException();
        }
        HttpHost targetHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        HttpClientContext context = HttpClientContext.create();

        // AuthCache authCache = new BasicAuthCache();
        // BasicScheme basicAuth = new BasicScheme();
        // authCache.put(targetHost, basicAuth);
        // context.setAuthCache(authCache);

        System.out.println(accept.toString());
        request.setHeader(HttpHeaders.ACCEPT, accept.toString());
        // request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");

        if (bodyValue != null) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(64 * 1024 * 1024);) {
                objectMapper.writerFor(bodyValue.getClass()).writeValue(out, bodyValue);
                HttpEntityEnclosingRequest.class.cast(request).setEntity(new GzipCompressingEntity(new ByteArrayEntity(out.toByteArray(), ContentType.parse(accept.toString()))));
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        System.out.println(targetHost);
        System.out.println(request);
        System.out.println(context);
        HttpEntity entity = null;
        try (CloseableHttpResponse response = client.execute(targetHost, request, context)) {
            int status = response.getStatusLine().getStatusCode();
            if (200 <= status && status < 300) {
                entity = response.getEntity();
                if (entity != null && elementTypeRef != null) {
                    String json = null;
                    try {
                        json = EntityUtils.toString(entity);
                        if (elementTypeRef == RestConverter.STRING) {
                            return (R) json;
                        }
                        return objectMapper.readerFor(elementTypeRef.jackson()).readValue(json);
                    } catch (IOException ex) {
                        try {
                            System.out.println(json);
                        } catch (Exception ex2) {
                            //
                        }
                        throw ex;
                    }
                }
            } else {
                throw new RuntimeException(uri + " > " + response.getStatusLine());
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (Exception ex) {
                    //
                }
            }
        }

        return null;
    }
}
