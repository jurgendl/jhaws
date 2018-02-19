package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.lucene.index.IndexWriter;

@FunctionalInterface
public interface IndexWriterAction {
    void action(IndexWriter writer) throws Exception;

    public default void transaction(IndexWriter writer) {
        try {
            action(writer);
            writer.commit();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
