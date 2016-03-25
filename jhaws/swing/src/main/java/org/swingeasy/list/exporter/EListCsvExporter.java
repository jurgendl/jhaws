package org.swingeasy.list.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.swingeasy.EList;
import org.swingeasy.EListExporterImpl;
import org.swingeasy.EListRecord;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 * @see http://tools.ietf.org/html/rfc4180
 */
public class EListCsvExporter<T> extends EListExporterImpl<T> {
	private String seperator = ",";

	private String escape = "\"";

	private String text = "\"";

	/**
	 * 
	 * @see org.swingeasy.EListExporterImpl#exportStream(org.swingeasy.EList, java.io.OutputStream)
	 */
	@Override
	public void exportStream(EList<T> list, OutputStream out) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
			for (EListRecord<T> record : list) {
				String stringValue = record.getStringValue();
				if (stringValue != null) {
					if (Number.class.isAssignableFrom(record.get().getClass())) {
						writer.write(stringValue);
					} else {
						stringValue = stringValue.replaceAll(this.text, this.text + this.text).replaceAll(this.seperator, this.escape + this.seperator);
						writer.write(this.text);
						writer.write(stringValue);
						writer.write(this.text);
					}
				}
				writer.write(SystemSettings.getNewline());
			}
			writer.close();
		}
	}

	/**
	 * 
	 * @see org.swingeasy.EListExporter#getAction()
	 */
	@Override
	public String getAction() {
		return "csv-export";
	}

	/**
	 * 
	 * @see org.swingeasy.EListExporter#getFileExtension()
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
