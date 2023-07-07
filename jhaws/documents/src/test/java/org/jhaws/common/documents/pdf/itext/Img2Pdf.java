package org.jhaws.common.documents.pdf.itext;

import static org.jhaws.common.lang.CollectionUtils8.sort;

import java.util.List;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Comparators.LastModifiedTimeComparator;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class Img2Pdf {
	public static void main(String[] args) {
		try {
			createPDF(new FilePath(args[0]), new FilePath(args[1]));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static public void createPDF(FilePath trg, FilePath dir) throws Exception {
		FilePath pdffile = new FilePath(dir, trg.getShortFileName() + ".pdf");
		List<FilePath> images = trg.listFiles();
		images = sort(true, images, new LastModifiedTimeComparator());
		com.itextpdf.text.Document document = new com.itextpdf.text.Document();
		/* PdfWriter writer = */PdfWriter.getInstance(document, pdffile.newBufferedOutputStream());
		document.open();
		for (FilePath img : images) {
			Image iimg = Image.getInstance(img.toUrl());
			Rectangle pageSize = new Rectangle(iimg.getWidth(), iimg.getHeight());
			document.setPageSize(pageSize);
			document.newPage();
			iimg.setAbsolutePosition(0f, 0f);
			document.add(iimg);
		}
		document.close();
	}
}
