package org.jhaws.common.net.restclient;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.util.UriBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.tcp.TcpClient;

// https://www.baeldung.com/spring-5-webclient
// https://www.baeldung.com/webflux-webclient-parameters
// https://www.viralpatel.net/basic-authentication-spring-webclient/
public abstract class RestClientWebClient extends RestClientParent {
    private WebClient client;

    public RestClientWebClient() {
        super();
    }

    public RestClientWebClient(Properties p) {
        super(p);
        init();
    }

    @Value("${connectionTimeoutInSeconds:600}")
    protected int connectionTimeoutInSeconds = 600;

    @Value("${bufferSize:100000000}")
    protected int bufferSize = 100_000_000;

    @PostConstruct
    protected void init() {
        TcpClient tcpClient = TcpClient.create()//
                // .doOnConnect(connectMetrics)
                // .doOnConnected(connectedMetrics)
                // .doOnDisconnected(disconnectedMetrics)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, bufferSize * 1000)//
                .option(ChannelOption.AUTO_CLOSE, Boolean.TRUE)//
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(bufferSize, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(bufferSize, TimeUnit.MILLISECONDS));
                });

        reactor.netty.http.client.HttpClient hclient = reactor.netty.http.client.HttpClient.from(tcpClient);
        hclient.compress(true);
        hclient.disableRetry(false);
        hclient.keepAlive(true);

        client = WebClient//
                .builder()//
                .clientConnector(new ReactorClientHttpConnector(hclient))//
                .codecs((ClientCodecConfigurer configurer) -> {
                    configurer.defaultCodecs().maxInMemorySize(bufferSize);
                })//
                .baseUrl(base)//
                .defaultHeaders(header -> header.setBasicAuth(user, password))//
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)//
                .build();
    }

    @Override
    protected <B, R> R get(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, null, elementTypeRef, client.method(HttpMethod.GET));
    }

    @Override
    protected <B, R> R post(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, bodyValue, elementTypeRef, client.method(HttpMethod.POST));
    }

    @Override
    protected <B, R> R put(Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, bodyValue, elementTypeRef, client.method(HttpMethod.PUT));
    }

    @Override
    protected <B, R> R delete(Function<UriBuilder, URI> uriFunction, RestConverter<R> elementTypeRef) {
        return call(MediaType.APPLICATION_JSON, uriFunction, null, elementTypeRef, client.method(HttpMethod.DELETE));
    }

    @Value("${callTimeoutInSeconds:60}")
    protected int callTimeoutInSeconds = 60;

    @Resource
    protected ObjectMapper objectMapper;

    protected <B, R> R call(MediaType accept, Function<UriBuilder, URI> uriFunction, B bodyValue, RestConverter<R> elementTypeRef, RequestBodyUriSpec method) {
        RequestBodySpec requestBodySpec = method.uri(uriFunction).accept(accept);
        ResponseSpec responseSpec = Optional.ofNullable(bodyValue).map(bodyValueNotNull -> requestBodySpec.bodyValue(bodyValueNotNull).retrieve()).orElseGet(() -> requestBodySpec.retrieve())//
                .onStatus((HttpStatus httpStatus) -> httpStatus.value() == RestException.REST_API_SERVER_ERROR_STATUSCODE, (ClientResponse clientResponse) -> {
                    try {
                        throw clientResponse.bodyToMono(RestExceptionInfo.class).block(Duration.ofSeconds(callTimeoutInSeconds)).toRestException();
                    } catch (RestException ex) {
                        ex.printStackTrace(System.out);
                        throw ex;
                    } catch (RuntimeException ex) {
                        ex.printStackTrace(System.out);
                        throw new RestException(RestException.REST_API_SERVER_ERROR_STATUSCODE + ": " + ex);
                    }
                });
        try {
            return Optional.ofNullable(elementTypeRef).map(elementTypeRefNotNull -> responseSpec//
                    .bodyToMono(elementTypeRefNotNull.springweb()).block(Duration.ofMinutes(10))).orElse(null);
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException ex) {
            if (ex.getRawStatusCode() == RestException.REST_API_SERVER_ERROR_STATUSCODE) {
                try {
                    throw objectMapper.readValue(ex.getResponseBodyAsString().getBytes("utf-8"), RestExceptionInfo.class).toRestException();
                } catch (IOException ioex) {
                    //
                }
            }
            ex.printStackTrace(System.out);
            System.out.println(ex.getRawStatusCode());
            System.out.println(ex.getResponseBodyAsString());
            System.out.println(ex.getStatusText());
            System.out.println(ex.getStatusCode());
            throw ex;
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        }
    }
}
