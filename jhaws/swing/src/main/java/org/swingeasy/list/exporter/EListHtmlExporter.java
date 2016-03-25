package org.swingeasy.list.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.swingeasy.EList;
import org.swingeasy.EListExporterImpl;
import org.swingeasy.EListRecord;

/**
 * @author Jurgen
 */
public class EListHtmlExporter<T> extends EListExporterImpl<T> {
	/**
	 * 
	 * @see org.swingeasy.EListExporterImpl#exportStream(org.swingeasy.EList, java.io.OutputStream)
	 */
	@Override
	public void exportStream(EList<T> list, OutputStream out) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title>");
			this.postHeaderCreate(list, writer);
			writer.write("</head><body><table border=\"1\"><tr>");
			writer.write("</tr>");
			for (EListRecord<T> record : list) {
				writer.write("<tr>");
				writer.write("<td>");
				String stringValue = record.getStringValue();
				if (stringValue != null) {
					writer.write(stringValue);
				}
				writer.write("</td>");
				writer.write("</tr>\n");
			}
			writer.write("</table></body></html>");
			writer.flush();
			writer.close();
		}
	}

	/**
	 * 
	 * @see org.swingeasy.EListExporter#getAction()
	 */
	@Override
	public String getAction() {
		return "html-export";
	}

	/**
	 * 
	 * @see org.swingeasy.EListExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "html";
	}

	/**
	 * @see org.swingeasy.EComponentStreamExporter#postHeaderCreate(javax.swing.JComponent, java.io.BufferedWriter)
	 */

	public void postHeaderCreate(EList<T> table, BufferedWriter writer) throws IOException {
		//
	}
}
