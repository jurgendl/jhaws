package org.jhaws.common.elasticsearch.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.jhaws.common.elasticsearch.common.ElasticDocument;
import org.jhaws.common.elasticsearch.common.Id;
import org.jhaws.common.elasticsearch.common.Index;
import org.jhaws.common.elasticsearch.common.Version;

import javax.annotation.PostConstruct;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ElasticHelper {
    public static MessageDigest SHA256;

    static {
        try {
            SHA256 = MessageDigest.getInstance("SHA-256");
        } catch (Exception ex) {
            //
        }
    }

    public static final Encoder BASE64_ENCODER = Base64.getEncoder();

    public static String id() {
        return java.util.UUID.randomUUID().toString();
    }

    // https://www.baeldung.com/jackson-annotations
    public static <T> String objectToJson(ObjectMapper om, T object) {
        try {
            return om.writer().writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T jsonToObject(ObjectMapper om, Class<T> type, String json) {
        if (json == null) return null;
        try {
            return om.readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T jsonToObject(ObjectMapper om, TypeReference<T> type, String json) {
        if (json == null) return null;
        try {
            return om.readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Map<Class<?>, String> index = Collections.synchronizedMap(new LinkedHashMap<>());

    private static Map<Class<?>, Field> id = Collections.synchronizedMap(new LinkedHashMap<>());

    private static Map<Class<?>, Field> version = Collections.synchronizedMap(new LinkedHashMap<>());

    private static Map<Class<?>, Optional<Method>> initializers = Collections.synchronizedMap(new LinkedHashMap<>());

    public static <T> Optional<Method> initializer(T o) {
        Class<?> type = o.getClass();
        return initializer(type);
    }

    public static <T> Optional<Method> initializer(Class<T> type) {
        Optional<Method> value = initializers.get(type);
        if (value == null) {
            value = Arrays.stream(type.getDeclaredMethods()).filter(method -> method.getParameterCount() == 0).filter(method -> Modifier.isPublic(method.getModifiers())).filter(method -> method.getDeclaredAnnotation(PostConstruct.class) != null).findAny();
            initializers.put(type, value);
        }
        return value;
    }

    public static <T> String index(T o) {
        Class<?> type = o.getClass();
        return index(type);
    }

    public static <T> String index(Class<T> type) {
        return index.computeIfAbsent(type, k -> type.getAnnotation(Index.class).value());
    }

    public static <T> String id(T o) {
        Class<?> type = o.getClass();
        Field field = id.get(type);
        if (field == null) {
            field = fields(type)//
                    .filter(f -> f.getAnnotation(Id.class) != null)//
                    .findAny().orElseThrow();
            // if (field.getAnnotation(JsonIgnore.class) == null) {
            // throw new IllegalArgumentException("JsonIgnore missing on " +
            // field);
            // }
            field.setAccessible(true);
            id.put(type, field);
        }
        try {
            return (String) field.get(o);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Stream<Field> fields(Class<?> type) {
        Class<?> current = type;
        List<Field> fields = new ArrayList<>();
        while (!Object.class.equals(current)) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields.stream();
    }

    public static <T> T id(T o, String oid) {
        Class<?> type = o.getClass();
        Field field = id.get(type);
        if (field == null) {
            field = fields(type)//
                    .filter(f -> f.getAnnotation(Id.class) != null)//
                    .findAny().orElseThrow();
            //if (field.getAnnotation(JsonIgnore.class) == null) {
            // throw new IllegalArgumentException("JsonIgnore missing on " +
            // field);
            //}
            field.setAccessible(true);
            id.put(type, field);
        }
        try {
            field.set(o, oid);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    public static <T> Long version(T o) {
        Class<?> type = o.getClass();
        Field field = version.get(type);
        if (field == null) {
            field = fields(type)//
                    .filter(f -> f.getAnnotation(Version.class) != null)//
                    .findAny().orElseThrow();
            field.setAccessible(true);
            version.put(type, field);
        }
        try {
            return (Long) field.get(o);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T version(T o, Long v) {
        if (v == null) return o;
        Class<?> type = o.getClass();
        Field field = version.get(type);
        if (field == null) {
            field = fields(type)//
                    .filter(f -> f.getAnnotation(Version.class) != null)//
                    .findAny().orElseThrow();
            field.setAccessible(true);
            version.put(type, field);
        }
        try {
            field.set(o, v);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    public static Map<String, Object> toMap(ObjectMapper om, Object o) {
        @SuppressWarnings("unchecked")
        Map<String, Object> map = om.convertValue(o, Map.class);
        id(o);
        map.remove(id.get(o.getClass()).getName());
        version(o);
        map.remove(version.get(o.getClass()).getName());
        return map;
    }

    public static <T> UnaryOperator<T> unaryOperator(Consumer<T> build) {
        return builder -> {
            build.accept(builder);
            return builder;
        };
    }

    public static <T> T toObject(ObjectMapper om, Class<T> type, GetResponse response) {
        if (!response.isExists()) return null;
        return toObject(om, type, response.getIndex(), response.getId(), response.getVersion(), response.getSourceAsString(), convertCalculatedFields(response.getFields()));
    }

    public static <T> T toObject(ObjectMapper om, Class<T> type, @SuppressWarnings("unused") String _index, String _id, Long _version, String json, Map<String, Object> calculatedFields) {
        T o = jsonToObject(om, type, json);
        id(o, _id);
        version(o, _version);
        setCalculatedFields(o, calculatedFields);
        return o;
    }

    public static Map<String, Object> convertCalculatedFields(Map<String, DocumentField> documentFields) {
        if (documentFields == null || documentFields.isEmpty()) return null;
        Map<String, Object> calculatedFields = new HashMap<>();
        for (Map.Entry<String, DocumentField> documentField : documentFields.entrySet()) {
            String name = documentField.getKey();
            List<Object> values = documentField.getValue().getValues();
            Object value = documentField.getValue().getValue();
            calculatedFields.put(name, values == null || values.size() == 1 ? value : values);
        }
        return calculatedFields;
    }

    public static <T> T setCalculatedFields(T o, Map<String, Object> calculatedFields) {
        if (calculatedFields != null && !calculatedFields.isEmpty()) {
            for (java.lang.reflect.Field f : o.getClass().getDeclaredFields()) {
                String name = f.getName();
                if (calculatedFields.containsKey(name)) {
                    Object value = calculatedFields.get(name);
                    if (value != null) {
                        try {
                            f.setAccessible(true);
                            f.set(o, value);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return o;
    }

    public static Class<?> getCollectionType(java.lang.reflect.Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof java.lang.reflect.ParameterizedType) {
                java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) genericType;
                Type actualType = pt.getActualTypeArguments()[0];
                if (actualType instanceof java.lang.reflect.WildcardType) {
                    java.lang.reflect.WildcardType type = (java.lang.reflect.WildcardType) actualType;
                    Type[] lowerBounds = type.getLowerBounds();
                    Type[] upperBounds = type.getUpperBounds();
                    if (lowerBounds.length > 0) {
                        if (lowerBounds[0] instanceof Class) {
                            return Class.class.cast(lowerBounds[0]);
                        }
                    } else if (upperBounds.length > 0) {
                        if (upperBounds[0] instanceof Class) {
                            return Class.class.cast(upperBounds[0]);
                        }
                    }
                } else {
                    if (actualType instanceof Class) {
                        return Class.class.cast(actualType);
                    }
                }
            }
        }
        return null;
    }

    public static String hash(String text) {
        return BASE64_ENCODER.encodeToString(SHA256.digest(text.getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] readBytes(String resourceName) {
        try (InputStream input = readInputStream(resourceName)) {
            byte[] buffer = new byte[input.available()];
            IOUtils.readFully(input, buffer);
            return buffer;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String readText(String resourceName) {
        return new String(readBytes(resourceName), StandardCharsets.UTF_8);
    }

    public static List<String> readLines(String resourceName) {
        return IOUtils.readLines(readInputStream(resourceName), StandardCharsets.UTF_8);
    }

    public static InputStream readInputStream(String resourceName) {
        return ElasticHelper.class.getClassLoader().getResourceAsStream(resourceName);
    }

    public static <T> void write(ObjectMapper om, OutputStream out, List<T> alle) {
        try {
            om.writer().writeValue(out, alle);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static <T> List<T> readList(ObjectMapper om, Class<T> type, InputStream in) {
        try {
            return om.readValue(in, om.getTypeFactory().constructCollectionType(List.class, type));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static void copy(InputStream in, OutputStream out) {
        try {
            IOUtils.copy(in, out);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String readString(InputStream in) {
        return new String(read(in), StandardCharsets.UTF_8);
    }

    public static byte[] read(InputStream in) {
        try {
            byte[] buffer = new byte[in.available()];
            IOUtils.readFully(in, buffer);
            return buffer;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static final Pattern PATTERN_SEARCHPARTS = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    public static List<String> searchParts(String query) {
        // https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
        Matcher m = PATTERN_SEARCHPARTS.matcher(query);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group(1).replace("\"", "").toLowerCase());
        }
        return list;
    }

    public static <T> BoolQueryBuilder termQuery(String field, Collection<T> list) {
        return list.stream().reduce(//
                QueryBuilders.boolQuery().minimumShouldMatch(1)//
                , (q, v) -> q.should(QueryBuilders.termQuery(field, v))//
                , (x, y) -> x);
    }

    // https://www.elastic.co/guide/en/elasticsearch/painless/7.10/painless-walkthrough.html
    // https://www.elastic.co/guide/en/elasticsearch/painless/current/painless-sort-context.html

    // document source to use in script
    public static final String SCRIPT_SOURCE = "ctx._source.";

    // to use in script, see #var(string)
    public static final String SCRIPT_PARAMS = "params.";

    // score to use in script
    public static final String SCRIPT_SCORE = "_score";

    // variable to use in script
    public static String var(String varName) {
        return SCRIPT_PARAMS + varName;
    }

    // document property to use in script
    public static String property(String propertyName) {
        return "doc['" + propertyName + "'].value";
    }

    static public <T> String debug(T object, ObjectMapper objectMapper) {
        return ElasticHelper.objectToJson(objectMapper, object);
    }

    static public <T extends ElasticDocument> String explanation(T object, ObjectMapper objectMapper, Explanation explanation) {
        return explanation(ElasticHelper.objectToJson(objectMapper, object), explanation);
    }

    static public String explanation(String json, Explanation explanation) {
        JsonObject _jsonObject = _jsonObject(json);
        Explanation details = explanation/* .getDetails()[0] */;
        return explanation0(_jsonObject, details, 0);
    }

    public static JsonObject _jsonObject(String json) {
        return Json.createReader(new InputStreamReader(new ByteArrayInputStream(json.getBytes()))).read().asJsonObject();
    }

    static private String value(JsonObject jso, String description) {
        String field;
        if (description.contains("weight(")) {
            field = description.substring(description.indexOf("weight(") + "weight(".length()).split(":")[0];
        } else {
            field = description.split(":")[0];
        }
        String[] parts = field.split("\\.");
        int i = 0;
        String val = "?";
        Object current = jso;
        try {
            for (; i < parts.length && current instanceof JsonObject; i++) {
                current = JsonObject.class.cast(current).get(parts[i]);
            }
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
        if (current == null) return null;
        try {
            if (current instanceof JsonString)
                val = JsonString.class.cast(current).getString();
            else
                val = current.toString();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
        if (val.length() > 500) val = "...";
        return val.replace("\r\n", " ").replace("\r", " ").replace("\n", " ");
    }

    private static final String START = " ┌ ";

    private static final String MID = " │ ";

    private static final String END = " └ ";

    static private String explanation0(JsonObject jso, Explanation explanation, int d) {
        boolean codes = false;

        boolean hasSub = false;
        for (int i = 0; i < explanation.getDetails().length; i++) {
            if (explanation.getDetails()[i].getValue().doubleValue() > 0.0d) {
                hasSub = true;
            }
        }

        StringBuilder buffer = new StringBuilder();

        if (explanation.getDescription().equals("max of:")) {
            if (codes) buffer.append(d).append("A ");
            buffer.append(MID);
            for (int i = 0; i < d; i++) {
                if (i + 1 == d) {
                    buffer.append(START);
                } else {
                    buffer.append(MID);
                }
            }
            buffer.append("max[").append(explanation.getValue()).append("](");
        } else if (explanation.getDescription().equals("sum of:")) {
            if (codes) buffer.append(d).append("B ");
            if (d == 0)
                buffer.append(START);
            else
                buffer.append(MID);
            buffer.append(START.repeat(Math.max(0, d)));
            buffer.append("Σ[").append(explanation.getValue()).append("](");
        } else {
            if (codes) buffer.append(d).append("C ");
            if (hasSub) {
                buffer.append(MID);
                buffer.append(MID.repeat(Math.max(0, d)));
            } else {
                buffer.append(MID);
                for (int i = 0; i < d; i++) {
                    if (i + 1 == d) {
                        buffer.append(END);
                    } else {
                        buffer.append(MID);
                    }
                }
            }
            String value = value(jso, explanation.getDescription());
            boolean skipFloat = explanation.getValue().floatValue() == Float.MAX_VALUE;
            boolean skipValue = value == null || "?".equals(value);
            if (skipFloat && skipValue && "maxBoost".equals(explanation.getDescription())) {
                // skip line
            } else {
                String str = (skipFloat ? "" : explanation.getValue().floatValue() + " = ") + explanation.getDescription() + (skipFloat ? "" : (" :: " + value));
                buffer.append(str);
            }

        }
        buffer.append("\n");

        Explanation[] details = explanation.getDetails();
        for (Explanation detail : details) {
            if (detail.getValue().doubleValue() > 0.0d) {
                buffer.append(explanation0(jso, detail, d + 1));
            }
        }

        boolean $d = explanation.getDescription().equals("max of:") || explanation.getDescription().equals("sum of:");

        if ($d) {
            if (codes) buffer.append(d).append("D ");
            if (d == 0)
                buffer.append(END);
            else
                buffer.append(MID);
            for (int i = 0; i < d; i++) {
                if (i + 1 == d) {
                    buffer.append(END);
                } else {
                    buffer.append(MID);
                }
            }
            buffer.append(")\n");
        }

        return buffer.toString();
    }
}
