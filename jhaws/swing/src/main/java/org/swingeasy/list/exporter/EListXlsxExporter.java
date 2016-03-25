package org.swingeasy.list.exporter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.swingeasy.EList;
import org.swingeasy.EListExporterImpl;
import org.swingeasy.EListRecord;

/**
 * @author Jurgen
 */
public class EListXlsxExporter<T> extends EListExporterImpl<T> {
    /**
     * 
     * @see org.swingeasy.EListExporterImpl#exportStream(org.swingeasy.EList, java.io.OutputStream)
     */
    @Override
    public void exportStream(EList<T> list, OutputStream out) throws IOException {
        Workbook wb = new SXSSFWorkbook(list.getRecords().size());
        Sheet sh = wb.createSheet();
        int rownum = 0;
        Row row = sh.createRow(rownum);
        int cellnum = 0;
        rownum++;
        for (EListRecord<T> record : list) {
            row = sh.createRow(rownum);
            cellnum = 0;
            Cell cell = row.createCell(cellnum);
            Object value = record.get();
            if (value != null) {
                if (value instanceof Boolean) {
                    cell.setCellValue(Boolean.class.cast(value).booleanValue());
                } else if (value instanceof Calendar) {
                    cell.setCellValue(Calendar.class.cast(value));
                } else if (value instanceof Date) {
                    cell.setCellValue(Date.class.cast(value));
                } else if (value instanceof Double) {
                    cell.setCellValue(Double.class.cast(value).doubleValue());
                } else if (value instanceof Number) {
                    cell.setCellValue(Number.class.cast(value).doubleValue());
                } else {
                    cell.setCellValue(record.getStringValue());
                }
            }
            cellnum++;
            rownum++;
        }
        wb.write(out);
        out.close();
    }

    /**
     * 
     * @see org.swingeasy.EListExporter#getAction()
     */
    @Override
    public String getAction() {
        return "xlsx-export";
    }

    /**
     * 
     * @see org.swingeasy.EListExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "xlsx";
    }
}
