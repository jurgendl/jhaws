package org.jhaws.common.documents.pdf.itext;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

public class PdfExtractImages {
	public static void main(String[] args) {
		try {
			String outfolder = "?.pdf";
			String pdffile = "?/";
			File file = new File(pdffile);
			PdfReader reader = new PdfReader(file.getAbsolutePath());
			for (int i = 0; i < reader.getXrefSize(); i++) {
				PdfObject pdfobj = reader.getPdfObject(i);
				if (pdfobj == null || !pdfobj.isStream()) {
					continue;
				}
				PdfStream stream = (PdfStream) pdfobj;
				PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
				if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
					byte[] img = PdfReader.getStreamBytesRaw((PRStream) stream);
					if (img.length > 10 * 1024) {
						FileOutputStream out = new FileOutputStream(
								new File(outfolder, String.format("%1$05d", i) + ".jpg"));
						out.write(img);
						out.flush();
						out.close();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
