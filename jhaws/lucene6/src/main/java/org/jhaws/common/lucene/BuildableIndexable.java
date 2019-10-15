package org.jhaws.common.lucene;

import java.time.LocalDateTime;

import org.apache.lucene.document.Document;

public class BuildableIndexable<T> implements Indexable<T> {
	protected transient LuceneDocumentBuilder<T> builder;

	@IndexField(LuceneIndex.DOC_UUID)
	protected String uuid;

	@IndexField(LuceneIndex.DOC_LASTMOD)
	protected LocalDateTime lastmodified;

	@SuppressWarnings("unchecked")
	public BuildableIndexable() {
		builder = new LuceneDocumentBuilder<T>((Class<T>) getClass()) {
			//
		};
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	@Override
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public LocalDateTime getLastmodified() {
		return lastmodified;
	}

	@Override
	public void setLastmodified(LocalDateTime lastmodified) {
		this.lastmodified = lastmodified;
	}

	@Override
	public T retrieve(Document doc) {
		return builder.retrieveFromDocument(doc, cast());
	}

	@SuppressWarnings("unchecked")
	protected T cast() {
		return (T) this;
	}

	@Override
	public Document indexable() {
		return builder.buildDocument(cast());
	}
}
