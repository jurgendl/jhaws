package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jhaws.common.io.FilePath;

public class OfficeDocxFileTextExtracter implements FileTextExtracter {
	@Override
	public List<String> accepts() {
		return Arrays.asList("docx");
	}

	@Override
	public void extract(InputStream inputFile, FilePath target) throws IOException {
		XWPFDocument docx = new XWPFDocument(inputFile);
		try (XWPFWordExtractor we = new XWPFWordExtractor(docx)) {
			target.write(we.getText());
		}
	}
}
