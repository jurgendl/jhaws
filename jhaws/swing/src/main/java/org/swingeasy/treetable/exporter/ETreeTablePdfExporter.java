package org.swingeasy.treetable.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.swingeasy.EComponentExporterImpl;
import org.swingeasy.ETreeTable;
import org.swingeasy.ETreeTableExporterImpl;

/**
 * @author Jurgen
 */
public class ETreeTablePdfExporter<T> extends ETreeTableExporterImpl<T> {
    protected ETreeTableHtmlExporter<T> delegate = new ETreeTableHtmlExporter<T>() {
        @Override
        public void postHeaderCreate(org.swingeasy.ETreeTable<T> table, BufferedWriter writer) throws IOException {
            writer.write(new String(ETreeTablePdfExporter.this.getStyle()));
        };
    };

    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETreeTable<T> component, OutputStream out) throws IOException {
        EComponentExporterImpl.exportHtmlToPdf(this.delegate, component, out);
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getAction()
     */
    @Override
    public String getAction() {
        return "pdf-export";
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
