package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.jhaws.common.io.FilePath;
import org.xml.sax.SAXException;

/**
 * @see https://www.tutorialspoint.com/tika/tika_extracting_odf.htm
 */
public class OdfFileTextExtracter implements FileTextExtracter {
	@Override
	public List<String> accepts() {
		return Arrays.asList("odt", "odp", "ods");
	}

	@Override
	public void extract(InputStream pdf, FilePath txt) throws IOException {
		try {
			org.apache.tika.sax.BodyContentHandler handler = new org.apache.tika.sax.BodyContentHandler();
			org.apache.tika.metadata.Metadata metadata = new org.apache.tika.metadata.Metadata();
			org.apache.tika.parser.ParseContext pcontext = new org.apache.tika.parser.ParseContext();
			org.apache.tika.parser.odf.OpenDocumentParser openofficeparser = new org.apache.tika.parser.odf.OpenDocumentParser();
			openofficeparser.parse(pdf, handler, metadata, pcontext);
			txt.write(handler.toString());
		} catch (org.apache.tika.exception.TikaException | SAXException ex) {
			throw new IOException(ex);
		}
	}
}
