package org.jhaws.common.docimport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.jhaws.common.docimport.conversion.CsvTool;
import org.jhaws.common.io.IOFile;

/**
 * na
 * 
 * @author Jurgen
 */
public class ImportCsvDocument implements ImportDocument {
    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(InputStream file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(file));
        String line = in.readLine();
        StringBuilder text = new StringBuilder();

        while (line != null) {
            for (Object part : CsvTool.csvsplito(line)) {
                text.append(part);
                text.append("\t");
            }

            text.append("\n\r");
            line = in.readLine();
        }

        return text.toString();
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "csv";
    }
}
