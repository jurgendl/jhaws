package org.swingeasy.textarea.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.swingeasy.EComponentExporterImpl;
import org.swingeasy.ETextArea;
import org.swingeasy.ETextAreaExporter;

/**
 * @author Jurgen
 */
public class ETextAreaPdfExporter extends EComponentExporterImpl<ETextArea> implements ETextAreaExporter {
    protected ETextAreaHtmlExporter delegate = new ETextAreaHtmlExporter() {
        @Override
        public void postHeaderCreate(ETextArea component, BufferedWriter writer) throws IOException {
            writer.write(new String(this.getStyle()));
        };
    };

    /**
     * @see org.swingeasy.EComponentExporterImpl#exportStream(javax.swing.JComponent, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETextArea component, OutputStream out) throws IOException {
        EComponentExporterImpl.exportHtmlToPdf(this.delegate, component, out);
    }

    /**
     * @see org.swingeasy.EComponentExporter#getAction()
     */
    @Override
    public String getAction() {
        return "pdf-export";
    }

    /**
     * @see org.swingeasy.EComponentExporterImpl#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
