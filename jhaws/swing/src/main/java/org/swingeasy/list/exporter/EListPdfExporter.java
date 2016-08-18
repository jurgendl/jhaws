package org.swingeasy.list.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.swingeasy.EComponentExporterImpl;
import org.swingeasy.EList;
import org.swingeasy.EListExporterImpl;

/**
 * @author Jurgen
 */
public class EListPdfExporter<T> extends EListExporterImpl<T> {
    protected EListHtmlExporter<T> delegate = new EListHtmlExporter<T>() {
        @Override
        public void postHeaderCreate(EList<T> table, BufferedWriter writer) throws IOException {
            writer.write(new String(EListPdfExporter.this.getStyle()));
        }
    };

    /**
     * 
     * @see org.swingeasy.EListExporterImpl#exportStream(org.swingeasy.EList, java.io.OutputStream)
     */
    @Override
    public void exportStream(EList<T> component, OutputStream out) throws IOException {
        EComponentExporterImpl.exportHtmlToPdf(this.delegate, component, out);
    }

    /**
     * 
     * @see org.swingeasy.EListExporter#getAction()
     */
    @Override
    public String getAction() {
        return "pdf-export";
    }

    /**
     * 
     * @see org.swingeasy.EListExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
