package org.swingeasy.tmp;

import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.lang.IntegerValue;

// http://stackoverflow.com/questions/7007917/how-to-extract-images-from-a-pdf-with-itext-in-the-correct-order
public class Pdf2Img {
	public static void main(String[] args) {
		try {
			IntegerValue iv = new IntegerValue();
			FilePath pdf = new FilePath(args[0]).checkExists();
			FilePath dir = new FilePath(args[1]).createDirectory();
			// PDFBox
			try (PDDocument document = PDDocument.load(pdf.toFile())) {
				getImagesFromPDF(document, img -> {
					if (img.getWidth() > 100 && img.getHeight() > 100) {
						BufferedOutputStream out = dir.child(pdf.getShortFileName() + "." + iv.add() + ".jpg").newBufferedOutputStream();
						try {
							ImageIO.write(img, "jpg", out);
						} catch (IOException ex) {
							throw new UncheckedIOException(ex);
						}
					}
				});
				// PDPageTree pageTree = document.getDocumentCatalog().getPages();
				// Iterator<PDPage> iter = pageTree.iterator();
				// int i = 0;
				// while (iter.hasNext()) {
				// PDPage page = iter.next();
				// PDResources resources = page.getResources();
				// for (COSName xObjectName : resources.getXObjectNames()) {
				// PDXObject xObject = resources.getXObject(xObjectName);
				// if (xObject instanceof PDImageXObject) {
				// PDImageXObject pdImageXObject = PDImageXObject.class.cast(xObject);
				// if (pdImageXObject.getWidth() > 25 && pdImageXObject.getHeight() > 25) {
				// BufferedOutputStream out = dir.child(pdf.getShortFileName() + "." + (i++) + ".jpg").newBufferedOutputStream();
				// IOUtils.copy(pdImageXObject.createInputStream(), out);
				// }
				// }
				// }
				// }
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
	}

	static public void getImagesFromPDF(PDDocument document, Consumer<RenderedImage> whatToDo) throws IOException {
		for (PDPage page : document.getPages()) {
			getImagesFromResources(page.getResources(), whatToDo);
		}
	}

	static private void getImagesFromResources(PDResources resources, Consumer<RenderedImage> whatToDo) throws IOException {
		for (COSName xObjectName : resources.getXObjectNames()) {
			PDXObject xObject = resources.getXObject(xObjectName);
			if (xObject instanceof PDFormXObject) {
				getImagesFromResources(((PDFormXObject) xObject).getResources(), whatToDo);
			} else if (xObject instanceof PDImageXObject) {
				System.out.println(xObject);
				whatToDo.accept(((PDImageXObject) xObject).getImage());
			}
		}
	}
}
