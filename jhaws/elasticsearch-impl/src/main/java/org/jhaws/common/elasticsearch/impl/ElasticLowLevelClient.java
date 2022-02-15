package org.jhaws.common.elasticsearch.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

// curl -XGET --user elastic:??? "http://localhost:9200/_cluster/stats?pretty"
// curl -XGET --user elastic:??? "http://localhost:9200/_cluster/settings"
// curl -XGET --user elastic:??? "http://localhost:9200/_nodes/stats?pretty"
// curl -XGET --user elastic:??? "http://localhost:9200/INDEX/stats?pretty"
// curl -XGET --user elastic:??? "http://localhost:9200/INDEX/_mapping"
// curl -XGET --user elastic:??? "http://localhost:9200/INDEX/_mapping/settings"
// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
public class ElasticLowLevelClient extends ElasticConfig implements InitializingBean {
    protected final Logger LOGGER;

    @Autowired(required = false)
    protected ObjectMapper objectMapper;

    @Autowired(required = false)
    protected ElasticCustomizer elasticCustomizer;

    protected transient AtomicReference<CloseableHttpClient> httpClientReference;

    protected transient ThreadLocal<HttpClientContext> httpClientContextReference;

    public ElasticLowLevelClient() {
        LOGGER = LoggerFactory.getLogger(getClass());
    }

    @PostConstruct
    @Override
    public void afterPropertiesSet() {
        LOGGER.trace("startup->");
        httpClient();
        LOGGER.trace("<-startup");
    }

    @PreDestroy
    public void shutdown() {
        LOGGER.trace("shutdown->");
        if (httpClientReference.get() != null) {
            try {
                httpClientReference.get().close();
            } catch (Exception ex) {
                //
            }
        }
        LOGGER.trace("<-shutdown");
    }

    protected void httpClient() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpHost target = new HttpHost(getProtocol(), getUrl().split(",")[0], getPort());
        httpClientReference = new AtomicReference<>();
        httpClientReference.set(httpclient);
        httpClientContextReference = new ThreadLocal<HttpClientContext>() {
            @Override
            protected HttpClientContext initialValue() {
                HttpClientContext localContext = HttpClientContext.create();
                BasicScheme basicAuth = new BasicScheme();
                basicAuth.initPreemptive(new UsernamePasswordCredentials(getUser(), getPassword().toCharArray()));
                localContext.resetAuthExchange(target, basicAuth);
                return localContext;
            }
        };
    }

    protected byte[] _execute(HttpUriRequestBase request) {
        try {
            return httpClientReference.get().execute(request, httpClientContextReference.get(), this::_response);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected <R extends HttpResponse & HttpEntityContainer> byte[] _response(R response) throws ClientProtocolException {
        LOGGER.debug("status: {}", new StatusLine(response));
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity == null) {
            return null;
        }
        Arrays.stream(response.getHeaders()).forEach(h -> LOGGER.trace("{}", h));
        byte[] entity;
        try {
            entity = EntityUtils.toByteArray(response.getEntity());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        if (!(200 <= response.getCode() && response.getCode() < 300)) {
            try {
                String stringResponse = new String(entity, StandardCharsets.UTF_8.toString());
                try {
                    stringResponse = objectToJson(getObjectMapper(), jsonToObject(getObjectMapper(), Map.class, stringResponse));
                } catch (Exception ex) {
                    //
                }
                throw new ClientProtocolException(new StatusLine(response).toString(), new RuntimeException("\n" + stringResponse));
            } catch (ClientProtocolException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new ClientProtocolException(new StatusLine(response).toString());
            }
        }
        return entity;

    }

    public String _info() {
        try {
            String uri = getProtocol() + "://" + getUrl().split(",")[0] + ":" + getPort() + "/";
            LOGGER.debug(uri);
            return new String(_execute(new HttpGet(uri)), StandardCharsets.UTF_8.toString());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /** !!! ASYNCHROON !!! */
    public void _bulk(String index, InputStream json) {
        String uri = getProtocol() + "://" + getUrl().split(",")[0] + ":" + getPort() + "/" + index + "/_bulk";
        LOGGER.debug("uri: {}", uri);
        HttpPost request = new HttpPost(uri);
        request.setEntity(jsonEntity(json));
        _execute(request);
        waitABit();
    }

    protected InputStreamEntity jsonEntity(InputStream json) {
        // request.setHeader("Accept", "application/json");
        // request.setHeader("Content-type", "application/json");
        try {
            return new InputStreamEntity(json, json.available(), ContentType.APPLICATION_JSON, StandardCharsets.UTF_8.toString());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected StringEntity jsonEntity(String json) {
        // request.setHeader("Accept", "application/json");
        // request.setHeader("Content-type", "application/json");
        return new StringEntity(json, ContentType.APPLICATION_JSON, StandardCharsets.UTF_8.toString(), false);
    }

    protected <T> StringEntity jsonEntity(T object) {
        return jsonEntity(objectToJson(getObjectMapper(), object));
    }

    protected void waitABit() {
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException ex) {
            //
        }
    }

    protected boolean _customCharacterFilter(String index, String characterFilterName, Map<String, Object> characterFilterConfig) {
        return _analysisConfig(index, Collections.singletonMap("char_filter", Collections.singletonMap(characterFilterName, characterFilterConfig)));
    }

    protected boolean _customTokenizer(String index, String tokenizerName, Map<String, Object> tokenizerConfig) {
        return _analysisConfig(index, Collections.singletonMap("tokenizer", Collections.singletonMap(tokenizerName, tokenizerConfig)));
    }

    protected boolean _customFilter(String index, String filterName, Map<String, Object> filterConfig) {
        return _analysisConfig(index, Collections.singletonMap("filter", Collections.singletonMap(filterName, filterConfig)));
    }

    protected boolean _customAnalyzer(String index, String analyzerName, Map<String, Object> analyzerConfig) {
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-custom-analyzer.html
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-update-settings.html
        // https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-elision-tokenfilter.html
        analyzerConfig.put("type", "custom");
        return _analysisConfig(index, Collections.singletonMap("analyzer", Collections.singletonMap(analyzerName, analyzerConfig)));
    }

    public boolean _analysisConfig(String index, Map<String, Object> analysis) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("analysis", analysis);
        return _setIndexSettings(index, settings);
    }

    public boolean _indexHighlightMaxAnalyzedOffset(String index) {
        Map<String, Object> indexSettings = new LinkedHashMap<>();
        indexSettings.put(ElasticCustomizer.INDEX_SETTINGS_HIGHLIGHT_MAX_ANALYZED_OFFSET, getElasticCustomizer().getHighlightMaxAnalyzedOffset());
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("index", indexSettings);
        return _setIndexSettings(index, settings);
    }

    @SuppressWarnings("serial")
    public ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper(new JsonFactory()) {
                {
                    registerModule(new com.fasterxml.jackson.datatype.jsr353.JSR353Module());
                    registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                    registerModule(new com.fasterxml.jackson.datatype.joda.JodaModule());
                    registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());

                    setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

                    configure(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                    configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                    configure(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT, true);

                    configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    configure(com.fasterxml.jackson.core.JsonGenerator.Feature.ESCAPE_NON_ASCII, true);

                    setVisibility(getSerializationConfig().getDefaultVisibilityChecker()//
                            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)//
                            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)//
                            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)//
                            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));//
                }
            };
        }
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ElasticCustomizer getElasticCustomizer() {
        if (elasticCustomizer == null) {
            elasticCustomizer = new ElasticCustomizer();
        }
        return elasticCustomizer;
    }

    public void setElasticCustomizer(ElasticCustomizer elasticCustomizer) {
        this.elasticCustomizer = elasticCustomizer;
    }

    protected boolean _setIndexReadOnly(String index, Boolean readOnly) {
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put(ElasticCustomizer.INDEX_SETTINGS__BLOCKS_READ_ONLY, Boolean.TRUE.equals(readOnly));
        return _setIndexSettings(index, settings);
    }

    private boolean _setIndexSettings(String index, Map<String, Object> settings) {
        try {
            String json = ElasticHelper.objectToJson(getObjectMapper(), settings);
            String uri = getProtocol() + "://" + getUrl().split(",")[0] + ":" + getPort() + "/" + index + "/_settings";
            LOGGER.debug("uri: {}", uri);
            LOGGER.debug("json: {}", json);
            HttpPut put = new HttpPut(uri);
            put.setEntity(jsonEntity(json));
            byte[] result = _execute(put);
            @SuppressWarnings("unchecked")
            Map<String, Object> map = jsonToObject(getObjectMapper(), Map.class, new String(result, StandardCharsets.UTF_8.toString()));
            LOGGER.debug("result: {}", map);
            boolean success = map != null && ("true".equals(map.get("acknowledged")) || Boolean.TRUE.equals(map.get("acknowledged")));
            return success;
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            throw new UncheckedIOException(ex);
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.out);
            throw ex;
        } finally {
            waitABit();
        }
    }
}
