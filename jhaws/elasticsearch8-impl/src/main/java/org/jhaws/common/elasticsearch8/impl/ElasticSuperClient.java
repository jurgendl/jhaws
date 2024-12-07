package org.jhaws.common.elasticsearch8.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.mget.MultiGetOperation;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettingsAnalysis;
import co.elastic.clients.elasticsearch.indices.SettingsHighlight;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/getting-started-java.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/reading.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-indices-put-settings.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-create-index.html
// @Component
public class ElasticSuperClient extends ElasticLowLevelClient {
    protected transient AtomicReference<ElasticsearchClient> clientReference = new AtomicReference<>();

    protected <T> List<T> toList(T item, @SuppressWarnings("unchecked") T... items) {
        List<T> list = new ArrayList<>();
        list.add(item);
        if (items != null) Arrays.stream(items).forEach(list::add);
        return list;
    }

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
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean deleteIndex(Class<T> type) {
        try {
            return deleteIndex(index(type));
        } catch (co.elastic.clients.elasticsearch._types.ElasticsearchException ex) {
            return false;
        }
    }

    public <T extends ElasticDocument> Map<String, Object> getObjectMapping(Class<T> annotatedType) {
        return getObjectMapping(annotatedType, MappingListener.DUMMY);
    }

    public <T extends ElasticDocument> Map<String, Object> getObjectMapping(Class<T> annotatedType, MappingListener listener) {
        return getElasticCustomizer().getObjectMapping(annotatedType, listener);
    }

    public <T extends ElasticDocument> boolean createIndex(Class<T> annotatedType) {
        return createIndex(index(annotatedType), //
                getObjectMapping(annotatedType), //
                getElasticCustomizer().charFilters(), //
                getElasticCustomizer().tokenFilters(), //
                getElasticCustomizer().tokenizers(), //
                getElasticCustomizer().normalizers(), //
                getElasticCustomizer().analyzers()//
        );
    }

    public boolean createIndex(String indexName) {
        return createIndex(indexName, //
                Collections.emptyMap(), //
                getElasticCustomizer().charFilters(), //
                getElasticCustomizer().tokenFilters(), //
                getElasticCustomizer().tokenizers(), //
                getElasticCustomizer().normalizers(), //
                getElasticCustomizer().analyzers()//
        );
    }

    public boolean createIndex(String indexName, //
            Map<String, Object> objectMapping, //
            Map<String, co.elastic.clients.elasticsearch._types.analysis.CharFilter> charFilters, //
            Map<String, co.elastic.clients.elasticsearch._types.analysis.TokenFilter> tokenFilters, //
            Map<String, co.elastic.clients.elasticsearch._types.analysis.Tokenizer> tokenizers, //
            Map<String, co.elastic.clients.elasticsearch._types.analysis.Normalizer> normalizers, //
            Map<String, co.elastic.clients.elasticsearch._types.analysis.Analyzer> analyzers) {
        try {
            System.out.println(objectMapping);// FIXME
            IndexSettingsAnalysis indexSettingsAnalysis = new IndexSettingsAnalysis.Builder()//
                    .charFilter(charFilters)//
                    .tokenizer(tokenizers)//
                    .filter(tokenFilters)//
                    .analyzer(analyzers)//
                    .normalizer(normalizers)//
                    .build();
            SettingsHighlight settingsHighlight = new SettingsHighlight.Builder().maxAnalyzedOffset(getElasticCustomizer().getHighlightMaxAnalyzedOffset()).build();
            IndexSettings indexSettings = new IndexSettings.Builder().maxResultWindow(getElasticCustomizer().getMaxResultWindow()).analysis(indexSettingsAnalysis).highlight(settingsHighlight).build();
            return getClient().indices().create(new CreateIndexRequest.Builder().index(indexName).settings(indexSettings).timeout(getTimeout()).build()).acknowledged();
        } catch (co.elastic.clients.elasticsearch._types.ElasticsearchException ex) {
            return false;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> Map<String, Map<String, ?>> getIndexMapping(Class<T> type) {
        return getIndexMapping(index(type), allFields(type));
    }

    public Map<String, Map<String, ?>> getIndexMapping(String index) {
        return getIndexMapping(index, null);
    }

    public <T extends ElasticDocument> List<String> allFields(Class<T> annotatedType) {
        List<String> fields = new ArrayList<>();
        getElasticCustomizer().getObjectMapping(annotatedType, (fullName, field, fieldMapping) -> fields.add(fullName));
        return fields;
    }

    public Map<String, Map<String, ?>> getIndexMapping(String index, List<String> includeFields) {
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
            return Arrays.asList(Result.Created, Result.Updated).contains(getClient().index(new IndexRequest.Builder<T>().index(index(document)).version(version(document)).id(id(document)).document(document).timeout(getTimeout()).build()).result());
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean documentExists(T document) {
        return documentExists(document.getClass(), id(document), version(document));
    }

    public <T extends ElasticDocument> boolean documentExists(Class<T> type, String id, Long version) {
        try {
            return getClient().get(getRequestBuilder -> getRequestBuilder.index(index(type)).version(version).id(id), type).found();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ElasticDocument> T getDocument(T document) {
        return (T) getDocument(document.getClass(), null, id(document), version(document));
    }

    public <T extends ElasticDocument> T getDocument(Class<T> type, List<String> includeFields, String id, Long version) {
        try {
            return getClient().get(getRequestBuilder -> {
                getRequestBuilder.index(index(type)).version(version).id(id);
                if (includeFields != null) getRequestBuilder.storedFields(includeFields);
                return getRequestBuilder;
            }, type).source();
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

    public <T extends ElasticDocument> List<T> multiGetDocument(Class<T> type, List<String> includeFields, String id, String... ids) {
        return multiGetDocument(type, includeFields, toList(id, ids));
    }

    public <T extends ElasticDocument> List<T> multiGetDocument(Class<T> type, List<String> includeFields, List<String> ids) {
        try {
            MgetRequest.Builder requestBuilder = new MgetRequest.Builder().index(index(type));
            ids.forEach(id -> {
                MultiGetOperation.Builder multiGetOperationBuilder = new MultiGetOperation.Builder().id(id);
                if (includeFields != null) multiGetOperationBuilder.storedFields(includeFields);
                requestBuilder.docs(multiGetOperationBuilder.build());
            });
            return getClient().mget(requestBuilder.build(), type).docs().stream().map(item -> item.result().source()).filter(Objects::nonNull).toList();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> List<ErrorCause> multiDeleteDocument(Class<T> type, String id, String... ids) {
        return multiDeleteDocument(type, toList(id, ids));
    }

    public <T extends ElasticDocument> List<ErrorCause> multiDeleteDocument(Class<T> type, List<String> ids) {
        try {
            return getClient().bulk(ids.stream()
                    .reduce(new BulkRequest.Builder(), (BulkRequest.Builder bulkRequestBuilder, String id) -> bulkRequestBuilder.operations(new BulkOperation.Builder().delete(new DeleteOperation.Builder().index(index(type)).id(id).build()).build()),
                            (BulkRequest.Builder bulkRequestBuilder, BulkRequest.Builder ignore) -> bulkRequestBuilder)
                    .timeout(getScrollTimeout()).build()).items().stream().map(item -> item.error()).filter(Objects::nonNull).toList();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> List<ErrorCause> multiIndexDocument(T document, @SuppressWarnings("unchecked") T... documents) {
        return multiIndexDocument(toList(document, documents));
    }

    public <T extends ElasticDocument> List<ErrorCause> multiIndexDocument(Collection<T> documents) {
        try {
            return getClient().bulk(documents.stream()
                    .reduce(new BulkRequest.Builder(),
                            (BulkRequest.Builder bulkRequestBuilder, T document) -> bulkRequestBuilder
                                    .operations(new BulkOperation.Builder().index(new IndexOperation.Builder<T>().index(index(document)).id(id(document)).version(version(document)).document(document).build()).build()),
                            (BulkRequest.Builder bulkRequestBuilder, BulkRequest.Builder ignore) -> bulkRequestBuilder)
                    .timeout(getScrollTimeout()).build()).items().stream().map(item -> item.error()).filter(Objects::nonNull).toList();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> void query(Class<T> type, String field, String searchText) {
        try {
            // https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/searching.html

            Query q = new Query.Builder().queryString(new QueryStringQuery.Builder().fields(field).query(searchText).build()).build();
            SearchRequest sq = new SearchRequest.Builder().index(index(type)).query(q).build();
            System.out.println("q=" + q);
            SearchResponse<T> response = getClient().search(sq, type);
            // getClient().search(s -> s.index(index(type))//
            // .query(q -> q.match(t -> t.field(field).query(searchText))), type);
            TotalHits total = response.hits().total();
            System.out.println(total);
            boolean isExactResult = total.relation() == TotalHitsRelation.Eq;
            if (isExactResult) {
                System.out.println("There are " + total.value() + " results");
            } else {
                System.out.println("There are more than " + total.value() + " results");
            }
            List<Hit<T>> hits = response.hits().hits();
            for (Hit<T> hit : hits) {
                T item = hit.source();
                System.out.println(item);
            }
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }
}
