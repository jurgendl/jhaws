package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.lucene.index.IndexReader;

@FunctionalInterface
public interface IndexReaderAction<T> {
    T action(IndexReader reader) throws Exception;

    public default T transaction(IndexReader reader) {
        try {
            return action(reader);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
