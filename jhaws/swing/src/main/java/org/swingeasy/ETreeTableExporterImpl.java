package org.swingeasy;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public abstract class ETreeTableExporterImpl<T> extends EComponentExporterImpl<ETreeTable<T>> implements ETreeTableExporter<T> {
    /**
     * @see org.swingeasy.EComponentExporterImpl#exportStream(javax.swing.JComponent, java.io.OutputStream)
     */
    @Override
    public abstract void exportStream(ETreeTable<T> table, OutputStream out) throws IOException;
}
