package org.jhaws.common.lucene;

import org.apache.lucene.index.IndexWriter;

@FunctionalInterface
public interface IndexWriterActionVoid extends IndexWriterAction<Void> {
    void actionVoid(IndexWriter writer) throws Exception;

    @Override
    default Void action(IndexWriter writer) throws Exception {
        actionVoid(writer);
        return null;
    }
}
