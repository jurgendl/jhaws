package org.jhaws.common.lucene;

import java.time.LocalDateTime;

public abstract class IndexableAdapter<T> implements Indexable<T> {
	@IndexField(LuceneIndex.DOC_VERSION)
	protected Integer version;

	@IndexField(LuceneIndex.DOC_UUID)
	protected String uuid;

	@IndexField(LuceneIndex.DOC_LASTMOD)
	protected LocalDateTime lastmodified;

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

	@Override
	public LocalDateTime getLastmodified() {
		return Indexable.super.getLastmodified();
	}

	public void setLastmodified(LocalDateTime lastmodified) {
		this.lastmodified = lastmodified;
	}
}
