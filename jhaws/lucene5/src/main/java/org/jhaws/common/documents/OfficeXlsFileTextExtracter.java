package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.jhaws.common.io.FilePath;

public class OfficeXlsFileTextExtracter implements FileTextExtracter {
    @Override
    public List<String> accepts() {
        return Arrays.asList("xls");
    }

    @Override
    public void extract(InputStream inputFile, FilePath target) throws IOException {
        throw new UnsupportedOperationException();
    }
}
