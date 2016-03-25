package org.swingeasy;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public abstract class ETableExporterImpl<T> extends EComponentExporterImpl<ETable<T>> implements ETableExporter<T> {
    /**
     * @see org.swingeasy.EComponentExporterImpl#exportStream(javax.swing.JComponent, java.io.OutputStream)
     */
    @Override
    public abstract void exportStream(ETable<T> table, OutputStream out) throws IOException;
}
