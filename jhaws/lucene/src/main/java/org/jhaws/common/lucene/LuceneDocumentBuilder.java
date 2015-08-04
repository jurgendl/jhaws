package org.jhaws.common.lucene;

import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;
import org.jhaws.common.lang.DateTime8;
import org.jhaws.common.lang.JGenerics;

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
				String name = entry.getAnnotation(IndexField.class).value();
				if (StringUtils.isBlank(name))
					name = entry.getName();
				Class<?> fieldType = entry.getType();
				if (String.class.equals(fieldType)) {
					d.add(new StringField(name, String.class.cast(v), Field.Store.YES));
				} else if (Boolean.class.isAssignableFrom(fieldType)) {
					d.add(new IntField(name, Boolean.TRUE.equals(v) ? 1 : 0, Field.Store.YES));
				} else if (Number.class.isAssignableFrom(fieldType)) {
					if (v instanceof Long) {
						d.add(new LongField(name, Long.class.cast(v), Field.Store.YES));
					} else if (v instanceof Integer) {
						d.add(new IntField(name, Integer.class.cast(v), Field.Store.YES));
					} else if (v instanceof Float) {
						d.add(new FloatField(name, Float.class.cast(v), Field.Store.YES));
					} else if (v instanceof Double) {
						d.add(new DoubleField(name, Double.class.cast(v), Field.Store.YES));
					} else if (v instanceof Short) {
						d.add(new IntField(name, Short.class.cast(v).intValue(), Field.Store.YES));
					} else if (v instanceof Byte) {
						d.add(new IntField(name, Byte.class.cast(v).intValue(), Field.Store.YES));
					} else {
						throw new UnsupportedOperationException();
					}
				} else if (Date.class.isAssignableFrom(fieldType)) {
					d.add(new LongField(name, Date.class.cast(v).getTime(), Field.Store.YES));
				} else if (ChronoLocalDateTime.class.isAssignableFrom(fieldType)) {
					d.add(new LongField(name, DateTime8.toDate(ChronoLocalDateTime.class.cast(v)).getTime(), Field.Store.YES));
				} else if (ChronoLocalDate.class.isAssignableFrom(fieldType)) {
					d.add(new LongField(name, DateTime8.toDate(ChronoLocalDate.class.cast(v)).getTime(), Field.Store.YES));
				} else {
					throw new UnsupportedOperationException(""+fieldType);
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
			o = getType().newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return retrieveFromDocument(d, o);
	}

	public T retrieveFromDocument(Document d, T o) {
		try {
			for (java.lang.reflect.Field entry : mapping) {
				String name = entry.getAnnotation(IndexField.class).value();
				if (StringUtils.isBlank(name))
					name = entry.getName();
				IndexableField v = d.getField(name);
				if (v == null) {
					continue;
				}
				Class<?> fieldType = entry.getType();
				if (String.class.equals(fieldType)) {
					entry.set(o, v.stringValue());
				} else if (Boolean.class.isAssignableFrom(fieldType)) {
					entry.set(o, v.numericValue().intValue()==1? Boolean.TRUE:Boolean.FALSE);
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
					throw new UnsupportedOperationException(""+fieldType);
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		if (type == null)
			type = (Class<T>) JGenerics.findImplementation(this);
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}
}
