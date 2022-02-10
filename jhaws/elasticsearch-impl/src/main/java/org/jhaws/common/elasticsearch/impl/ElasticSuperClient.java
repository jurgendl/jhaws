package org.jhaws.common.elasticsearch.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.TotalHits.Relation;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesRequest;
import org.elasticsearch.action.admin.cluster.repositories.get.GetRepositoriesResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsRequest;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsRequest;
import org.elasticsearch.action.admin.cluster.snapshots.get.GetSnapshotsResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.explain.ExplainResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.CloseIndexRequest;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetFieldMappingsRequest;
import org.elasticsearch.client.indices.GetFieldMappingsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.client.xpack.XPackInfoRequest;
import org.elasticsearch.client.xpack.XPackInfoResponse;
import org.elasticsearch.client.xpack.XPackInfoResponse.BuildInfo;
import org.elasticsearch.client.xpack.XPackInfoResponse.FeatureSetsInfo;
import org.elasticsearch.client.xpack.XPackInfoResponse.LicenseInfo;
import org.elasticsearch.client.xpack.XPackUsageRequest;
import org.elasticsearch.client.xpack.XPackUsageResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.repositories.fs.FsRepository;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.ParsedGlobal;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ExtendedStats.Bounds;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedExtendedStats;
import org.elasticsearch.search.aggregations.metrics.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.Percentile;
import org.elasticsearch.search.aggregations.metrics.PercentileRanks;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.snapshots.SnapshotInfo;
import org.jhaws.common.elasticsearch.common.Analyzer;
import org.jhaws.common.elasticsearch.common.Analyzers;
import org.jhaws.common.elasticsearch.common.ElasticDocument;
import org.jhaws.common.elasticsearch.common.FieldType;
import org.jhaws.common.elasticsearch.common.Pagination;

// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html
// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-query-builders.html
//
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-range-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-match-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-term-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-fuzzy-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-regexp-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-prefix-query.html
//
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-filter-context.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-constant-score-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-boosting-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-dis-max-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-function-score-query.html
//
// https://www.elastic.co/guide/en/elasticsearch/reference/master/full-text-queries.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-intervals-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-match-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-match-bool-prefix-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-match-query-phrase.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-match-query-phrase-prefix.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-multi-match-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-query-string-query.html
// https://www.elastic.co/guide/en/elasticsearch/reference/master/query-dsl-simple-query-string-query.html
//
// https://stackoverflow.com/questions/37513634/what-is-the-significance-of-doc-count-error-upper-bound-in-elasticsearch-and-how
// https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations-bucket-terms-aggregation.html#_calculating_document_count_error
//
//
// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-index.html
//
// TODO https://artifacts.elastic.co/javadoc/org/elasticsearch/client/elasticsearch-rest-client/7.6.0/org/elasticsearch/client/package-summary.html
// https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-pattern-replace-charfilter.html
// https://www.elastic.co/guide/en/elasticsearch/reference/7.7/search-request-body.html#request-body-search-scroll
//
// https://stackoverflow.com/questions/9075098/start-windows-service-from-java
// @Component
public class ElasticSuperClient extends ElasticLowLevelClient {
    public static final String _NONE_ = "_none_";

    public static final String PROCEED = "proceed";

    public static final String MONITORING = "monitoring";

    public static final String MAPPINGS = "mappings";

    public static final String SETTINGS = "settings";

    protected transient AtomicReference<RestHighLevelClient> clientReference = new AtomicReference<>();

    public ElasticSuperClient() {
        super();
    }

    @PostConstruct
    @Override
    public void afterPropertiesSet() {
        LOGGER.trace("startup->");
        super.afterPropertiesSet();
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_basic_authentication.html
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(getUser(), getPassword()));
        HttpHost[] array = Arrays.stream(getUrl().split(",")).map(u -> new HttpHost(u, getPort(), getProtocol())).toArray(l -> new HttpHost[l]);
        RestClientBuilder restClientBuilder = RestClient.builder(array)//
                // https://discuss.elastic.co/t/how-to-avoid-30-000ms-timeout-during-reindexing/231370/2
                // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_timeouts.html
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder//
                        .setConnectTimeout(10_000)// 10s (defo 1)
                        .setSocketTimeout(120_000)// 120s (defo 30)
                )// https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_basic_authentication.html
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder//
                        .setDefaultCredentialsProvider(credentialsProvider)//
                );
        clientReference = new AtomicReference<>();
        clientReference.set(new RestHighLevelClient(restClientBuilder));
        LOGGER.trace("<-startup");

        // TODO
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_other_authentication_methods.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_number_of_threads.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_encrypted_communication.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_node_selector.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/sniffer.html
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

    public RestHighLevelClient getClient() {
        synchronized (clientReference) {
            RestHighLevelClient instance = clientReference.get();
            if (instance == null) {
                afterPropertiesSet();
            }
            return clientReference.get();
        }
    }

    public TimeValue getTimeout() {
        return TimeValue.timeValueMinutes(1);
    }

    public TimeValue getScrollTimeout() {
        return TimeValue.timeValueMinutes(5);
    }

    public boolean ping() {
        try {
            return getClient().ping(RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean indexExists(Class<T> type) {
        return indexExists(index(type));
    }

    public boolean indexExists(String index) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-indices-exists.html
        GetIndexRequest request = new GetIndexRequest(index);
        // request.local(false);
        // request.humanReadable(true);
        // request.includeDefaults(false);
        // request.indicesOptions(indicesOptions);
        boolean exists;
        try {
            exists = getClient().indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return exists;
    }

    public boolean deleteIndex(String index) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-delete-index.html
        try {
            return getClient().indices().delete(new DeleteIndexRequest(index).timeout(getTimeout()), RequestOptions.DEFAULT).isAcknowledged();
        } catch (ElasticsearchStatusException ex) {
            return false;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean createIndex(Class<T> annotatedType) {
        return createIndex(index(annotatedType), getObjectMapping(annotatedType), settings());
    }

    public boolean createIndex(String indexName) {
        return createIndex(indexName, Collections.emptyMap(), settings());
    }

    public boolean createIndex(String indexName, Map<String, Object> mappings, Map<String, Object> settings) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-create-index.html
        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);

            Map<String, Object> config = new LinkedHashMap<>();
            if (settings != null) config.put(SETTINGS, settings);
            if (mappings != null) config.put(MAPPINGS, mappings);

            LOGGER.info("\n{}", getObjectMapper().writerFor(Map.class).writeValueAsString(config));
            request.source(config);

            request.setTimeout(getTimeout());

            CreateIndexResponse response = getClient().indices().create(request, RequestOptions.DEFAULT);
            return response.isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> Map<String, Map<String, ?>> getIndexMapping(Class<T> type) {
        return getIndexMapping(index(type), allFields(type).toArray(l -> new String[l]));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<String, ?>> getIndexMapping(String index, String[] fields) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-get-field-mappings.html
        GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        request.indices(index);
        if (fields != null && fields.length > 0) {
            request.fields(fields);
        } else {
            throw new IllegalArgumentException("fields");
        }
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        GetFieldMappingsResponse response;
        try {
            response = getClient().indices().getFieldMapping(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }

        Map<String, Map<String, org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata>> mappings = response.mappings();
        Map<String, org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata> fieldMappings = mappings.get(index);
        Map<String, Map<String, ?>> info = new TreeMap<>();
        for (String field : fields) {
            org.elasticsearch.client.indices.GetFieldMappingsResponse.FieldMappingMetadata metaData = fieldMappings.get(field);
            if (metaData != null) {
                info.put(metaData.fullName(), (Map<String, ?>) metaData.sourceAsMap().entrySet().iterator().next().getValue());
            } else {
                info.put(field, null);
            }
        }
        return info;
    }

    public void indexDocument(String index, String id, String jsonString) {
        performIndexRequest(createIndexRequest(index, id, jsonString));
    }

    public IndexRequest createIndexRequest(String index, String id, String jsonString) {
        return new IndexRequest(index).id(id).source(jsonString, XContentType.JSON);
    }

    public void indexDocument(String index, String id, Map<String, Object> jsonMap) {
        performIndexRequest(createIndexRequest(index, id, jsonMap));
    }

    public IndexRequest createIndexRequest(String index, String id, Map<String, Object> jsonMap) {
        return new IndexRequest(index).id(id).source(jsonMap);
    }

    public void indexDocument(String index, String id, Consumer<XContentBuilder> build) {
        performIndexRequest(createIndexRequest(index, id, build));
    }

    public IndexRequest createIndexRequest(String index, String id, Consumer<XContentBuilder> build) {
        return new IndexRequest(index).id(id).source(build(build));
    }

    public <T extends ElasticDocument> T initialize(T o) {
        ElasticHelper.initializer(o).ifPresent(method -> {
            try {
                method.invoke(o);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });
        return o;
    }

    public <T extends ElasticDocument> IndexRequest createIndexRequest(T o) {
        return new IndexRequest(index(o)).id(id(o)).source(toMap(getObjectMapper(), initialize(o)));
    }

    public <T extends ElasticDocument> void indexDocument(T o) {
        performIndexRequest(createIndexRequest(o));
    }

    public void performIndexRequest(IndexRequest request) {
        request.timeout(getTimeout());
        IndexResponse response;
        try {
            response = getClient().index(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        handleIndexResponse(response);
    }

    public void handleIndexResponse(IndexResponse response) {
        handleDocWriteResponse(response);
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            // LOGGER.debug(shardInfo.getTotal() + "!=" + shardInfo.getSuccessful());
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
                LOGGER.debug(reason);
            }
        }
    }

    public <T extends ElasticDocument> boolean documentExists(T o) {
        return documentExists(index(o), id(o), version(o));
    }

    public boolean documentExists(String index, String id, Long version) {
        GetRequest request = new GetRequest(index, id);
        if (version != null) {
            request.version(version);
        }
        request.fetchSourceContext(fetchNone());
        request.storedFields(_NONE_);
        return performGetRequest(request) != null;
    }

    public Map<String, Object> getDocument(String index, String id) {
        return getDocument(index, id, null, null, null);
    }

    public Map<String, Object> getDocument(String index, String id, Long version, String[] includes, String[] excludes) {
        return performGetRequest(createGetRequest(index, id, version, includes, excludes));
    }

    public GetRequest createGetRequest(String index, String id, Long version, String[] includes, String[] excludes) {
        GetRequest request = new GetRequest(index, id);
        if (version != null) {
            request.version(version);
        }
        request.fetchSourceContext(fetch(true, includes, excludes));
        return request;
    }

    public FetchSourceContext fetchNone() {
        return FetchSourceContext.DO_NOT_FETCH_SOURCE;
    }

    public FetchSourceContext fetchAll() {
        return FetchSourceContext.FETCH_SOURCE;
    }

    public FetchSourceContext fetch(boolean source, String[] includes, String[] excludes) {
        return includes == null && excludes == null ? fetchAll() : //
                new FetchSourceContext(source//
                        , Optional.ofNullable(includes).orElse(new String[0])//
                        , Optional.ofNullable(excludes).orElse(new String[0]));
    }

    public Map<String, Object> performGetRequest(GetRequest request) {
        GetResponse response = null;
        try {
            response = getClient().get(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            // bij bv timeout
            throw handleIOException(ex);
        } catch (ElasticsearchException e) {
            handleElasticSearchException(e);
            throw e;
        }
        return handleGetResponse(response);
    }

    public void handleElasticSearchException(ElasticsearchException e) {
        if (e.status() == RestStatus.NOT_FOUND) {
            LOGGER.debug("index does not exists");
        }
        if (e.status() == RestStatus.CONFLICT) {
            LOGGER.debug("gevraagde versie van document bestaat niet");
        }
    }

    public Map<String, Object> handleGetResponse(GetResponse response) {
        if (response.isExists()) {
            // long version = getResponse.getVersion();
            // LOGGER.debug(version);
            // String sourceAsString = getResponse.getSourceAsString();
            // LOGGER.debug(sourceAsString);
            Map<String, Object> sourceAsMap = response.getSourceAsMap();
            return sourceAsMap;
            // byte[] sourceAsBytes = getResponse.getSourceAsBytes();
            // LOGGER.debug(sourceAsBytes);
        }
        LOGGER.debug("NOT EXISTS");
        return null;
    }

    public void deleteDocument(String index, String id, Long version) {
        performDeleteRequest(createDeleteRequest(index, id, version));
    }

    public DeleteRequest createDeleteRequest(String index, String id, Long version) {
        DeleteRequest request = new DeleteRequest(index, id);
        if (version != null) {
            request.version(version);
        }
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        return request;
    }

    public <T extends ElasticDocument> void deleteDocument(T o) {
        performDeleteRequest(createDeleteRequest(o));
    }

    public <T extends ElasticDocument> DeleteRequest createDeleteRequest(T o) {
        return createDeleteRequest(index(o), id(o), version(o));
    }

    public void performDeleteRequest(DeleteRequest request) {
        request.timeout(getTimeout());
        DeleteResponse response;
        try {
            response = getClient().delete(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        handleDeleteResponse(response);
    }

    public void handleDeleteResponse(DeleteResponse response) {
        handleDocWriteResponse(response);
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            // LOGGER.debug(shardInfo.getTotal() + "!=" + shardInfo.getSuccessful());
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
                LOGGER.debug(reason);
            }
        }
    }

    public void handleDocWriteResponse(DocWriteResponse response) {
        LOGGER.trace("result: {}", response.getResult());
    }

    public void updateDocument(String index, String id, String script, Map<String, Object> params) {
        performUpdateRequest(createUpdateRequest(index, id, script, params));
    }

    public UpdateRequest createUpdateRequest(String index, String id, String script, Map<String, Object> params) {
        return new UpdateRequest(index, id).script(script(script, params));
    }

    public Script script(String script, Map<String, Object> params) {
        return new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, script, params == null ? Collections.emptyMap() : params);
    }

    public void performUpdateRequest(UpdateRequest request) {
        request.retryOnConflict(3);
        {
            // request.fetchSource(true);
            // request.fetchSourceContext(createFetchSourceContext(includes, excludes));
        }
        UpdateResponse response;
        try {
            response = getClient().update(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        handleUpdateResponse(response);
    }

    public void handleUpdateResponse(UpdateResponse response) {
        handleDocWriteResponse(response);
        ReplicationResponse.ShardInfo shardInfo = response.getShardInfo();
        if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            // LOGGER.debug(shardInfo.getTotal() + "!=" + shardInfo.getSuccessful());
        }
        if (shardInfo.getFailed() > 0) {
            for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                String reason = failure.reason();
                LOGGER.debug(reason);
            }
        }
    }

    public void updateDocument(String index, String id, Map<String, Object> jsonMap) {
        performUpdateRequest(createUpdateRequest(index, id, jsonMap));
    }

    public UpdateRequest createUpdateRequest(String index, String id, Map<String, Object> jsonMap) {
        return new UpdateRequest(index, id).doc(jsonMap);
    }

    public void updateDocument(String index, String id, String jsonString) {
        performUpdateRequest(createUpdateRequest(index, id, jsonString));
    }

    public UpdateRequest createUpdateRequest(String index, String id, String jsonString) {
        return new UpdateRequest(index, id).id(id).doc(jsonString, XContentType.JSON);
    }

    public void updateDocument(String index, String id, Consumer<XContentBuilder> build) {
        performUpdateRequest(createUpdateRequest(index, id, build));
    }

    public UpdateRequest createUpdateRequest(String index, String id, Consumer<XContentBuilder> build) {
        return new UpdateRequest(index, id).doc(build(build));
    }

    public XContentBuilder build(Consumer<XContentBuilder> build) {
        try {
            return unaryOperator(build).apply(XContentFactory.jsonBuilder().startObject()).endObject();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> void updateDocument(T o) {
        updateDocument(index(o), id(o), toMap(getObjectMapper(), o));
    }

    @SuppressWarnings("unchecked")
    public <T extends ElasticDocument> T getDocument(T o) {
        return (T) getDocument(Class.class.cast(o.getClass()), o);
    }

    public <T extends ElasticDocument> T getDocument(Class<T> type, T o) {
        GetRequest request = createGetRequest(o);
        GetResponse response = null;
        try {
            response = getClient().get(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            // bij bv timeout
            throw handleIOException(ex);
        } catch (ElasticsearchException e) {
            handleElasticSearchException(e);
            throw e;
        }
        return toObject(getObjectMapper(), type, response);
    }

    public <T extends ElasticDocument> GetRequest createGetRequest(T o) {
        return createGetRequest(index(o), id(o), version(o), null, null);
    }

    public void bulk(String index, @SuppressWarnings("rawtypes") List<DocWriteRequest> requests) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-bulk.html
        BulkRequest request = new BulkRequest(index);
        requests.forEach(r -> request.add(r));
        request.timeout(getTimeout());
        requests.stream()
                .filter(req -> req instanceof UpdateRequest)
                .map(UpdateRequest.class::cast)
                .forEach(req -> req.setRefreshPolicy(UpdateRequest.RefreshPolicy.NONE));
        requests.stream()
                .filter(req -> req instanceof DeleteRequest)
                .map(DeleteRequest.class::cast)
                .forEach(req -> req.setRefreshPolicy(DeleteRequest.RefreshPolicy.NONE));
        requests.stream()
                .filter(req -> req instanceof IndexRequest)
                .map(IndexRequest.class::cast)
                .forEach(req -> req.setRefreshPolicy(IndexRequest.RefreshPolicy.NONE));
        request.setRefreshPolicy(BulkRequest.RefreshPolicy.NONE);
        BulkResponse bulkResponse;
        try {
            bulkResponse = getClient().bulk(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                if (bulkItemResponse.isFailed()) {
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    LOGGER.debug("{} - {} - {}", failure.getType(), failure.getStatus(), failure.getMessage());
                }
            }
        }
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            switch (bulkItemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    handleIndexResponse(indexResponse);
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    handleUpdateResponse(updateResponse);
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    handleDeleteResponse(deleteResponse);
                    break;
            }
        }
    }

    public List<Map<String, Object>> multiGetDocument(String index, List<String> ids, String[] includes, String[] excludes) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-multi-get.html
        return performMultiGetRequest(createMultiGetRequest(index, ids, includes, excludes));
    }

    public List<Map<String, Object>> performMultiGetRequest(MultiGetRequest request) {
        return performMultiGetRequest(request, this::handleGetResponse);
    }

    public <T> List<T> performMultiGetRequest(MultiGetRequest request, Function<GetResponse, T> mapper) {
        MultiGetResponse response;
        try {
            response = getClient().mget(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return Arrays.stream(response.getResponses()).map(multiGetItemResponse -> {
            if (multiGetItemResponse.isFailed()) {
                MultiGetResponse.Failure failure = multiGetItemResponse.getFailure();
                Exception e = failure.getFailure();
                ElasticsearchException ee = (ElasticsearchException) e;
                handleElasticSearchException(ee);
            }
            return multiGetItemResponse.getResponse();
        }).map(mapper).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public MultiGetRequest createMultiGetRequest(String index, List<String> ids, String[] includes, String[] excludes) {
        FetchSourceContext fetch = fetch(true, includes, excludes);// FetchSourceContext.DO_NOT_FETCH_SOURCE
        MultiGetRequest request = new MultiGetRequest();
        ids.forEach(id -> request.add(new MultiGetRequest.Item(index, id).fetchSourceContext(fetch)));
        return request;
    }

    public <T extends ElasticDocument> List<T> multiGetDocument(Class<T> type, List<T> objects) {
        FetchSourceContext fetch = fetch(true, null, null);
        MultiGetRequest request = new MultiGetRequest();
        objects.forEach(o -> request.add(new MultiGetRequest.Item(index(o), id(o)).fetchSourceContext(fetch)));
        return performMultiGetRequest(request, response -> toObject(getObjectMapper(), type, response));
    }

    public long handleBulkByScrollResponse(BulkByScrollResponse bulkResponse) {
        // Get total time taken
        // Check if the request timed out
        // Get total number of docs processed
        // Number of docs that were deleted
        // Number of batches that were executed
        // Number of skipped docs
        // Number of version conflicts
        // Number of times request had to retry bulk index operations
        // Number of times request had to retry search operations
        // The total time this request has throttled itself not including the current
        // throttle time if it is currently sleeping
        // Remaining delay of any current throttle sleep or 0 if not sleeping
        // Failures during search phase
        // Failures during bulk index operation
        TimeValue timeTaken = bulkResponse.getTook();
        LOGGER.trace("{}", timeTaken);
        boolean timedOut = bulkResponse.isTimedOut();
        LOGGER.trace("{}", timedOut);
        long totalDocs = bulkResponse.getTotal();
        LOGGER.trace("{}", totalDocs);
        long updatedDocs = bulkResponse.getUpdated();
        LOGGER.trace("{}", updatedDocs);
        long deletedDocs = bulkResponse.getDeleted();
        LOGGER.trace("{}", deletedDocs);
        long batches = bulkResponse.getBatches();
        LOGGER.trace("{}", batches);
        long noops = bulkResponse.getNoops();
        LOGGER.trace("{}", noops);
        long versionConflicts = bulkResponse.getVersionConflicts();
        LOGGER.trace("{}", versionConflicts);
        long bulkRetries = bulkResponse.getBulkRetries();
        LOGGER.trace("{}", bulkRetries);
        long searchRetries = bulkResponse.getSearchRetries();
        LOGGER.trace("{}", searchRetries);
        TimeValue throttledMillis = bulkResponse.getStatus().getThrottled();
        LOGGER.trace("{}", throttledMillis);
        TimeValue throttledUntilMillis = bulkResponse.getStatus().getThrottledUntil();
        LOGGER.trace("{}", throttledUntilMillis);
        List<ScrollableHitSource.SearchFailure> searchFailures = bulkResponse.getSearchFailures();
        LOGGER.trace("{}", searchFailures);
        List<BulkItemResponse.Failure> bulkFailures = bulkResponse.getBulkFailures();
        LOGGER.trace("{}", bulkFailures);
        return updatedDocs > 0l ? updatedDocs : deletedDocs;
    }

    public <T extends ElasticDocument> long updateDocumentsByQuery(Class<T> type, QueryBuilder query, Integer max, String script,
            Map<String, Object> params) {
        return updateDocumentsByQuery(index(type), query, max, script, params);
    }

    public long updateDocumentsByQuery(String index, QueryBuilder query, Integer max, String script, Map<String, Object> params) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-update-by-query.html
        UpdateByQueryRequest request = new UpdateByQueryRequest(index);
        request.setConflicts(PROCEED);
        request.setQuery(query == null ? QueryBuilders.matchAllQuery() : query);
        if (max != null) request.setMaxDocs(max);
        request.setBatchSize(1_000);
        request.setScript(script(script, params));
        request.setTimeout(getTimeout());
        request.setRefresh(true);
        request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        BulkByScrollResponse bulkResponse;
        try {
            bulkResponse = getClient().updateByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return handleBulkByScrollResponse(bulkResponse);
    }

    public <T extends ElasticDocument> long deleteDocumentsByQuery(Class<T> type, QueryBuilder query, Integer max) {
        return deleteDocumentsByQuery(index(type), query, max);
    }

    public long deleteDocumentsByQuery(String index, QueryBuilder query, Integer max) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-document-delete-by-query.html
        DeleteByQueryRequest request = new DeleteByQueryRequest(index);
        request.setConflicts(PROCEED);
        request.setQuery(query == null ? QueryBuilders.matchAllQuery() : query);
        if (max != null) request.setMaxDocs(max);
        request.setBatchSize(1_000);
        request.setTimeout(getTimeout());
        request.setRefresh(true);
        // request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        BulkByScrollResponse bulkResponse;
        try {
            bulkResponse = getClient().deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        return handleBulkByScrollResponse(bulkResponse);
    }

    public boolean stopScrolling(Scrolling scrolling) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-clear-scroll.html
        if (scrolling == null || scrolling.getScrollId() == null) return false;
        ClearScrollRequest request = new ClearScrollRequest();
        request.addScrollId(scrolling.getScrollId());
        try {
            ClearScrollResponse response = getClient().clearScroll(request, RequestOptions.DEFAULT);
            boolean succeeded = response.isSucceeded();
            scrolling.setScrollId(null);
            return succeeded;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> long count(Class<T> type, QueryBuilder query) {
        return count(index(type), query);
    }

    public long count(String index, QueryBuilder query) {
        CountRequest countRequest = new CountRequest(index);
        countRequest.query(query == null ? QueryBuilders.matchAllQuery() : query);
        CountResponse countResponse;
        try {
            countResponse = getClient().count(countRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        long count = countResponse.getCount();
        // RestStatus status = countResponse.status();
        // Boolean terminatedEarly = countResponse.isTerminatedEarly();
        // int totalShards = countResponse.getTotalShards();
        // int skippedShards = countResponse.getSkippedShards();
        // int successfulShards = countResponse.getSuccessfulShards();
        // int failedShards = countResponse.getFailedShards();
        if (countResponse.getShardFailures() != null && countResponse.getShardFailures().length > 0) {
            for (ShardSearchFailure failure : countResponse.getShardFailures()) {
                String reason = failure.reason();
                LOGGER.debug(reason);
            }
        }

        return count;
    }

    public <T extends ElasticDocument> List<QueryResult<T>> query(Class<T> type, QueryBuilder query, Pagination pagination, List<SortBuilder<?>> sort,
            String[] includes, String[] excludes) {
        return $query(index(type), query, pagination, sort, includes, excludes, Mapper.toObject(getObjectMapper(), type), null);
    }

    public List<QueryResult<Map<String, Object>>> query(String index, QueryBuilder query, Pagination pagination, List<SortBuilder<?>> sort,
            String[] includes, String[] excludes) {
        return $query(index, query, pagination, sort, includes, excludes, Mapper.toMap(), null);
    }

    public <T extends ElasticDocument> List<QueryResult<T>> highlight(Class<T> type, QueryBuilder query, Pagination pagination,
            List<SortBuilder<?>> sort, String[] includes, String[] excludes, List<String> highlight) {
        return $query(index(type), query, pagination, sort, includes, excludes, Mapper.toObject(getObjectMapper(), type), highlight);
    }

    public <T> List<QueryResult<Map<String, Object>>> highlight(String index, QueryBuilder query, Pagination pagination, List<SortBuilder<?>> sort,
            String[] includes, String[] excludes, List<String> highlight) {
        return $query(index, query, pagination, sort, includes, excludes, Mapper.toMap(), highlight);
    }

    public <T extends ElasticDocument> List<AggregationResult> aggregate(Class<T> type, String aggregationField,
            Map<String, List<MetricAggregation>> aggregations, Long minDocCount) {
        return aggregate(index(type), aggregationField, aggregations, minDocCount);
    }

    public List<AggregationResult> aggregate(String index, String aggregationField, Map<String, List<MetricAggregation>> aggregations,
            Long minDocCount) {
        List<AggregationResult> aggregationResults = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(getElasticCustomizer().getMaxResultWindow());

        searchSourceBuilder.timeout(getTimeout());

        searchSourceBuilder.fetchSource(fetchNone());

        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-aggregation-builders.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search.html#java-rest-high-search-request-building-aggs

        AggregationBuilder aggBuilder;
        if (aggregationField == null) {
            // https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html
            aggBuilder = AggregationBuilders.global("aggregation_");
        } else {
            TermsAggregationBuilder termAgg = AggregationBuilders//
                    .terms("aggregation_" + aggregationField)//
                    .field(aggregationField)//
                    .size(getElasticCustomizer().getMaxResultWindow());
            if (minDocCount != null) {
                termAgg.minDocCount(minDocCount);
            }
            aggBuilder = termAgg;
            // https://discuss.elastic.co/t/running-cardinality-for-more-than-10000-buckets/192717/2
            // .includeExclude(new IncludeExclude(0, 10))//
        }
        aggregations.entrySet().forEach(e -> e.getValue().forEach(agg -> buildAggregation(aggBuilder, /* subAggregationField */e.getKey(), agg)));
        searchSourceBuilder.aggregation(aggBuilder);

        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        System.out.println(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = getClient().search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }

        $$query_shardlog(searchResponse);

        SearchHits hits = searchResponse.getHits();
        TotalHits totalHits = hits.getTotalHits();
        // the total number of hits, must be interpreted in the context of totalHits.relation
        long numHits = totalHits.value;
        // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the total (GREATER_THAN_OR_EQUAL_TO)
        TotalHits.Relation relation = totalHits.relation;
        if (relation != Relation.EQUAL_TO) {
            LOGGER.warn("{} {}", relation, numHits);
        }

        searchResponse.getAggregations().getAsMap().entrySet().forEach(e -> {
            if (e.getValue() instanceof ParsedTerms) {
                ParsedTerms.class.cast(e.getValue()).getBuckets().forEach(bucket -> {
                    AggregationResult aggregationResult = new AggregationResult(bucket.getKeyAsString(), bucket.getDocCount());
                    aggregationResults.add(aggregationResult);
                    bucket.getAggregations().forEach(subAggregation -> aggregationResultsParser(aggregationResult, subAggregation));
                });
            } else if (e.getValue() instanceof ParsedGlobal) {
                ParsedGlobal.class.cast(e.getValue())
                        .getAggregations()
                        .forEach(subAggregation -> aggregationResults.add(aggregationResultsParser(new AggregationResult(), subAggregation)));
            } else {
                throw new UnsupportedOperationException(e.getValue().getClass().getName());
            }
        });

        return aggregationResults;
    }

    public <T extends ElasticDocument> QueryResults<T> multiQuery(Class<T> type, List<QueryBuilder> queries, Pagination pagination,
            List<SortBuilder<?>> sort, String[] includes, String[] excludes) {
        return $multi_query(index(type), queries, pagination, sort, includes, excludes, Mapper.toObject(getObjectMapper(), type), null);
    }

    public QueryResults<Map<String, Object>> multiQuery(String index, List<QueryBuilder> queries, Pagination pagination, List<SortBuilder<?>> sort,
            String[] includes, String[] excludes) {
        return $multi_query(index, queries, pagination, sort, includes, excludes, Mapper.toMap(), null);
    }

    protected <T> QueryResults<T> $multi_query(String index, List<QueryBuilder> queries, Pagination pagination, List<SortBuilder<?>> sort,
            String[] includes, String[] excludes, Mapper<T> mapper, List<String> highlight) {
        QueryContext<T> context = new QueryContext<>();
        context.index = index;
        context.query = queries.get(0);
        context.pagination = pagination;
        context.sort = sort;
        context.includes = includes;
        context.excludes = excludes;
        context.mapper = mapper;
        context.highlight = highlight;

        $$query_prepare(context);

        MultiSearchRequest request = new MultiSearchRequest();
        context.searchRequest = new SearchRequest(context.index);
        context.searchRequest.source(context.searchSourceBuilder);
        request.add(context.searchRequest);

        int[] order = new int[queries.size() - 1];
        for (int i = 0; i < order.length; i++) {
            order[i] = -1;
        }
        int orderIndex = 0;
        List<QueryBuilder> extra = new ArrayList<>();
        for (int i = 1; i < queries.size(); i++) {
            QueryBuilder query = queries.get(i);
            if (query != null) {
                order[orderIndex] = i - 1;
                orderIndex++;
                extra.add(query);
            }
        }

        extra.stream().forEach(partialQuery -> {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(fetchNone());
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(1);
            searchSourceBuilder.timeout(getTimeout());
            searchSourceBuilder.query(partialQuery);
            SearchRequest partialQueryRequest = new SearchRequest(index);
            partialQueryRequest.source(searchSourceBuilder);
            request.add(partialQueryRequest);
        });

        MultiSearchResponse multiResponse;
        try {
            multiResponse = getClient().msearch(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        context.searchResponse = multiResponse.getResponses()[0].getResponse();
        if (multiResponse.getResponses()[0].getFailure() != null) {
            throw new RuntimeException(multiResponse.getResponses()[0].getFailure());
        }

        $$query_shardlog(context.searchResponse);

        QueryResults<T> queryResults = new QueryResults<>();
        queryResults.setResults($$query_resolve_results(context));
        queryResults.setPagination(context.pagination);
        queryResults.setMaxScore(context.maxScore);
        long[] partialTotalHits = new long[queries.size() - 1];
        for (int i = 0; i < partialTotalHits.length; i++) {
            partialTotalHits[i] = -1;
        }
        for (int i = 1; i < multiResponse.getResponses().length; i++) {
            partialTotalHits[order[i - 1]] = multiResponse.getResponses()[i].getResponse().getHits().getTotalHits().value;
        }
        queryResults.setPartialTotalHits(Arrays.stream(partialTotalHits).boxed().collect(Collectors.toList()));
        return queryResults;
    }

    static class QueryContext<T> {
        String index;

        QueryBuilder query;

        Pagination pagination;

        List<SortBuilder<?>> sort;

        String[] includes;

        String[] excludes;

        Mapper<T> mapper;

        List<String> highlight;

        Scrolling scrolling;

        SearchSourceBuilder searchSourceBuilder;

        SearchRequest searchRequest;

        SearchScrollRequest scrollRequest;

        SearchResponse searchResponse;

        float maxScore;
    }

    protected <T> List<QueryResult<T>> $query(String index, QueryBuilder query, Pagination pagination, List<SortBuilder<?>> sort, String[] includes,
            String[] excludes, Mapper<T> mapper, List<String> highlight) {
        QueryContext<T> context = new QueryContext<>();
        context.index = index;
        context.query = query;
        context.pagination = pagination;
        context.sort = sort;
        context.includes = includes;
        context.excludes = excludes;
        context.mapper = mapper;
        context.highlight = highlight;
        return $$query(context);
    }

    protected <T> List<QueryResult<T>> $$query(QueryContext<T> context) {
        $$query_prepare(context);

        $$query_highlight_prepare(context);

        // TODO
        // if (false) {
        // TermSuggestionBuilder termSuggestionBuilder =
        // SuggestBuilders.termSuggestion("user").text("kmichy");
        // SuggestBuilder suggestBuilder = new SuggestBuilder();
        // suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
        // searchSourceBuilder.suggest(suggestBuilder);
        // }

        $$query_execute_search(context);

        $$query_shardlog(context.searchResponse);

        List<QueryResult<T>> qr = $$query_resolve_results(context);

        // TODO
        // if (false) {
        // Suggest suggest = searchResponse.getSuggest();
        // TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
        // for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
        // for (TermSuggestion.Entry.Option option : entry) {
        // String suggestText = option.getText().string();
        // }
        // }
        // }

        // TODO
        // if (false) {
        // Map<String, ProfileShardResult> profilingResults =
        // searchResponse.getProfileResults();
        // for (Map.Entry<String, ProfileShardResult> profilingResult :
        // profilingResults.entrySet()) {
        // String key = profilingResult.getKey();
        // ProfileShardResult profileShardResult = profilingResult.getValue();
        // List<QueryProfileShardResult> queryProfileShardResults =
        // profileShardResult.getQueryProfileResults();
        // for (QueryProfileShardResult queryProfileResult : queryProfileShardResults) {
        // for (ProfileResult profileResult : queryProfileResult.getQueryResults()) {
        // String queryName = profileResult.getQueryName();
        // long queryTimeInMillis = profileResult.getTime();
        // List<ProfileResult> profiledChildren = profileResult.getProfiledChildren();
        // }
        // CollectorResult collectorResult = queryProfileResult.getCollectorResult();
        // String collectorName = collectorResult.getName();
        // Long collectorTimeInMillis = collectorResult.getTime();
        // List<CollectorResult> profiledChildren =
        // collectorResult.getProfiledChildren();
        // }
        // AggregationProfileShardResult aggsProfileResults =
        // profileShardResult.getAggregationProfileResults();
        // for (ProfileResult profileResult : aggsProfileResults.getProfileResults()) {
        // String aggName = profileResult.getQueryName();
        // long aggTimeInMillis = profileResult.getTime();
        // List<ProfileResult> profiledChildren = profileResult.getProfiledChildren();
        // }
        // }
        // }

        return qr;
    }

    protected <T> List<QueryResult<T>> $$query_resolve_results(QueryContext<T> context) {
        SearchHits hits = context.searchResponse.getHits();
        context.maxScore = hits.getMaxScore();
        TotalHits totalHits = hits.getTotalHits();
        // the total number of hits, must be interpreted in the context of
        // totalHits.relation
        long numHits = totalHits.value;
        // whether the number of hits is accurate (EQUAL_TO) or a lower bound of
        // the
        // total (GREATER_THAN_OR_EQUAL_TO)
        TotalHits.Relation relation = totalHits.relation;
        if (relation != Relation.EQUAL_TO) {
            LOGGER.warn("{} {}", relation, numHits);
        }
        SearchHit[] searchHits = hits.getHits();
        context.pagination.setTotal(numHits);
        context.pagination.setResults(searchHits == null ? 0 : searchHits.length);
        if (context.scrolling != null && context.pagination.getResults() < context.pagination.getMax()) { // FIXME misschien == 0
            stopScrolling(context.scrolling);
        }

        List<QueryResult<T>> qr = new ArrayList<>();
        if (searchHits != null && searchHits.length > 0) {
            for (SearchHit hit : searchHits) {
                @SuppressWarnings("unchecked")
                T result = context.mapper == null ? (T) hit.getSourceAsMap()
                        : context.mapper.map(hit.getIndex(), hit.getId(), hit.getVersion() == -1l ? null : hit.getVersion(), hit.getSourceAsString(),
                                hit.getSourceAsMap());
                Map<String, List<String>> highlightC = new LinkedHashMap<String, List<String>>();
                if (context.highlight != null && !context.highlight.isEmpty()) {
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    context.highlight.forEach(veld -> {
                        HighlightField highlightField = highlightFields.get(veld);
                        if (highlightField != null) {
                            Text[] fragments = highlightField.fragments();
                            highlightC.put(veld, Arrays.stream(fragments).map(Text::string).collect(Collectors.toList()));
                        }
                    });
                }
                qr.add(new QueryResult<>(//
                        hit.getScore()//
                        , hits.getMaxScore()//
                        , hit.getId()//
                        , result//
                        , highlightC//
                ));
            }
        }
        return qr;
    }

    protected void $$query_shardlog(SearchResponse searchResponse) {
        // RestStatus status = searchResponse.status();
        // TimeValue took = searchResponse.getTook();
        // Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        // boolean timedOut = searchResponse.isTimedOut();
        // int totalShards = searchResponse.getTotalShards();
        // int successfulShards = searchResponse.getSuccessfulShards();
        // int failedShards = searchResponse.getFailedShards();
        if (searchResponse.getShardFailures() != null && searchResponse.getShardFailures().length > 0) {
            for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
                String reason = failure.reason();
                LOGGER.error("{}", reason);
                throw new RuntimeException(failure.getCause());
            }
        }
    }

    protected <T> void $$query_execute_search(QueryContext<T> context) {
        if (context.scrolling != null && context.scrolling.getScrollId() != null) {
            context.scrolling.setStart(context.scrolling.getStart() + context.scrolling.getMax());
            context.scrollRequest = new SearchScrollRequest(context.scrolling.getScrollId());
            context.scrollRequest.scroll(getScrollTimeout());
            try {
                context.searchResponse = getClient().scroll(context.scrollRequest, RequestOptions.DEFAULT);
            } catch (IOException ex) {
                throw handleIOException(ex);
            }
        } else {
            context.searchRequest = new SearchRequest(context.index);
            context.searchRequest.source(context.searchSourceBuilder);
            if (context.scrolling != null) {
                context.searchRequest.scroll(getScrollTimeout());
            }
            try {
                context.searchResponse = getClient().search(context.searchRequest, RequestOptions.DEFAULT);
            } catch (IOException ex) {
                throw handleIOException(ex);
            }
            if (context.scrolling != null) {
                context.scrolling.setScrollId(context.searchResponse.getScrollId());
            }
        }
    }

    protected <T> void $$query_highlight_prepare(QueryContext<T> context) {
        if (context.highlight != null && !context.highlight.isEmpty()) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            context.highlight.forEach(h -> {
                HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(h);
                highlightTitle.highlighterType(HighlighterType.unified.id());
                highlightBuilder.field(highlightTitle);
            });
            context.searchSourceBuilder.highlighter(highlightBuilder);
        }
    }

    protected <T> void $$query_prepare(QueryContext<T> context) {
        if (context.pagination == null) {
            context.pagination = new Pagination();
        }
        if (context.pagination instanceof Scrolling) {
            context.scrolling = Scrolling.class.cast(context.pagination);
        }

        context.searchSourceBuilder = new SearchSourceBuilder();

        if (context.sort != null && !context.sort.isEmpty()) context.searchSourceBuilder.trackScores(true);// adds score when sorting

        // searchSourceBuilder.profile(true); // werkt niet met pagination

        context.searchSourceBuilder.query(context.query == null ? QueryBuilders.matchAllQuery() : context.query);

        if (!(context.pagination instanceof Scrolling)) {
            context.searchSourceBuilder.from(context.pagination.getStart());
        }
        context.searchSourceBuilder.size(context.pagination.getMax());

        context.searchSourceBuilder.timeout(getTimeout());

        if (context.sort != null) {
            context.sort.forEach(context.searchSourceBuilder::sort);
        }

        context.searchSourceBuilder.fetchSource(fetch(true, context.includes, context.excludes));
    }

    protected AggregationResult aggregationResultsParser(AggregationResult aggregationResult, Aggregation subAggregation) {
        if (subAggregation instanceof NumericMetricsAggregation.SingleValue) {
            NumericMetricsAggregation.SingleValue p = NumericMetricsAggregation.SingleValue.class.cast(subAggregation);
            aggregationResult.getResults().put(subAggregation.getName() + "[" + subAggregation.getType() + "]", p.value());
        } else if (subAggregation instanceof PercentileRanks) {
            PercentileRanks p = PercentileRanks.class.cast(subAggregation);
            Iterator<Percentile> it = p.iterator();
            Map<Integer, Double> tmp = new LinkedHashMap<>();
            while (it.hasNext()) {
                Percentile percentile = it.next();
                tmp.put((int) percentile.getPercent(), percentile.getValue());
            }
            aggregationResult.getResults().put(subAggregation.getName() + "[" + subAggregation.getType() + "]", tmp);
        } else if (subAggregation instanceof ParsedExtendedStats) {
            ParsedExtendedStats p = ParsedExtendedStats.class.cast(subAggregation);
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.avg + "]", p.getAvg());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.max + "]", p.getMax());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.min + "]", p.getMin());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + "std_deviation" + "]", p.getStdDeviation());
            aggregationResult.getResults()
                    .put(subAggregation.getName() + "[" + "std_deviation.lowerBound" + "]", p.getStdDeviationBound(Bounds.LOWER));
            aggregationResult.getResults()
                    .put(subAggregation.getName() + "[" + "std_deviation.upperBound" + "]", p.getStdDeviationBound(Bounds.UPPER));
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.sum + "]", p.getSum());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + "sum_of_squares" + "]", p.getSumOfSquares());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + "variance" + "]", p.getVariance());
        } else if (subAggregation instanceof ParsedStats) {
            ParsedStats p = ParsedStats.class.cast(subAggregation);
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.avg + "]", p.getAvg());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.max + "]", p.getMax());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.min + "]", p.getMin());
            aggregationResult.getResults().put(subAggregation.getName() + "[" + MetricAggregation.sum + "]", p.getSum());
        } else {
            throw new UnsupportedOperationException(subAggregation.getClass().getName());
        }
        return aggregationResult;
    }

    protected void buildAggregation(AggregationBuilder aggBuilder, String subAggregationField, MetricAggregation agg) {
        switch (agg) {
            case avg:
                aggBuilder.subAggregation(AggregationBuilders.avg(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case cardinality:
                aggBuilder.subAggregation(AggregationBuilders.cardinality(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case max:
                aggBuilder.subAggregation(AggregationBuilders.max(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case median_absolute_deviation:
                aggBuilder.subAggregation(AggregationBuilders.medianAbsoluteDeviation(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case min:
                aggBuilder.subAggregation(AggregationBuilders.min(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case tdigest_percentile_ranks:
                aggBuilder.subAggregation(
                        AggregationBuilders.percentileRanks(agg + "::" + subAggregationField, IntStream.range(1, 100 + 1).asDoubleStream().toArray())
                                .field(subAggregationField));
                break;
            case sum:
                aggBuilder.subAggregation(AggregationBuilders.sum(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case stats:
                aggBuilder.subAggregation(AggregationBuilders.stats(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case extended_stats:
                aggBuilder.subAggregation(AggregationBuilders.extendedStats(agg + "::" + subAggregationField).field(subAggregationField));
                break;
            case count:
                break;
            default:
                throw new UnsupportedOperationException("" + agg);
        }
    }

    public MainResponse getInfo() {
        MainResponse response;
        try {
            response = getClient().info(RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }

        StringBuilder sb = new StringBuilder();
        String clusterName = response.getClusterName();
        sb.append("clusterName=").append(clusterName).append("\n");
        String clusterUuid = response.getClusterUuid();
        sb.append("clusterUuid=").append(clusterUuid).append("\n");
        String nodeName = response.getNodeName();
        sb.append("nodeName=").append(nodeName).append("\n");
        MainResponse.Version version = response.getVersion();
        String buildDate = version.getBuildDate();
        sb.append("buildDate=").append(buildDate).append("\n");
        String buildFlavor = version.getBuildFlavor();
        sb.append("buildFlavor=").append(buildFlavor).append("\n");
        String buildHash = version.getBuildHash();
        sb.append("buildHash=").append(buildHash).append("\n");
        String buildType = version.getBuildType();
        sb.append("buildType=").append(buildType).append("\n");
        String luceneVersion = version.getLuceneVersion();
        sb.append("luceneVersion=").append(luceneVersion).append("\n");
        String minimumIndexCompatibilityVersion = version.getMinimumIndexCompatibilityVersion();
        sb.append("minimumIndexCompatibilityVersion=").append(minimumIndexCompatibilityVersion).append("\n");
        String minimumWireCompatibilityVersion = version.getMinimumWireCompatibilityVersion();
        sb.append("minimumWireCompatibilityVersion=").append(minimumWireCompatibilityVersion).append("\n");
        String number = version.getNumber();
        sb.append("number=").append(number).append("\n");

        LOGGER.trace("{}", sb);

        return response;
    }

    @SuppressWarnings("unused")
    public void infoXPack() {
        {
            XPackInfoRequest request = new XPackInfoRequest();
            request.setVerbose(true);
            request.setCategories(EnumSet.of(XPackInfoRequest.Category.BUILD, XPackInfoRequest.Category.LICENSE, XPackInfoRequest.Category.FEATURES));
            XPackInfoResponse response;
            try {
                response = getClient().xpack().info(request, RequestOptions.DEFAULT);
            } catch (IOException ex) {
                throw handleIOException(ex);
            }

            BuildInfo build = response.getBuildInfo();
            LicenseInfo license = response.getLicenseInfo();
            // assertThat(license.getExpiryDate(),
            // is(greaterThan(Instant.now().toEpochMilli())));
            FeatureSetsInfo features = response.getFeatureSetsInfo();
        }
        {
            XPackUsageRequest request = new XPackUsageRequest();
            XPackUsageResponse response;
            try {
                response = getClient().xpack().usage(request, RequestOptions.DEFAULT);
            } catch (IOException ex) {
                throw handleIOException(ex);
            }
            Map<String, Map<String, Object>> usages = response.getUsages();
            Map<String, Object> monitoringUsage = usages.get(MONITORING);
            // assertThat(monitoringUsage.get("available"), is(true));
            // assertThat(monitoringUsage.get("enabled"), is(true));
            // assertThat(monitoringUsage.get("collection_enabled"), is(false));
        }
    }

    public <T extends ElasticDocument> boolean deleteIndex(Class<T> type) {
        return deleteIndex(index(type));
    }

    public List<String> analyzeGlobal(Analyzer analyzer, String string) {
        return performAnalyzeRequest(createAnalyzeRequestGlobal(analyzer, string));
    }

    public List<String> analyzeIndex(String index, String analyzer, String string) {
        return performAnalyzeRequest(createAnalyzeRequestIndex(index, analyzer, string));
    }

    public List<String> analyzeIndexField(String index, String field, String string) {
        return performAnalyzeRequest(createAnalyzeRequestIndexField(index, field, string));
    }

    public List<String> performAnalyzeRequest(AnalyzeRequest request) {
        boolean explain = false;
        request.explain(explain);
        AnalyzeResponse response;
        try {
            response = getClient().indices().analyze(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        List<String> collect1 = null;
        List<String> collect2 = null;
        List<String> collect3 = null;
        if (explain && response.detail() != null && response.detail().analyzer() != null && response.detail().analyzer().getTokens() != null) {
            collect1 = Arrays.stream(response.detail().analyzer().getTokens()).map(token -> token.getTerm()).collect(Collectors.toList());
        }
        if (response.getTokens() != null) {
            collect2 = response.getTokens().stream().map(token -> token.getTerm()).collect(Collectors.toList());
        }
        if (response.detail() != null && response.detail().tokenizer() != null && response.detail().tokenizer().getTokens() != null) {
            collect3 = Arrays.stream(response.detail().tokenizer().getTokens()).map(token -> token.getTerm()).collect(Collectors.toList());
        }
        if (collect1 != null) return collect1;
        if (collect2 != null) return collect2;
        if (collect3 != null) return collect3;
        return Collections.emptyList();
    }

    // if (false) {
    // request =
    // AnalyzeRequest.buildCustomNormalizer().addTokenFilter("lowercase").build("<b>BaR</b>");
    // }
    // if (false) {
    // Map<String, Object> stopFilter = new HashMap<>();
    // stopFilter.put("type", "stop");
    // stopFilter.put("stopwords", new String[] { "to" });
    // request = AnalyzeRequest.buildCustomAnalyzer("standard")
    // .addCharFilter("html_strip")
    // .addTokenFilter("lowercase")
    // .addTokenFilter(stopFilter)
    // .build("<b>Some text to analyze</b>");
    // }
    // if (false) {
    // request = AnalyzeRequest.withIndexAnalyzer("my_index", "my_analyzer", "some
    // text to analyze");
    // }
    // if (false) {
    // request = AnalyzeRequest.withNormalizer("my_index", "my_normalizer", "some
    // text to analyze");
    // }

    public AnalyzeRequest createAnalyzeRequestIndexField(String index, String field, String string) {
        return AnalyzeRequest.withField(index, field, string);
    }

    public AnalyzeRequest createAnalyzeRequestGlobal(Analyzer analyzer, String string) {
        return AnalyzeRequest.withGlobalAnalyzer(analyzer.id(null), string);
    }

    public AnalyzeRequest createAnalyzeRequestIndex(String index, String analyzer, String string) {
        return AnalyzeRequest.withIndexAnalyzer(index, analyzer, string);
    }

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-multi-search.html
    // haha

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-search-template.html
    // haha

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-rank-eval.html
    // haha

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-explain.html
    // haha

    // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high-analyze.html
    //
    // Map<String, Object> stopFilter = new HashMap<>();
    // stopFilter.put("type", "stop");
    // stopFilter.put("stopwords", new String[]{ "to" });
    //
    // AnalyzeRequest request = AnalyzeRequest.buildCustomAnalyzer("standard")
    // .addCharFilter("html_strip")
    // .addTokenFilter("lowercase")
    // .addTokenFilter(stopFilter)
    // .build("<b>Some text to analyze</b>");
    // AnalyzeRequest request = AnalyzeRequest.buildCustomNormalizer()
    // .addTokenFilter("lowercase")
    // .build("<b>BaR</b>");
    //
    // AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(
    // "my_index",
    // "my_analyzer",
    // "some text to analyze"
    // );
    //
    // AnalyzeRequest request = AnalyzeRequest.withNormalizer(
    // "my_index",
    // "my_normalizer",
    // "some text to analyze"
    // );
    //
    // request.explain(true);
    // AnalyzeResponse response = client.indices().analyze(request,
    // RequestOptions.DEFAULT);

    public <T extends ElasticDocument> boolean closeIndex(Class<T> type) {
        return closeIndex(index(type));
    }

    public boolean closeIndex(String index) {
        CloseIndexRequest request = new CloseIndexRequest(index);
        request.setMasterTimeout(getTimeout());
        try {
            return getClient().indices().close(request, RequestOptions.DEFAULT).isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean openIndex(Class<T> type) {
        return openIndex(index(type));
    }

    public boolean openIndex(String index) {
        OpenIndexRequest request = new OpenIndexRequest(index);
        request.timeout(getTimeout());
        try {
            return getClient().indices().open(request, RequestOptions.DEFAULT).isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    // protected boolean customAnalyzer(String index, String analyzerName,
    // Map<String, Object> analyzerConfig) {
    // closeIndex(index);
    // boolean s = _customAnalyzer(index, analyzerName, analyzerConfig);
    // openIndex(index);
    // return s;
    // }

    public Map<String, Object> settings() {
        return getElasticCustomizer().settings();
    }

    public <T extends ElasticDocument> Map<String, Object> getObjectMapping(Class<T> annotatedType) {
        return getObjectMapping(annotatedType, MappingListener.DUMMY);
    }

    public <T extends ElasticDocument> Map<String, Object> getObjectMapping(Class<T> annotatedType, MappingListener listener) {
        return getElasticCustomizer().getObjectMapping(annotatedType, listener);
    }

    public <T extends ElasticDocument> List<String> allFields(Class<T> annotatedType) {
        List<String> fields = new ArrayList<>();
        getElasticCustomizer().getObjectMapping(annotatedType, (fullName, field, fieldMapping) -> fields.add(fullName));
        return fields;
    }

    // Can only use wildcard queries on keyword and text fields
    public <T extends ElasticDocument> List<String> wildcardFields(Class<T> annotatedType) {
        List<String> fields = new ArrayList<>();
        getElasticCustomizer().getObjectMapping(annotatedType, (fullName, field, fieldMapping) -> {
            if (fieldMapping.fieldType == FieldType.TEXT || fieldMapping.fieldType == FieldType.KEYWORD) {
                fields.add(fullName);
            }
        });
        return fields;
    }

    public <T extends ElasticDocument> GetRequest createGetRequest(Class<T> type, String id) {
        return createGetRequest(index(type), id, null, null, null);
    }

    public <T extends ElasticDocument> T getDocument(Class<T> type, String id) {
        GetRequest request = createGetRequest(type, id);
        GetResponse response = null;
        try {
            response = getClient().get(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            // bij bv timeout
            throw handleIOException(ex);
        } catch (ElasticsearchException e) {
            handleElasticSearchException(e);
            throw e;
        }
        return toObject(getObjectMapper(), type, response);
    }

    // TODO https://dzone.com/articles/reindexing-data-with-elasticsearch-1

    // TODO https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-snapshot-create-repository.html

    public <T extends ElasticDocument> long count(Class<T> type) {
        return count(index(type));
    }

    public long count(String index) {
        CountRequest countRequest = new CountRequest(index);
        // SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // countRequest.source(searchSourceBuilder);
        countRequest.query(QueryBuilders.matchAllQuery());
        try {
            CountResponse countResponse = getClient().count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public boolean updateClusterSetting(Consumer<Settings.Builder> config) {
        try {
            ClusterUpdateSettingsRequest request = new ClusterUpdateSettingsRequest();
            Settings.Builder builder = Settings.builder();
            config.accept(builder);
            Settings settings = builder.build();
            request.persistentSettings(settings);
            ClusterUpdateSettingsResponse updateSettingsResponse = getClient().cluster().putSettings(request, RequestOptions.DEFAULT);
            LOGGER.info("ack: {}", updateSettingsResponse.isAcknowledged());
            return updateSettingsResponse.isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean updateIndexSetting(Class<T> type, Map<String, Object> config) {
        return updateIndexSetting(index(type), config);
    }

    public boolean updateIndexSetting(String index, Map<String, Object> config) {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(index);
            request.settings(config);
            request.indicesOptions(IndicesOptions.lenientExpandOpen());
            AcknowledgedResponse updateSettingsResponse = getClient().indices().putSettings(request, RequestOptions.DEFAULT);
            return updateSettingsResponse.isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean updateIndexSetting(Class<T> type, Consumer<Settings.Builder> config) {
        return updateIndexSetting(index(type), config);
    }

    public boolean updateIndexSetting(String index, Consumer<Settings.Builder> config) {
        try {
            UpdateSettingsRequest request = new UpdateSettingsRequest(index);
            Settings.Builder builder = Settings.builder();
            config.accept(builder);
            Settings settings = builder.build();
            request.settings(settings);
            request.indicesOptions(IndicesOptions.lenientExpandOpen());
            AcknowledgedResponse updateSettingsResponse = getClient().indices().putSettings(request, RequestOptions.DEFAULT);
            return updateSettingsResponse.isAcknowledged();
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> boolean updateIndexMapping(Class<T> type) {
        return updateIndexMapping(ElasticHelper.index(type), getObjectMapping(type));
    }

    public boolean updateIndexMapping(String index, Map<String, Object> mapping) {
        try {
            PutMappingRequest request = new PutMappingRequest(index);
            request.source(mapping);
            request.setTimeout(TimeValue.timeValueMinutes(2));
            AcknowledgedResponse putMappingResponse = getClient().indices().putMapping(request, RequestOptions.DEFAULT);
            boolean acknowledged = putMappingResponse.isAcknowledged();
            return acknowledged;
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
    }

    public <T extends ElasticDocument> Explanation explainQueryResult(T document, QueryBuilder query) {
        return explainQueryResult(index(document), document.getId(), query, null, null);
    }

    public <T extends ElasticDocument> Explanation explainQueryResult(String index, String id, QueryBuilder query, String[] includes,
            String[] excludes) {
        ExplainRequest request = new ExplainRequest(index, id);
        request.query(query);
        request.fetchSourceContext(fetch(true, includes, excludes));
        ExplainResponse response;
        try {
            response = getClient().explain(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }

        // boolean exists = response.isExists();
        // boolean match = response.isMatch();
        // boolean hasExplanation = response.hasExplanation();
        Explanation explanation = response.getExplanation();
        // GetResult getResult = response.getGetResult();
        // if (getResult != null) {
        // Map<String, Object> source = getResult.getSource();
        // Map<String, DocumentField> fields = getResult.getFields();
        // }
        return explanation;
    }

    public <T extends ElasticDocument> List<String> uniqueTerms(Class<T> type, String field) {
        return uniqueTerms(index(type), field);
    }

    public List<String> uniqueTerms(String index, String field) {
        // {
        // "size": 0,
        // "aggs": {
        // "uniqueValueCount": { <-- name
        // "cardinality": {
        // "field": "${field}" <--
        // }
        // },
        // "${index}": { <--
        // "terms": {
        // "field": "${field}", <--
        // "include": {
        // "partition": 0, <-- iterate over partitions
        // "num_partitions": 1 <-- split up into partitions
        // },
        // "size": 10000
        // }
        // }
        // }
        // }
        List<MetricAggregation> aggregations = Arrays.asList(MetricAggregation.cardinality);
        Map<String, List<MetricAggregation>> agg = Collections.singletonMap("uniqueValueCount", aggregations);
        List<AggregationResult> aggregationResults = aggregate(index, field, agg, null);
        return aggregationResults.stream().map(AggregationResult::getKey).collect(Collectors.toList());
    }

    public boolean deleteRepository(String repositoryName) {
        DeleteRepositoryRequest request = new DeleteRepositoryRequest(repositoryName);
        request.timeout(TimeValue.timeValueMinutes(10));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(10));
        AcknowledgedResponse response;
        try {
            response = getClient().snapshot().deleteRepository(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        boolean acknowledged = response.isAcknowledged();
        return acknowledged;
    }

    public List<org.elasticsearch.cluster.metadata.RepositoryMetadata> getRepositories(String... repositories) {
        GetRepositoriesRequest request = new GetRepositoriesRequest();
        if (repositories != null) {
            request.repositories(repositories);
        }
        request.local(true);
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        GetRepositoriesResponse response;
        try {
            response = getClient().snapshot().getRepository(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        List<org.elasticsearch.cluster.metadata.RepositoryMetadata> repositoryMetadataResponse = response.repositories();
        return repositoryMetadataResponse;
    }

    public boolean createRepository(String repositoryName, Path location) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-snapshot-create-repository.html
        PutRepositoryRequest request = new PutRepositoryRequest();
        Settings settings = Settings.builder()//
                .put(FsRepository.LOCATION_SETTING.getKey(), location.toString())//
                .put(FsRepository.COMPRESS_SETTING.getKey(), true)//
                .build();
        request.settings(settings);
        request.name(repositoryName);
        request.type(FsRepository.TYPE);
        request.timeout(TimeValue.timeValueMinutes(1));
        request.verify(true);
        AcknowledgedResponse response;
        try {
            response = getClient().snapshot().createRepository(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        boolean acknowledged = response.isAcknowledged();
        return acknowledged;
    }

    public List<SnapshotInfo> getSnapshots(String repositoryName) {
        GetSnapshotsRequest request = new GetSnapshotsRequest();
        request.repository(repositoryName);
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        request.verbose(true);
        request.ignoreUnavailable(true);
        GetSnapshotsResponse response;
        try {
            response = getClient().snapshot().get(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        List<SnapshotInfo> snapshotsInfos = response.getSnapshots();
        return snapshotsInfos;
    }

    public SnapshotInfo getSnapshotInfo(String repositoryName, String snapshotName, String... indices) {
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/_snapshot_apis.html
        // https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high-snapshot-create-snapshot.html
        CreateSnapshotRequest request = new CreateSnapshotRequest();
        request.repository(repositoryName);
        request.snapshot(snapshotName);
        request.indices(indices);
        request.indicesOptions(IndicesOptions.fromOptions(false, false, true, true));
        request.partial(false);
        request.includeGlobalState(true);
        request.masterNodeTimeout(TimeValue.timeValueMinutes(10));
        request.waitForCompletion(true);
        CreateSnapshotResponse response;
        try {
            response = getClient().snapshot().create(request, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        RestStatus status = response.status();
        LOGGER.info("{}", "CreateSnapshotResponse.status=" + status);
        SnapshotInfo snapshotInfo = response.getSnapshotInfo();
        return snapshotInfo;
    }

    public void excludeTryouts() {
        ElasticCustomizer.ANALYZER_EXCLUDE_TRYOUTS.forEach(excludeTryout -> {
            try {
                analyzeGlobal(excludeTryout, "test");
                LOGGER.info("analyzer {} is available", excludeTryout);
            } catch (ConnectionException ex) {
                LOGGER.error("{}", ex);
                throw ex;
            } catch (RuntimeException ex) {
                String id = excludeTryout.id(null);
                LOGGER.warn("replacing analyzer {} by {}", id, Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER);
                getElasticCustomizer().getReplaceAnalyzers().put(id, Analyzers.CUSTOM_ANY_LANGUAGE_ANALYZER);
            }
        });
    }

    public boolean isIndexMappingUpToDate(Class<? extends ElasticDocument> indexClass) {
        Map<String, Object> indexMapping = getIndexMappingClean(indexClass);
        Map<String, Object> objectMapping = getObjectMappingClean(indexClass);
        return indexMapping.equals(objectMapping);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getObjectMappingClean(Class<? extends ElasticDocument> indexClass) {
        Map<String, Object> properties = (Map<String, Object>) getObjectMapping(indexClass).get("properties");
        Map<String, Object> jsonToObject = (Map<String, Object>) jsonToObject(getObjectMapper(), Object.class,
                objectToJson(getObjectMapper(), properties.entrySet().stream().filter(entry -> {
                    Map<String, Object> tmp = (Map<String, Object>) entry.getValue();
                    Object enabled = tmp.get("enabled");
                    if (enabled == null) return true;
                    return !"false".equals(enabled.toString());
                }).map(entry -> {
                    Map<String, Object> tmp = (Map<String, Object>) entry.getValue();
                    if ("date".equals(tmp.get("type"))) {
                        tmp.remove("format");
                    }
                    return entry;
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new))));
        return jsonToObject;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getIndexMappingClean(Class<? extends ElasticDocument> indexClass) {
        Map<String, Object> jsonToObject = (Map<String, Object>) jsonToObject(getObjectMapper(), Object.class,
                objectToJson(getObjectMapper(),
                        getIndexMapping(indexClass).entrySet()
                                .stream()
                                .filter(entry -> !entry.getKey().contains("."))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, TreeMap::new))));
        return jsonToObject;
    }

    protected RuntimeException handleIOException(IOException ex) {
        if (ex instanceof java.net.ConnectException) {
            return new ConnectionException(java.net.ConnectException.class.cast(ex));
        }
        return new UncheckedIOException(ex);
    }

    private boolean skip(Object o) {
        if (o == null) return true;
        if (o instanceof String) return StringUtils.isBlank(String.class.cast(o));
        if (o instanceof Collection) return Collection.class.cast(o).isEmpty();
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends ElasticDocument> void debug(T example) {
        String idx = index(example);
        String json = objectToJson(getObjectMapper(), example);
        Map<String, Object> values = jsonToObject(getObjectMapper(), Map.class, json);
        Map<String, Object> mapping = getObjectMappingClean(example.getClass());
        values.entrySet()
                .stream()//
                .filter(a -> !skip(a.getValue()))//
                .filter(a -> !"id".equals(a.getKey()))//
                .forEach(e -> {
                    Map<String, Object> x = (Map<String, Object>) mapping.get(e.getKey());
                    Object value = e.getValue();
                    {
                        String analyzer = String.class.cast(x.get("analyzer"));
                        if (analyzer != null) {
                            System.out.println(e.getKey() + "/" + x.get("type") + "/" + analyzer + "/" + value);
                            if (value instanceof Collection) {
                                Collection.class.cast(value).forEach(v -> {
                                    if (v instanceof String) {
                                        System.out.println("\t" + v + ">" + analyzeIndex(idx, analyzer, String.class.cast(v)));
                                    }
                                });
                            } else {
                                if (value instanceof String) {
                                    System.out.println("\t" + value + ">" + analyzeIndex(idx, analyzer, String.class.cast(value)));
                                }
                            }
                        }
                    }
                    Map<String, Object> fields = (Map<String, Object>) x.get("fields");
                    if (fields != null) {
                        fields.entrySet().forEach(fe -> {
                            Map<String, Object> fv = (Map<String, Object>) fe.getValue();
                            String analyzer = String.class.cast(fv.get("analyzer"));
                            if (analyzer != null) {
                                System.out.println(e.getKey() + "." + fe.getKey() + "/" + fv.get("type") + "/" + analyzer + "/" + value);
                                if (value instanceof Collection) {
                                    Collection.class.cast(value).forEach(v -> {
                                        if (v instanceof String) {
                                            System.out.println("\t" + v + ">" + analyzeIndex(idx, analyzer, String.class.cast(v)));
                                        }
                                    });
                                } else {
                                    if (value instanceof String) {
                                        System.out.println("\t" + value + ">" + analyzeIndex(idx, analyzer, String.class.cast(value)));
                                    }
                                }
                            }
                        });
                    }
                });
    }

    public <T extends ElasticDocument> Map<String, Boolean> updateDocumentsByQuery(Class<T> type, QueryBuilder query, Pagination pagination,
            Consumer<T> changer) {
        return updateDocumentsByQuery(type, query, pagination, (T doc) -> {
            changer.accept(doc);
            return true;
        });
    }

    public <T extends ElasticDocument> Map<String, Boolean> updateDocumentsByQuery(Class<T> type, QueryBuilder query, Pagination pagination,
            Predicate<T> changer) {
        Map<String, Boolean> changed = new LinkedHashMap<>();
        for (QueryResult<T> result : query(type, query, pagination, null, null, null)) {
            T document = result.getResult();
            if (changer.test(document)) {
                indexDocument(document);
                changed.put(document.getId(), true);
            } else {
                changed.put(document.getId(), false);
            }
        }
        return changed;
    }

    public Map<String, Boolean> updateDocumentsByQuery(String index, QueryBuilder query, Pagination pagination,
            Consumer<Map<String, Object>> changer) {
        return updateDocumentsByQuery(index, query, pagination, (Map<String, Object> doc) -> {
            changer.accept(doc);
            return true;
        });
    }

    public Map<String, Boolean> updateDocumentsByQuery(String index, QueryBuilder query, Pagination pagination,
            Predicate<Map<String, Object>> changer) {
        Map<String, Boolean> changed = new LinkedHashMap<>();
        for (QueryResult<Map<String, Object>> result : query(index, query, pagination, null, null, null)) {
            Map<String, Object> document = result.getResult();
            if (changer.test(document)) {
                indexDocument(index, result.getId(), document);
                changed.put(result.getId(), true);
            } else {
                changed.put(result.getId(), false);
            }
        }
        return changed;
    }

    // https://stackoverflow.com/questions/29002215/remove-a-field-from-a-elasticsearch-document
    public long removePropertyFromDocuments(String index, String property) {
        String parent = "";
        if (property.contains(".")) {
            int pos = property.lastIndexOf(".") + 1;
            parent = property.substring(0, pos);
            property = property.substring(pos);
        }
        String del_field_name = "del_field_name";
        String script = ElasticHelper.SCRIPT_SOURCE + parent + "remove(" + ElasticHelper.SCRIPT_PARAMS + del_field_name + ")";
        Map<String, Object> params = Collections.singletonMap(del_field_name, parent + property);
        ExistsQueryBuilder query = QueryBuilders.existsQuery(parent + property);
        return updateDocumentsByQuery(index, query, null, script, params);
    }

    public <T extends ElasticDocument> long removePropertyFromDocuments(Class<T> type, String property) {
        return removePropertyFromDocuments(index(type), property);
    }

    public <T extends ElasticDocument> void reindex(Class<T> type, String destIndex) {
        performReindexRequest(createReindexRequest(index(type), destIndex));
    }

    public void reindex(String index, String destIndex) {
        performReindexRequest(createReindexRequest(index, destIndex));
    }

    public void performReindexRequest(ReindexRequest reindexRequest) {
        BulkByScrollResponse response;
        try {
            response = getClient().reindex(reindexRequest, RequestOptions.DEFAULT);
        } catch (IOException ex) {
            throw handleIOException(ex);
        }
        handleBulkByScrollResponse(response);
    }

    public ReindexRequest createReindexRequest(String index, String destIndex) {
        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceIndices(index);
        reindexRequest.setDestIndex(destIndex);
        return reindexRequest;
    }
}
