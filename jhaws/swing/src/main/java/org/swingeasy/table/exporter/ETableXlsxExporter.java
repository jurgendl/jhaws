package org.swingeasy.table.exporter;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.swingeasy.ETable;
import org.swingeasy.ETableExporterImpl;
import org.swingeasy.ETableRecord;

/**
 * @author Jurgen
 */
public class ETableXlsxExporter<T> extends ETableExporterImpl<T> {
    /**
     * @see org.swingeasy.ETableExporterImpl#exportStream(org.swingeasy.ETable, java.io.OutputStream)
     */
    @Override
    public void exportStream(ETable<T> table, OutputStream out) throws IOException {
        Workbook wb = new SXSSFWorkbook(table.getRecords().size());
        Sheet sh = wb.createSheet();
        int rownum = 0;
        Row row = sh.createRow(rownum);
        int cellnum = 0;
        for (String name : table.getHeadernames()) {
            Cell cell = row.createCell(cellnum++);
            cell.setCellValue(name);
        }
        rownum++;
        for (ETableRecord<T> record : table) {
            row = sh.createRow(rownum);
            cellnum = 0;
            for (int column = 0; column < record.size(); column++) {
                Cell cell = row.createCell(cellnum);
                Object value = record.get(column);
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
                        cell.setCellValue(record.getStringValue(column));
                    }
                }
                cellnum++;
            }
            rownum++;
        }
        wb.write(out);
        out.close();
        if (wb instanceof Closeable) { // MethodNotFoundException in some versions
            Closeable.class.cast(wb).close();
        }
    }

    /**
     * @see org.swingeasy.ETableExporter#getAction()
     */
    @Override
    public String getAction() {
        return "xlsx-export";
    }

    /**
     * @see org.swingeasy.ETableExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "xlsx";
    }
}
