package org.jhaws.common.documents.pdf.itext;

//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.pdfbox.cos.COSBase;
//import org.apache.pdfbox.cos.COSDictionary;
//import org.apache.pdfbox.cos.COSName;
//import org.apache.pdfbox.pdfparser.PDFStreamParser;
//import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDResources;
//import org.apache.pdfbox.pdmodel.common.PDStream;

public class PdfRemoveImages {
	public static void main(String[] args) {
		String from = "d:/1.pdf";
		String to = "d:/2.pdf";
	}

//	https://stackoverflow.com/questions/6831194/how-can-i-remove-all-images-drawings-from-a-pdf-file-and-leave-text-only-in-java
//	public static void strip(String pdfFile, String pdfFileOut) throws Exception {
//
//		PDDocument doc = PDDocument.load(pdfFile);
//
//		List pages = doc.getDocumentCatalog().getAllPages();
//		for (int i = 0; i < pages.size(); i++) {
//			PDPage page = (PDPage) pages.get(i);
//
//			// added
//			COSDictionary newDictionary = new COSDictionary(page.getCOSDictionary());
//
//			PDFStreamParser parser = new PDFStreamParser(page.getContents());
//			parser.parse();
//			List tokens = parser.getTokens();
//			List newTokens = new ArrayList();
//			for (int j = 0; j < tokens.size(); j++) {
//				Object token = tokens.get(j);
//
//				if (token instanceof PDFOperator) {
//					PDFOperator op = (PDFOperator) token;
//					if (op.getOperation().equals("Do")) {
//						// remove the one argument to this operator
//						// added
//						COSName name = (COSName) newTokens.remove(newTokens.size() - 1);
//						// added
//						deleteObject(newDictionary, name);
//						continue;
//					}
//				}
//				newTokens.add(token);
//			}
//			PDStream newContents = new PDStream(doc);
//			ContentStreamWriter writer = new ContentStreamWriter(newContents.createOutputStream());
//			writer.writeTokens(newTokens);
//			newContents.addCompression();
//
//			page.setContents(newContents);
//
//			// added
//			PDResources newResources = new PDResources(newDictionary);
//			page.setResources(newResources);
//		}
//
//		doc.save(pdfFileOut);
//		doc.close();
//	}
//
//	// added
//	public static boolean deleteObject(COSDictionary d, COSName name) {
//		for (COSName key : d.keySet()) {
//			if (name.equals(key)) {
//				d.removeItem(key);
//				return true;
//			}
//			COSBase object = d.getDictionaryObject(key);
//			if (object instanceof COSDictionary) {
//				if (deleteObject((COSDictionary) object, name)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
}
