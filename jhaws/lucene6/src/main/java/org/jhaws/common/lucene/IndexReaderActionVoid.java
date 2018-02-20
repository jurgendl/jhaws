package org.jhaws.common.lucene;

import org.apache.lucene.index.IndexReader;

@FunctionalInterface
public interface IndexReaderActionVoid extends IndexReaderAction<Void> {
    void actionVoid(IndexReader reader) throws Exception;

    default Void action(IndexReader reader) throws Exception {
        actionVoid(reader);
        return null;
    }
}
