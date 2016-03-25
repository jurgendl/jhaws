package org.swingeasy.textarea.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.swingeasy.EComponentExporterImpl;
import org.swingeasy.ETextArea;
import org.swingeasy.ETextAreaExporter;

/**
 * @author Jurgen
 */
public class ETextAreaHtmlExporter extends EComponentExporterImpl<ETextArea>implements ETextAreaExporter {
	/**
	 * @see org.swingeasy.EComponentExporterImpl#exportStream(javax.swing.JComponent, java.io.OutputStream)
	 */
	@Override
	public void exportStream(ETextArea component, OutputStream out) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title>");
			this.postHeaderCreate(component, writer);
			writer.write("</head><body>");
			writer.write(component.getText());
			writer.write("</body></html>");
			writer.flush();
			writer.close();
		}
	}

	/**
	 * @see org.swingeasy.EComponentExporter#getAction()
	 */
	@Override
	public String getAction() {
		return "html-export";
	}

	/**
	 * @see org.swingeasy.EComponentExporterImpl#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "html";
	}

	/**
	 * @see org.swingeasy.EComponentStreamExporter#postHeaderCreate(javax.swing.JComponent, java.io.BufferedWriter)
	 */

	public void postHeaderCreate(ETextArea table, BufferedWriter writer) throws IOException {
		//
	}
}
