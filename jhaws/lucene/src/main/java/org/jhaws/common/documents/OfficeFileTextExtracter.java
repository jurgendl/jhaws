package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hdgf.extractor.VisioTextExtractor;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.extractor.ExcelExtractor;
import org.apache.xmlbeans.XmlException;
import org.jhaws.common.io.FilePath;

public class OfficeFileTextExtracter implements FileTextExtracter {
	@Override
	public List<String> accepts() {
		return Arrays.asList("doc", "xls", "pps");
	}

	@Override
	public void extract(InputStream fis, FilePath target) throws IOException {
		NPOIFSFileSystem fileSystem = new NPOIFSFileSystem(fis);
		// Firstly, get an extractor for the Workbook
		POIOLE2TextExtractor oleTextExtractor;
		try {
			oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
		} catch (OpenXML4JException | XmlException ex) {
			throw new IOException(ex);
		}
		// Then a List of extractors for any embedded Excel, Word, PowerPoint
		// or Visio objects embedded into it.
		POITextExtractor[] embeddedExtractors;
		try {
			embeddedExtractors = ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
		} catch (OpenXML4JException | XmlException ex) {
			throw new IOException(ex);
		}
		for (POITextExtractor textExtractor : embeddedExtractors) {
			// If the embedded object was an Excel spreadsheet.
			if (textExtractor instanceof ExcelExtractor) {
				ExcelExtractor excelExtractor = (ExcelExtractor) textExtractor;
				// System.out.println(excelExtractor.getText());
				target.write(excelExtractor.getText());
			}
			// A Word Document
			else if (textExtractor instanceof WordExtractor) {
				WordExtractor wordExtractor = (WordExtractor) textExtractor;
				// String[] paragraphText = wordExtractor.getParagraphText();
				// for (String paragraph : paragraphText) {
				// System.out.println(paragraph);
				// }
				// // Display the document's header and footer text
				// System.out.println("Footer text: " + wordExtractor.getFooterText());
				// System.out.println("Header text: " + wordExtractor.getHeaderText());
				target.write(wordExtractor.getText());
			}
			// PowerPoint Presentation.
			else if (textExtractor instanceof PowerPointExtractor) {
				PowerPointExtractor powerPointExtractor = (PowerPointExtractor) textExtractor;
				// System.out.println("Text: " + powerPointExtractor.getText());
				// System.out.println("Notes: " + powerPointExtractor.getNotes());
				target.write(powerPointExtractor.getText());
			}
			// Visio Drawing
			else if (textExtractor instanceof VisioTextExtractor) {
				VisioTextExtractor visioTextExtractor = (VisioTextExtractor) textExtractor;
				// System.out.println("Text: " + visioTextExtractor.getText());
				target.write(visioTextExtractor.getText());
			} else {
				new RuntimeException("cannot extract").printStackTrace();
			}
		}
	}
}
