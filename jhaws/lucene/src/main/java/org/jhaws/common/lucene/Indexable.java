package org.jhaws.common.lucene;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import model.other.OtherCte;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;

public interface Indexable<T> {
	public static abstract class IndexableAdapter<X> implements Indexable<X> {
		private Integer version;

		private String uuid;

		@Override
		public Integer getVersion() {
			return version;
		}

		@Override
		public void setVersion(Integer version) {
			this.version = version;
		}

		@Override
		public String getUuid() {
			return uuid;
		}

		@Override
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}

	public default Term term() {
		return new Term(LuceneIndex.DOC_UUID, getUuid().toString());
	}

	public default Document indexable() {
		Document d = new Document();
		if (getUuid() != null) d.add(new StringField(LuceneIndex.DOC_UUID, getUuid(), Field.Store.YES));
		if (getVersion() != null) d.add(new IntField(LuceneIndex.DOC_VERSION, getVersion(), Field.Store.YES));
		d.add(new LongField(OtherCte.L_LASTMOD, Date.from(getLastmodified().toInstant(ZoneOffset.ofHours(0))).getTime(), Field.Store.YES));
		return d;
	}

	public default void retrieveBase(Document doc) {
		if (doc.getField(LuceneIndex.DOC_UUID) != null) setUuid(doc.get(LuceneIndex.DOC_UUID));
		if (doc.getField(LuceneIndex.DOC_VERSION) != null) setVersion(doc.getField(LuceneIndex.DOC_VERSION).numericValue().intValue());
		if (doc.getField(OtherCte.L_LASTMOD) != null) setLastmodified(new Date(Long.class.cast(doc.getField(OtherCte.L_LASTMOD).numericValue())));
	}

	public T retrieve(Document doc);

	public default LocalDateTime getLastmodified() {
		return LocalDateTime.ofInstant(new Date(0).toInstant(), ZoneId.systemDefault());
	}

	public default void setLastmodified(@SuppressWarnings("unused") Date lastmodified) {
		//
	}

	public void setUuid(String uuid);

	public String getUuid();

	public void setVersion(Integer version);

	public Integer getVersion();
}
