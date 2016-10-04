package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

import org.jhaws.common.io.FilePath;

public class RtfFileTextExtracter implements FileTextExtracter {
	@Override
	public void extract(InputStream file, FilePath target) throws IOException {
		try {
			RTFEditorKit rtfParser = new RTFEditorKit();
			Document document = rtfParser.createDefaultDocument();
			rtfParser.read(file, document, 0);
			String text = document.getText(0, document.getLength());
			target.write(text);
		} catch (BadLocationException ex) {
			throw new IOException(ex);
		}
	}
}
