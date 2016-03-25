package org.swingeasy.table.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.swingeasy.EComponentExporterImpl;
import org.swingeasy.ETable;
import org.swingeasy.ETableExporterImpl;

/**
 * @author Jurgen
 */
public class ETablePdfExporter<T> extends ETableExporterImpl<T> {
    protected ETableHtmlExporter<T> delegate = new ETableHtmlExporter<T>() {
        @Override
        public void postHeaderCreate(org.swingeasy.ETable<T> table, BufferedWriter writer) throws IOException {
            writer.write(new String(ETablePdfExporter.this.getStyle()));
        };
    };

    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETable<T> component, OutputStream out) throws IOException {
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
