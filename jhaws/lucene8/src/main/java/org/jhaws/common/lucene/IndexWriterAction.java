package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.lucene.index.IndexWriter;

@FunctionalInterface
public interface IndexWriterAction<T> {
    T action(IndexWriter writer) throws Exception;

    public default T transaction(IndexWriter writer) {
        try {
            T rv = action(writer);
            writer.commit();
            return rv;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
