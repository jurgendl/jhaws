package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.lucene.search.IndexSearcher;

@FunctionalInterface
public interface IndexSearcherAction<T> {
    T action(IndexSearcher searcher) throws Exception;

    public default T transaction(IndexSearcher searcher) {
        try {
            return action(searcher);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
