package org.swingeasy.treetable.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.swingeasy.EComponentHelper;
import org.swingeasy.ETreeTable;
import org.swingeasy.ETreeTableExporterImpl;
import org.swingeasy.ETreeTableRecord;

/**
 * @author Jurgen
 */
public class ETreeTableHtmlExporter<T> extends ETreeTableExporterImpl<T> {
	/**
	 * 
	 * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
	 */
	@Override
	public void exportStream(ETreeTable<T> table, OutputStream out) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title>");
			this.postHeaderCreate(table, writer);
			writer.write("</head><body><table border=\"1\"><tr>");
			for (String name : table.getHeadernames()) {
				writer.write("<th>");
				writer.write(EComponentHelper.removeHtml(name));
				writer.write("</th>");
			}
			writer.write("</tr>");
			for (ETreeTableRecord<T> record : table) {
				writer.write("<tr>");
				for (int column = 0; column < record.size(); column++) {
					writer.write("<td>");
					String stringValue = record.getStringValue(column);
					if (stringValue != null) {
						writer.write(EComponentHelper.removeHtml(stringValue));
					}
					writer.write("</td>");
				}
				writer.write("</tr>\n");
			}
			writer.write("</table></body></html>");
			writer.flush();
			writer.close();
		}
	}

	/**
	 * 
	 * @see org.swingeasy.ETableExporter#getAction()
	 */
	@Override
	public String getAction() {
		return "html-export";
	}

	/**
	 * 
	 * @see org.swingeasy.ETableExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "html";
	}

	/**
	 * @see org.swingeasy.EComponentStreamExporter#postHeaderCreate(org.swingeasy.ETreeTable, java.io.BufferedWriter)
	 */

	public void postHeaderCreate(ETreeTable<T> table, BufferedWriter writer) throws IOException {
		//
	}
}
