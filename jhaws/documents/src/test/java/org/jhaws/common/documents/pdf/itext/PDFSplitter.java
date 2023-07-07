package org.jhaws.common.documents.pdf.itext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PageRange;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.kernel.utils.PdfSplitter;

public class PDFSplitter {
	public static void main(String args[]) throws IOException {
		String ORIG = "";
		String OUTPUT_FOLDER = "";
		List<String> files = new ArrayList<>();
		{
			int maxPageCount = 1;
			PdfDocument pdfDocument = new PdfDocument(new PdfReader(new File(ORIG)));
			PdfSplitter pdfSplitter = new PdfSplitter(pdfDocument) {
				int partNumber = 1;

				@Override
				protected PdfWriter getNextPdfWriter(PageRange documentPageRange) {
					try {
						String f = OUTPUT_FOLDER + partNumber++ + ".pdf";
						files.add(f);
						return new PdfWriter(f);
					} catch (final FileNotFoundException ignored) {
						throw new RuntimeException();
					}
				}
			};
			pdfSplitter.splitByPageCount(maxPageCount, (pdfDoc, pageRange) -> pdfDoc.close());
			pdfDocument.close();
		}
		{
			PdfDocument pdfDocument = new PdfDocument(new PdfReader(files.get(0)),
					new PdfWriter(OUTPUT_FOLDER + "merged.pdf"));
			files.stream().skip(1).forEach(f -> {
				try {
					PdfDocument pdfDocument2 = new PdfDocument(new PdfReader(f));
					PdfMerger merger = new PdfMerger(pdfDocument);
					merger.merge(pdfDocument2, 1, pdfDocument2.getNumberOfPages());
					pdfDocument2.close();
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
			pdfDocument.close();
		}
	}
}