package org.jhaws.common.documents.pdf.itext;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDTransparencyGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jhaws.common.lang.IntegerValue;

// https://memorynotfound.com/apache-pdfbox-extract-images-pdf-document/
public class PdfExtractImagesAlt {
	public static void main(String[] args) {
		try {
			String outfolder = "?.pdf";
			String pdffile = "?/";
			File inFile = new File(pdffile);
			PDDocument document = PDDocument.load(inFile);
			PDPageTree pages = document.getDocumentCatalog().getPages();
			IntegerValue ii = new IntegerValue();
			for (int i = 0; i < pages.getCount(); i++) {
				PDPage page = pages.get(i);
				System.out.println("page " + i);
				PDResources pdResources = page.getResources();
				iterate(outfolder, ii, pdResources);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void iterate(String outfolder, IntegerValue ii, PDResources pdResources) throws IOException {
		for (COSName name : pdResources.getXObjectNames()) {
			PDXObject o = pdResources.getXObject(name);
			if (o instanceof PDImageXObject) {
				PDImageXObject image = (PDImageXObject) o;
				if (image.getWidth() > 200) {
					System.out.println("image " + ii);
					// COSStream cs = image.getCOSObject();
					// InputStream in = cs.createInputStream();
					// try (OutputStream out = new FileOutputStream("f:/tmp/" + "extracted-image-" +
					// ii.get() + ".jpg")) {
					// IOUtils.copy(in, out);
					// }
					ImageIO.write(image.getImage(), "jpg",
							new File(outfolder + "extracted-image-" + ii.get() + ".jpg"));
					ii.add();
				}
			}
			if (o instanceof PDTransparencyGroup) {
				PDTransparencyGroup group = (PDTransparencyGroup) o;
				PDResources pdResources2 = group.getResources();
				iterate(outfolder, ii, pdResources2);
			}
		}
	}
}
