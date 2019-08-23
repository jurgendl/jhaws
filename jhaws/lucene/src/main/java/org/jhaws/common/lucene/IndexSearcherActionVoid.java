package org.jhaws.common.lucene;

import org.apache.lucene.search.IndexSearcher;

@FunctionalInterface
public interface IndexSearcherActionVoid extends IndexSearcherAction<Void> {
    void actionVoid(IndexSearcher searcher) throws Exception;

    @Override
    default Void action(IndexSearcher searcher) throws Exception {
        actionVoid(searcher);
        return null;
    }
}
