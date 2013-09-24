package org.jhaws.common.docimport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.jhaws.common.io.IOFile;

/**
 * Excel XLS document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportExcelDocument implements ImportDocument {
    /**
     * Creates a new XlsToLuceneDocument object.
     */
    public ImportExcelDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(final InputStream file) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem(file);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        StringBuilder sb = new StringBuilder(100);

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            HSSFSheet sheet = wb.getSheetAt(i);
            Iterator<?> rowit = sheet.rowIterator();

            while (rowit.hasNext()) {
                HSSFRow row = (HSSFRow) rowit.next();

                Iterator<?> cellit = row.cellIterator();

                while (cellit.hasNext()) {
                    HSSFCell cell = (HSSFCell) cellit.next();

                    if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                        sb.append(cell.getStringCellValue());
                        sb.append("; ");
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "xls";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
