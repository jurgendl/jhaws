package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.tika.exception.TikaException;
import org.jhaws.common.io.FilePath;
import org.xml.sax.SAXException;

public class TikaTextExtractor implements FileTextExtracter {
    @Override
    public List<String> accepts() {
        return Arrays.asList("txt", "rtf", "xml", "txt", "pdf", "xls", "xlsx", "ppt", "pptx", "doc", "docx", "odp", "ods", "odt", "epub", "csv");
    }

    @Override
    public void extract(InputStream stream, FilePath target) throws IOException {
        try {
            TikaHelper.parse(stream, target.newBufferedOutputStream());
        } catch (SAXException ex) {
            throw new IOException(ex);
        } catch (TikaException ex) {
            throw new IOException(ex);
        }
    }
}
