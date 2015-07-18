package org.jhaws.common.lucene;

import org.apache.lucene.document.Document;

public class BuildableIndexable<T> extends IndexableAdapter<T> {
	protected transient LuceneDocumentBuilder<T> builder;

	@SuppressWarnings("unchecked")
	public BuildableIndexable() {
		builder = new LuceneDocumentBuilder<T>((Class<T>) getClass()) {};
	}

	@SuppressWarnings("unchecked")
	@Override
	public T retrieve(Document doc) {
		builder.retrieveFromDocument(doc);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document indexable() {
		return builder.buildDocument((T) this);
	}
}
