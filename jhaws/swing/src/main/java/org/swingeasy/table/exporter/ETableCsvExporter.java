package org.swingeasy.table.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.swingeasy.ETable;
import org.swingeasy.ETableExporterImpl;
import org.swingeasy.ETableRecord;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 * @see http://tools.ietf.org/html/rfc4180
 */
public class ETableCsvExporter<T> extends ETableExporterImpl<T> {
	private String seperator = ",";

	private String escape = "\"";

	private String text = "\"";

	/**
	 * 
	 * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
	 */
	@Override
	public void exportStream(ETable<T> table, OutputStream out) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			for (ETableRecord<T> record : table) {
				for (int column = 0; column < record.size(); column++) {
					String stringValue = record.getStringValue(column);
					if (stringValue != null) {
						if (Number.class.isAssignableFrom(table.getHeaders().getColumnClass(column))) {
							writer.write(stringValue);
						} else {
							stringValue = stringValue.replaceAll(this.text, this.text + this.text).replaceAll(this.seperator, this.escape + this.seperator);
							writer.write(this.text);
							writer.write(stringValue);
							writer.write(this.text);
						}
					}
					if (column < (record.size() - 1)) {
						writer.write(this.seperator);
					}
				}
				writer.write(SystemSettings.getNewline());
			}
			writer.close();
		}
	}

	/**
	 * 
	 * @see org.swingeasy.ETableExporter#getAction()
	 */
	@Override
	public String getAction() {
		return "csv-export";
	}

	/**
	 * 
	 * @see org.swingeasy.ETableExporter#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return "csv";
	}

	public void setEscape(String escape) {
		this.escape = escape;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public void setText(String text) {
		this.text = text;
	}
}
