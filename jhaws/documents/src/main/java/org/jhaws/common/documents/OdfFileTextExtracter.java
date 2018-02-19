package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.odf.OpenDocumentParser;
import org.apache.tika.sax.BodyContentHandler;
import org.jhaws.common.io.FilePath;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

// alternate: https://tika.apache.org/1.2/api/org/apache/tika/parser/rtf/RTFParser.html
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
            ContentHandler handler = new BodyContentHandler();
            // handler = new SafeContentHandler(handler);
            Metadata metadata = new Metadata();
            ParseContext pcontext = new ParseContext();
            OpenDocumentParser openofficeparser = new OpenDocumentParser();
            openofficeparser.parse(pdf, handler, metadata, pcontext);
            txt.write(handler.toString());
        } catch (org.apache.tika.exception.TikaException | SAXException ex) {
            throw new IOException(ex);
        }
    }
}
