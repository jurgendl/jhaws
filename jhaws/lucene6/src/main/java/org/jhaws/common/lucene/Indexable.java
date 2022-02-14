package org.jhaws.common.lucene;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LegacyLongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;

@SuppressWarnings("deprecation")
public interface Indexable<T> {
    public default Term term() {
        return new Term(LuceneIndex.DOC_UUID, getUuid().toString());
    }

    public default Document indexable() {
        Document d = new Document();
        if (getUuid() != null) d.add(new StringField(LuceneIndex.DOC_UUID, getUuid(), Field.Store.YES));
        d.add(new LegacyLongField(LuceneIndex.DOC_LASTMOD, Date.from(getLastmodified().toInstant(ZoneOffset.ofHours(0))).getTime(), Field.Store.YES));
        return d;
    }

    public default void retrieveBase(Document doc) {
        if (doc.getField(LuceneIndex.DOC_UUID) != null) setUuid(doc.get(LuceneIndex.DOC_UUID));
        if (doc.getField(LuceneIndex.DOC_LASTMOD) != null) {
            Number l = doc.getField(LuceneIndex.DOC_LASTMOD).numericValue();
            Date d = new Date(Long.class.cast(l));
            LocalDateTime dt = LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault());
            setLastmodified(dt);
        }
    }

    public T retrieve(Document doc);

    public default LocalDateTime getLastmodified() {
        return LocalDateTime.ofInstant(new Date(0).toInstant(), ZoneId.systemDefault());
    }

    public default void setLastmodified(LocalDateTime lastmodified) {
        //
    }

    public void setUuid(String uuid);

    public String getUuid();
}
