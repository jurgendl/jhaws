package org.jhaws.common.lucene;

import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.jhaws.common.lang.JGenerics;

public abstract class LuceneDocumentBuilder<F extends Indexable<? super F>> {
	protected Class<F> type;

	protected List<java.lang.reflect.Field> mapping = new ArrayList<>();

	public LuceneDocumentBuilder() {
		java.lang.reflect.Field[] fields = getType().getDeclaredFields();
		if (fields != null) {
			for (java.lang.reflect.Field field : fields) {
				IndexField indexField = field.getAnnotation(IndexField.class);
				if (indexField != null) {
					mapping.add(field);
				}
			}
		}
	}

	public Document buildDocument(F f) {
		Document d = new Document();

		if (f.getUuid() != null) d.add(new StringField(LuceneIndex.DOC_UUID, f.getUuid(), Field.Store.YES));
		if (f.getVersion() != null) d.add(new IntField(LuceneIndex.DOC_VERSION, f.getVersion(), Field.Store.YES));
		d.add(new LongField(LuceneIndex.DOC_LASTMOD, Date.from(f.getLastmodified().toInstant(ZoneOffset.ofHours(0))).getTime(), Field.Store.YES));

		try {
			for (java.lang.reflect.Field entry : mapping) {
				Object v = entry.get(f);
				if (String.class.equals(entry.getType())) {
					d.add(new StringField(entry.getName(), (String) v, Field.Store.YES));
				} else if (Number.class.isAssignableFrom(entry.getType())) {
					if (v instanceof Long) {
						d.add(new LongField(entry.getName(), Long.class.cast(v), Field.Store.YES));
					} else if (v instanceof Integer) {
						d.add(new IntField(entry.getName(), Integer.class.cast(v), Field.Store.YES));
					} else if (v instanceof Float) {
						d.add(new FloatField(entry.getName(), Float.class.cast(v), Field.Store.YES));
					} else if (v instanceof Double) {
						d.add(new DoubleField(entry.getName(), Double.class.cast(v), Field.Store.YES));
					} else if (v instanceof Short) {
						d.add(new IntField(entry.getName(), Short.class.cast(v).intValue(), Field.Store.YES));
					} else if (v instanceof Byte) {
						d.add(new IntField(entry.getName(), Byte.class.cast(v).intValue(), Field.Store.YES));
					} else {
						throw new UnsupportedOperationException();
					}
				} else if (Date.class.isAssignableFrom(entry.getType())) {
					d.add(new LongField(entry.getName(), Date.class.cast(v).getTime(), Field.Store.YES));
				} else if (ChronoLocalDateTime.class.isAssignableFrom(entry.getType())) {
					d.add(new LongField(entry.getName(), Date.from(ChronoLocalDateTime.class.cast(v).toInstant(ZoneOffset.ofHours(0))).getTime(), Field.Store.YES));
				} else {
					throw new UnsupportedOperationException();
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}

		return d;
	}

	public F retrieveFromDocument(Document d) {
		F f;
		try {
			f = getType().newInstance();
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}

		if (d.getField(LuceneIndex.DOC_UUID) != null) f.setUuid(d.get(LuceneIndex.DOC_UUID));
		if (d.getField(LuceneIndex.DOC_VERSION) != null) f.setVersion(d.getField(LuceneIndex.DOC_VERSION).numericValue().intValue());
		if (d.getField(LuceneIndex.DOC_LASTMOD) != null) f.setLastmodified(new Date(Long.class.cast(d.getField(LuceneIndex.DOC_LASTMOD).numericValue())));

		try {
			for (java.lang.reflect.Field entry : mapping) {
				Object v = entry.get(f);
				if (String.class.equals(entry.getType())) {
					d.add(new StringField(entry.getName(), (String) v, Field.Store.YES));
				} else if (Number.class.isAssignableFrom(entry.getType())) {
					if (v instanceof Long) {
						d.add(new LongField(entry.getName(), Long.class.cast(v), Field.Store.YES));
					} else if (v instanceof Integer) {
						d.add(new IntField(entry.getName(), Integer.class.cast(v), Field.Store.YES));
					} else if (v instanceof Float) {
						d.add(new FloatField(entry.getName(), Float.class.cast(v), Field.Store.YES));
					} else if (v instanceof Double) {
						d.add(new DoubleField(entry.getName(), Double.class.cast(v), Field.Store.YES));
					} else if (v instanceof Short) {
						d.add(new IntField(entry.getName(), Short.class.cast(v).intValue(), Field.Store.YES));
					} else if (v instanceof Byte) {
						d.add(new IntField(entry.getName(), Byte.class.cast(v).intValue(), Field.Store.YES));
					} else {
						throw new UnsupportedOperationException();
					}
				} else if (Date.class.isAssignableFrom(entry.getType())) {
					d.add(new LongField(entry.getName(), Date.class.cast(v).getTime(), Field.Store.YES));
				} else if (ChronoLocalDateTime.class.isAssignableFrom(entry.getType())) {
					d.add(new LongField(entry.getName(), Date.from(ChronoLocalDateTime.class.cast(v).toInstant(ZoneOffset.ofHours(0))).getTime(), Field.Store.YES));
				} else {
					throw new UnsupportedOperationException();
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}

		return f;
	}

	@SuppressWarnings("unchecked")
	public Class<F> getType() {
		if (type == null) type = (Class<F>) JGenerics.findImplementation(this);
		return type;
	}

	public void setType(Class<F> type) {
		this.type = type;
	}
}
