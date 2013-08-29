package org.jhaws.common.lucene.doctype.conversion;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

/**
 * converteert csv naar xls met poi
 * 
 * @author Jurgen
 */
public class CsvTool {
    /**
     * NA
     * 
     * @param inF NA
     * @param outF NA
     * 
     * @throws IOException NA
     */
    public static void convertCsvToXls(final File inF, final File outF) throws IOException {
        CsvTool.convertCsvToXls(new FileInputStream(inF), new BufferedOutputStream(new FileOutputStream(outF)));
    }

    /**
     * na
     * 
     * @param inF na
     * @param outF na
     * 
     * @throws IOException na
     */
    public static void convertCsvToXls(final InputStream inF, final OutputStream outF) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inF));
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("rapport"); //$NON-NLS-1$
        String lijn = in.readLine();
        short rindex = 0;

        while (lijn != null) {
            HSSFRow row = sheet1.createRow(rindex++);
            String[] parts = CsvTool.csvsplit(lijn);

            for (int i = 0; i < parts.length; i++) {
                @SuppressWarnings("deprecation")
                HSSFCell cell = row.createCell((short) i);

                try {
                    Double d = Double.parseDouble(parts[i]);
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(d);
                } catch (final NumberFormatException e) {
                    String s = parts[i];

                    try {
                        if (parts[i].startsWith("\"") && parts[i].endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                            s = s.substring(1, s.length() - 1);
                        }
                    } catch (IndexOutOfBoundsException exx) {
                        System.err.println(lijn);
                        System.err.println(parts[i]);
                        throw exx;
                    }

                    if (s.equals("")) { //$NON-NLS-1$
                        cell.setCellType(Cell.CELL_TYPE_BLANK);
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);

                        HSSFRichTextString content = new HSSFRichTextString(s);
                        cell.setCellValue(content);
                    }
                }
            }

            lijn = in.readLine();
        }

        wb.write(outF);
        outF.close();
    }

    /**
     * NA
     * 
     * @param line NA
     * 
     * @return NA
     */
    public static String[] csvsplit(final String line) {
        String[] parts = line.split(","); //$NON-NLS-1$
        ArrayList<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.startsWith("\"")) { //$NON-NLS-1$

                if (part.endsWith("\"")) { //$NON-NLS-1$
                                           // begint en eindigt met "
                    list.add(part);
                } else {
                    // begint met "
                    sb.append(part);
                    sb.append(","); //$NON-NLS-1$
                }
            } else {
                if (part.endsWith("\"")) { //$NON-NLS-1$
                                           // eindigt met "
                    sb.append(part);
                    list.add(sb.toString());
                    sb = new StringBuilder();
                } else {
                    // geen "
                    if (sb.length() == 0) {
                        list.add(part);
                    } else {
                        sb.append(part);
                    }
                }
            }
        }

        return list.toArray(new String[0]);
    }

    /**
     * na
     * 
     * @param line na
     * 
     * @return
     */
    public static Object[] csvsplito(final String line) {
        String[] strings = CsvTool.csvsplit(line);
        Object[] tmp = new Object[strings.length];

        for (int i = 0; i < tmp.length; i++) {
            String sv = strings[i].toString();

            if (sv.contains(",") || sv.contains(".")) { //$NON-NLS-1$ //$NON-NLS-2$

                try {
                    double d = Double.parseDouble(sv.replace(',', '.'));
                    tmp[i] = new Double(d);
                } catch (NumberFormatException e) {
                    if (strings[i].startsWith("\"") && strings[i].endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                        tmp[i] = strings[i].substring(1, strings[i].length() - 1);
                    } else {
                        tmp[i] = strings[i];
                    }
                }
            } else {
                try {
                    long l = Long.parseLong(sv);
                    tmp[i] = new Long(l);
                } catch (NumberFormatException e) {
                    if (strings[i].startsWith("\"") && strings[i].endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                        tmp[i] = strings[i].substring(1, strings[i].length() - 1);
                    } else {
                        tmp[i] = strings[i];
                    }
                }
            }
        }

        return tmp;
    }
}
