package org.jhaws.common.lucene.doctype;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.AbstractToLuceneDocument;


/**
 * Excel XLS document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class XlsToLuceneDocument extends AbstractToLuceneDocument {
    /**
     * Creates a new XlsToLuceneDocument object.
     */
    public XlsToLuceneDocument() {
        super();
    }

    /**
     * 
     * @see org.jhaws.common.lucene.AbstractToLuceneDocument#getText(java.io.File)
     */
    @Override
    public String getText(final IOFile file) throws IOException {
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
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
                        sb.append("; "); //$NON-NLS-1$
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
        return "xls"; //$NON-NLS-1$
    }
}
