package org.jhaws.common.elasticsearch8.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.jhaws.common.elasticsearch.common.ElasticDocument;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/getting-started-java.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/reading.html
// @Component
public class ElasticSuperClient extends ElasticLowLevelClient {

    protected transient AtomicReference<ElasticsearchClient> clientReference = new AtomicReference<>();

    @PostConstruct
    @Override
    public void afterPropertiesSet() {
        LOGGER.trace("startup->");
        super.afterPropertiesSet();
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(getUser(), getPassword()));
        RestClient restClient = RestClient.builder(HttpHost.create(getProtocol() + "://" + getUrl() + ":" + getPort())).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }).build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
        clientReference = new AtomicReference<>();
        clientReference.set(esClient);
        LOGGER.trace("<-startup");
    }

    @Override
    @PreDestroy
    public void shutdown() {
        LOGGER.trace("shutdown->");
        // Close the client
        if (clientReference.get() != null) {
            try {
                clientReference.get().close();
            } catch (Exception ex) {
                //
            }
        }
        LOGGER.trace("<-shutdown");
        super.shutdown();
    }

    protected RuntimeException handleIOException(IOException ex) {
        if (ex instanceof java.net.ConnectException) {
            return new ConnectionException(java.net.ConnectException.class.cast(ex));
        }
        return new UncheckedIOException(ex);
    }

    public ElasticsearchClient getClient() {
        synchronized (clientReference) {
            ElasticsearchClient instance = clientReference.get();
            if (instance == null) {
                afterPropertiesSet();
            }
            return clientReference.get();
        }
    }

    public Time getTimeout() {
        return new Time.Builder().time("1m").build();
    }

    public Time getScrollTimeout() {
        return new Time.Builder().time("5m").build();
    }

    public boolean ping() {
        try {
            return getClient().ping().value();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean indexExists(Class<T> type) {
        return indexExists(index(type));
    }

    public boolean indexExists(String index) {
        boolean exists;
        try {
            exists = getClient().indices().exists(new ExistsRequest.Builder().index(index).build()).value();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return exists;
    }

    public boolean deleteIndex(String index) {
        try {
            return getClient().indices().delete(new DeleteIndexRequest.Builder().index(index).timeout(getTimeout()).build()).acknowledged();
            // } catch (ElasticsearchStatusException ex) {
            // return false;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean deleteIndex(Class<T> type) {
        return deleteIndex(index(type));
    }

    public <T extends ElasticDocument> boolean createIndex(Class<T> annotatedType) {
        // FIXME
        return createIndex(index(annotatedType), null/* getObjectMapping(annotatedType) */, null/* settings() */);
    }

    public boolean createIndex(String indexName) {
        // FIXME
        return createIndex(indexName, Collections.emptyMap(), null/* settings() */);
    }

    public boolean createIndex(String indexName, Map<String, Object> mappings, Map<String, Object> settings) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-create-index.html
        try {
            // FIXME
            // Map<String, Object> config = new LinkedHashMap<>();
            //// if (settings != null) config.put(SETTINGS, settings);
            // if (mappings != null) config.put(MAPPINGS, mappings);

            // LOGGER.debug("\n{}", getObjectMapper().writerFor(Map.class).writeValueAsString(config));
            // request.source(config);

            return getClient().indices().create(new CreateIndexRequest.Builder().index(indexName).timeout(getTimeout()).build()).acknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> Map<String, Map<String, ?>> getIndexMapping(Class<T> type) {
        return getIndexMapping(index(type), allFields(type).toArray(l -> new String[l]));
    }

    public Map<String, Map<String, ?>> getIndexMapping(String index) {
        return getIndexMapping(index, null);
    }

    public <T extends ElasticDocument> List<String> allFields(Class<T> annotatedType) {
        List<String> fields = new ArrayList<>();
        getElasticCustomizer().getObjectMapping(annotatedType, (fullName, field, fieldMapping) -> fields.add(fullName));
        return fields;
    }

    public Map<String, Map<String, ?>> getIndexMapping(String index, String[] fields) {
        // FIXME
        // Map<String, Map<String, ?>> info = new TreeMap<>();
        // if (fields == null || fields.length == 0) {
        // GetMappingsRequest request = new GetMappingsRequest();
        // request.indices(index);
        // request.indicesOptions(IndicesOptions.lenientExpandOpen());
        // GetMappingsResponse response;
        // try {
        // response = getClient().indices().getMapping(request, getRequestOptions());
        // } catch (IOException ex) {
        // throw handleIOException(ex);
        // }
        // org.elasticsearch.cluster.metadata.MappingMetadata mappings = response.mappings().get(index);
        // Map<String, Map<String, ?>> properties = (Map<String, Map<String, ?>>) mappings.getSourceAsMap().get("properties");
        // info.putAll(properties);
        // } else {
        // // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-get-field-mappings.html
        // GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        // request.indices(index);
        // request.fields(fields);
        // request.indicesOptions(IndicesOptions.lenientExpandOpen());
        // GetFieldMappingsResponse response;
        // try {
        // response = getClient().indices().getFieldMapping(request, getRequestOptions());
        // } catch (IOException ex) {
        // throw handleIOException(ex);
        // }
        // Map<String, Map<String, org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata>> mappings = response.mappings();
        // Map<String, org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata> fieldMappings = mappings.get(index);
        // for (String field : fields) {
        // org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata metaData = fieldMappings.get(field);
        // if (metaData != null) {
        // info.put(metaData.fullName(), (Map<String, ?>) metaData.sourceAsMap().entrySet().iterator().next().getValue());
        // } else {
        // info.put(field, null);
        // }
        // }
        // }
        // return info;
        try {
            Map<String, Property> properties = getClient().indices().getMapping(getMappingRequestBuilder -> getMappingRequestBuilder.index(index)).get(index).mappings().properties();
            System.out.println(objectToJson(getObjectMapper(), properties));
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return null;
    }

    public <T extends ElasticDocument> boolean indexDocument(T document) {
        try {
            Result result = getClient().index(new IndexRequest.Builder<T>().index(index(document)).version(version(document)).id(id(document)).document(document).timeout(getTimeout()).build()).result();
            return result == Result.Created || result == Result.Updated;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean documentExists(T document) {
        return documentExists(document.getClass(), index(document), id(document), version(document));
    }

    public <T extends ElasticDocument> boolean documentExists(Class<T> type, String index, String id, Long version) {
        try {
            return getClient().get(g -> g.index(index).version(version).id(id), type).found();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ElasticDocument> T getDocument(T document) {
        return (T) getDocument(document.getClass(), index(document), id(document), version(document));
    }

    public <T extends ElasticDocument> T getDocument(Class<T> type, String index, String id, Long version) {
        try {
            return getClient().get(g -> g.index(index).version(version).id(id), type).source();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean deleteDocument(T document) {
        return deleteDocument(index(document), id(document), version(document));
    }

    public <T extends ElasticDocument> boolean deleteDocument(String index, String id, Long version) {
        try {
            return getClient().delete(new DeleteRequest.Builder().index(index).version(version).id(id).timeout(getTimeout()).build()).result() == Result.Deleted;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean createDocument(T document) {
        try {
            return Result.Created == getClient().index(new IndexRequest.Builder<T>().index(index(document)).version(version(document)).id(id(document)).opType(OpType.Create).timeout(getTimeout()).document(document).build()).result();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean updateDocument(T document) {
        try {
            if (!documentExists(document)) return false;
            return Result.Updated == getClient().index(new IndexRequest.Builder<T>().index(index(document)).version(version(document)).id(id(document)).document(document).timeout(getTimeout()).build()).result();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    // TODO bulk ...
}
