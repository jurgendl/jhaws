package org.jhaws.common.documents.pdf.itext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jhaws.common.io.FilePath;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

public class Txt2Pdf {
	public static void main(String[] args) {
		FilePath f = new FilePath("?/");
		f.listFiles().stream().filter(a -> a.getExtension().equalsIgnoreCase("txt")).forEach(ff -> {
			try {
				File fff = ff.toFile();
				String ffff = ff.appendExtension("pdf").getAbsolutePath();
				System.out.println(ffff);
				createPdf(fff, ffff);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	static public void createPdf(File sourceFile, String dest) throws Exception {
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

		Document document = new Document(pdf);
		document.setTextAlignment(TextAlignment.LEFT);
		document.setFontSize((float) 8.0);
		document.setLeftMargin((float) 40.0);
		document.setRightMargin((float) 40.0);

		// BufferedReader br = new BufferedReader(new FileReader(sourceFile));
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), "UTF8"));
		String line;
		// PdfFont normal = PdfFontFactory.createFont(FontConstants.COURIER);
		Paragraph para = new Paragraph();
		// para.setFont(normal);
		while ((line = br.readLine()) != null) {
			// line = line.replace("\u0020", "\u00A0");
			para.add(line + "\n");
			// document.add(new Paragraph(line).setFont(normal));
		}
		document.add(para);
		document.close();
		br.close();
	}
}
