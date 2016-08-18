package org.swingeasy;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public abstract class EListExporterImpl<T> extends EComponentExporterImpl<EList<T>> implements EListExporter<T> {
    /**
     * @see org.swingeasy.EComponentExporterImpl#exportStream(javax.swing.JComponent, java.io.OutputStream)
     */
    @Override
    public abstract void exportStream(EList<T> table, OutputStream out) throws IOException;
}
