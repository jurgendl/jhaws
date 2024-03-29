package org.jhaws.common.lucene;

import java.lang.reflect.InvocationTargetException;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.util.BytesRef;
import org.jhaws.common.lang.ClassUtils;
import org.jhaws.common.lang.DateTime8;

public abstract class LuceneDocumentBuilder<T> {
    protected Class<T> type;

    protected List<java.lang.reflect.Field> mapping = new ArrayList<>();

    public LuceneDocumentBuilder(Class<T> type) {
        this.type = type;
        init();
    }

    public LuceneDocumentBuilder() {
        getType();
        init();
    }

    public void init() {
        Class<?> current = getType();
        while (current != null && !Object.class.equals(current)) {
            java.lang.reflect.Field[] fields = current.getDeclaredFields();
            if (fields != null) {
                for (java.lang.reflect.Field field : fields) {
                    IndexField indexField = field.getAnnotation(IndexField.class);
                    if (indexField != null) {
                        mapping.add(field);
                        field.setAccessible(true);
                    }
                }
            }
            current = current.getSuperclass();
        }
    }

    public Document buildDocument(T o) {
        Document d = new Document();
        try {
            for (java.lang.reflect.Field entry : mapping) {
                Object v = entry.get(o);
                if (v == null) {
                    continue;
                }
                IndexField anno = entry.getAnnotation(IndexField.class);
                Store store = anno.store() ? Field.Store.YES : Field.Store.NO;
                String name = anno.value();
                if (StringUtils.isBlank(name)) name = entry.getName();
                Class<?> fieldType = entry.getType();
                if (String.class.equals(fieldType)) {
                    String s = String.class.cast(v);
                    FieldType indexFieldType;
                    if (anno.big()) {
                        if (anno.store()) {
                            indexFieldType = TEXT_TYPE_STORED;
                        } else {
                            indexFieldType = TEXT_TYPE_NOT_STORED;
                        }
                    } else {
                        if (anno.store()) {
                            indexFieldType = STRING_TYPE_STORED;
                        } else {
                            indexFieldType = STRING_TYPE_NOT_STORED;
                        }
                    }
                    d.add(new Field(name, s, indexFieldType));
                } else if (byte[].class.isAssignableFrom(fieldType)) {
                    d.add(new BinaryDocValuesField(name, new BytesRef((byte[]) v)));
                    if (store == Store.YES) {
                        d.add(new StoredField(name, new BytesRef((byte[]) v)));
                    }
                } else if (Byte[].class.isAssignableFrom(fieldType)) {
                    Byte[] a = (Byte[]) v;
                    byte[] b = new byte[a.length];
                    for (int i = 0; i < a.length; i++) {
                        b[i] = a[i];
                    }
                    d.add(new BinaryDocValuesField(name, new BytesRef(b)));
                    if (store == Store.YES) {
                        d.add(new StoredField(name, new BytesRef(b)));
                    }
                } else if (Boolean.class.isAssignableFrom(fieldType)) {
                    d.add(new IntField(name, Boolean.TRUE.equals(v) ? 1 : 0, store));
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    if (v instanceof Long) {
                        d.add(new LongField(name, Long.class.cast(v), store));
                    } else if (v instanceof Integer) {
                        d.add(new IntField(name, Integer.class.cast(v), store));
                    } else if (v instanceof Float) {
                        d.add(new FloatField(name, Float.class.cast(v), store));
                    } else if (v instanceof Double) {
                        d.add(new DoubleField(name, Double.class.cast(v), store));
                    } else if (v instanceof Short) {
                        d.add(new IntField(name, Short.class.cast(v).intValue(), store));
                    } else if (v instanceof Byte) {
                        d.add(new IntField(name, Byte.class.cast(v).intValue(), store));
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else if (Date.class.isAssignableFrom(fieldType)) {
                    d.add(new LongField(name, Date.class.cast(v).getTime(), store));
                } else if (ChronoLocalDateTime.class.isAssignableFrom(fieldType)) {
                    d.add(new LongField(name, DateTime8.toDate(ChronoLocalDateTime.class.cast(v)).getTime(), store));
                } else if (ChronoLocalDate.class.isAssignableFrom(fieldType)) {
                    d.add(new LongField(name, DateTime8.toDate(ChronoLocalDate.class.cast(v)).getTime(), store));
                } else {
                    throw new UnsupportedOperationException(String.valueOf(fieldType));
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return d;
    }

    public T retrieveFromDocument(Document d) {
        T o;
        try {
            o = getType().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex);
        }
        return retrieveFromDocument(d, o);
    }

    public T retrieveFromDocument(Document d, T o) {
        try {
            for (java.lang.reflect.Field entry : mapping) {
                String name = entry.getAnnotation(IndexField.class).value();
                if (StringUtils.isBlank(name)) name = entry.getName();
                IndexableField v = d.getField(name);
                if (v == null) {
                    continue;
                }
                Class<?> fieldType = entry.getType();
                if (String.class.equals(fieldType)) {
                    entry.set(o, v.stringValue());
                } else if (Boolean.class.isAssignableFrom(fieldType)) {
                    entry.set(o, v.numericValue().intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);
                } else if (Number.class.isAssignableFrom(fieldType)) {
                    if (Long.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().longValue());
                    } else if (Integer.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().intValue());
                    } else if (Float.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().floatValue());
                    } else if (Double.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().doubleValue());
                    } else if (Short.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().shortValue());
                    } else if (Byte.class.isAssignableFrom(fieldType)) {
                        entry.set(o, v.numericValue().byteValue());
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else if (Date.class.isAssignableFrom(fieldType)) {
                    entry.set(o, new Date(v.numericValue().longValue()));
                } else if (ChronoLocalDateTime.class.isAssignableFrom(fieldType)) {
                    entry.set(o, DateTime8.toLocalDateTime(new Date(v.numericValue().longValue())));
                } else if (ChronoLocalDate.class.isAssignableFrom(fieldType)) {
                    entry.set(o, DateTime8.toLocalDate(new Date(v.numericValue().longValue())));
                } else {
                    throw new UnsupportedOperationException("" + fieldType);
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        return o;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getType() {
        if (type == null) type = (Class<T>) ClassUtils.findImplementation(this);
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public static final FieldType TEXT_TYPE_NOT_STORED = new FieldType();

    public static final FieldType TEXT_TYPE_STORED = new FieldType();

    public static final FieldType STRING_TYPE_NOT_STORED = new FieldType();

    public static final FieldType STRING_TYPE_STORED = new FieldType();

    static {
        // TEXT_TYPE_NOT_STORED.setIndexed(true);
        TEXT_TYPE_NOT_STORED.setOmitNorms(false);
        TEXT_TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);// DOCS_AND_FREQS_AND_POSITIONS
        TEXT_TYPE_NOT_STORED.setTokenized(true);
        TEXT_TYPE_NOT_STORED.setStored(false);
        TEXT_TYPE_NOT_STORED.freeze();

        // TEXT_TYPE_STORED.setIndexed(true);
        TEXT_TYPE_STORED.setOmitNorms(false);
        TEXT_TYPE_STORED.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);// DOCS_AND_FREQS_AND_POSITIONS
        TEXT_TYPE_STORED.setTokenized(true);
        TEXT_TYPE_STORED.setStored(true);
        TEXT_TYPE_STORED.setStoreTermVectorOffsets(true);
        TEXT_TYPE_STORED.setStoreTermVectorPayloads(true);
        TEXT_TYPE_STORED.setStoreTermVectorPositions(true);
        TEXT_TYPE_STORED.setStoreTermVectorPositions(true);
        TEXT_TYPE_STORED.setStoreTermVectors(true);
        TEXT_TYPE_STORED.freeze();

        // STRING_TYPE_NOT_STORED.setIndexed(true);
        STRING_TYPE_NOT_STORED.setOmitNorms(true);
        STRING_TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
        STRING_TYPE_NOT_STORED.setTokenized(false);
        STRING_TYPE_NOT_STORED.setStored(false);
        STRING_TYPE_NOT_STORED.freeze();

        // STRING_TYPE_STORED.setIndexed(true);
        STRING_TYPE_STORED.setOmitNorms(true);
        STRING_TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
        STRING_TYPE_STORED.setTokenized(false);
        STRING_TYPE_STORED.setStored(true);
        STRING_TYPE_STORED.freeze();
    }
}
