package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.apache.xmlbeans.XmlException;
import org.jhaws.common.io.FilePath;

public class OfficePpsxFileTextExtracter implements FileTextExtracter {
	@Override
	public List<String> accepts() {
		return Arrays.asList("ppsx");
	}

	@Override
	public void extract(InputStream inputFile, FilePath target) throws IOException {
		try {
			OPCPackage d = OPCPackage.open(inputFile);
			XSLFSlideShow ppsx = new XSLFSlideShow(d);
			try (XSLFPowerPointExtractor xw = new XSLFPowerPointExtractor(ppsx);) {
				target.write(xw.getText());
			}
		} catch (OpenXML4JException | XmlException ex) {
			throw new IOException(ex);
		}
	}
}
