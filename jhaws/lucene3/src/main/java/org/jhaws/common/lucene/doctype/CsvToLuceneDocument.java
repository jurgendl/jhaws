package org.jhaws.common.lucene.doctype;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;
import org.jhaws.common.lucene.doctype.conversion.CsvTool;

/**
 * na
 * 
 * @author Jurgen
 */
public class CsvToLuceneDocument extends AbstractToLuceneDocument {
	/**
	 * 
	 * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(util.io.IOFile)
	 */
	@Override
	public String getText(IOFile file) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = in.readLine();
		StringBuilder text = new StringBuilder();

		while (line != null) {
			for (Object part : CsvTool.csvsplito(line)) {
				text.append(part);
				text.append("\t"); //$NON-NLS-1$
			}

			text.append("\n\r"); //$NON-NLS-1$
			line = in.readLine();
		}

		return text.toString();
	}
}
