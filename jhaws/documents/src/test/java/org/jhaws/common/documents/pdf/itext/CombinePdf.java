package org.jhaws.common.documents.pdf.itext;

import java.util.stream.Collectors;

import org.jhaws.common.io.FilePath;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;

public class CombinePdf {
	public static void main(String[] args) {
		try {
			// https://stackoverflow.com/questions/40589413/is-it-possible-to-merge-several-pdfs-using-itext7
			FilePath root = new FilePath("?");
			FilePath dest = root.child("combi.pdf");
			PdfDocument pdf = new PdfDocument(new PdfWriter(dest.toFile()));
			PdfMerger merger = new PdfMerger(pdf);
			for (FilePath x : root.listFiles().stream().filter(x -> "pdf".equalsIgnoreCase(x.getExtension()))
					.filter(x -> !x.equal(dest)).sorted().collect(Collectors.toList())) {
				System.out.println(x);
				PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(x.toFile()));
				merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());
				firstSourcePdf.close();
			}
			pdf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
