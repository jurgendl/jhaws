package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.jhaws.common.io.FilePath;

public class OfficeDocFileTextExtracter implements FileTextExtracter {
    @Override
    public List<String> accepts() {
        return Arrays.asList("doc");
    }

    @Override
    public void extract(InputStream inputFile, FilePath target) throws IOException {
        HWPFDocument doc = new HWPFDocument(inputFile);
        try (WordExtractor we = new WordExtractor(doc)) {
            target.write(we.getText());
        }
    }
}
