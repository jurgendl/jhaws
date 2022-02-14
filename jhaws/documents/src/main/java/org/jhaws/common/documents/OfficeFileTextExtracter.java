package org.jhaws.common.documents;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.extractor.POIOLE2TextExtractor;
import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.ooxml.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xmlbeans.XmlException;
import org.jhaws.common.io.FilePath;

public class OfficeFileTextExtracter implements FileTextExtracter {
    @Override
    public List<String> accepts() {
        return Arrays.asList("doc", "xls", "pps");
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    @Override
    public void extract(InputStream fis, FilePath target) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(fis, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        POITextExtractor[] embeddedExtractors = null;
        {
            try {
                POIFSFileSystem fileSystem = new POIFSFileSystem(bais);
                POIOLE2TextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
                embeddedExtractors = ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
            } catch (OpenXML4JException | XmlException ex) {
                System.err.println(ex);
            }
        }
        if (embeddedExtractors == null || embeddedExtractors.length == 0) {
            bais.reset();
            try {
                POIFSFileSystem fileSystem = new POIFSFileSystem(bais);
                POIOLE2TextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
                embeddedExtractors = ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
            } catch (OpenXML4JException | XmlException ex) {
                System.err.println(ex);
            }
        }
        if (embeddedExtractors == null || embeddedExtractors.length == 0) {
            bais.reset();
            try {
                POITextExtractor oleTextExtractor = ExtractorFactory.createExtractor(bais);
                target.write(oleTextExtractor.getText());
            } catch (OpenXML4JException | XmlException ex) {
                System.err.println(ex);
            }
        }
        if (embeddedExtractors != null) {
            for (POITextExtractor textExtractor : embeddedExtractors) {
                // If the embedded object was an Excel spreadsheet.
                if (textExtractor instanceof org.apache.poi.ss.extractor.ExcelExtractor) {
                    org.apache.poi.ss.extractor.ExcelExtractor excelExtractor = (org.apache.poi.ss.extractor.ExcelExtractor) textExtractor;
                    target.write(excelExtractor.getText());
                }
                if (textExtractor instanceof org.apache.poi.hssf.extractor.ExcelExtractor) {
                    org.apache.poi.hssf.extractor.ExcelExtractor excelExtractor = (org.apache.poi.hssf.extractor.ExcelExtractor) textExtractor;
                    target.write(excelExtractor.getText());
                }
                if (textExtractor instanceof org.apache.poi.xssf.extractor.XSSFExcelExtractor) {
                    org.apache.poi.xssf.extractor.XSSFExcelExtractor excelExtractor = (org.apache.poi.xssf.extractor.XSSFExcelExtractor) textExtractor;
                    target.write(excelExtractor.getText());
                }
                // A Word Document
                else if (textExtractor instanceof org.apache.poi.hwpf.extractor.WordExtractor) {
                    org.apache.poi.hwpf.extractor.WordExtractor wordExtractor = (org.apache.poi.hwpf.extractor.WordExtractor) textExtractor;
                    // String[] paragraphText = wordExtractor.getParagraphText();
                    // for (String paragraph : paragraphText) {
                    // System.out.println(paragraph);
                    // }
                    // // Display the document's header and footer text
                    // System.out.println("Footer text: " + wordExtractor.getFooterText());
                    // System.out.println("Header text: " + wordExtractor.getHeaderText());
                    target.write(wordExtractor.getText());
                } else if (textExtractor instanceof org.apache.poi.hwpf.extractor.Word6Extractor) {
                    org.apache.poi.hwpf.extractor.Word6Extractor wordExtractor = (org.apache.poi.hwpf.extractor.Word6Extractor) textExtractor;
                    // String[] paragraphText = wordExtractor.getParagraphText();
                    // for (String paragraph : paragraphText) {
                    // System.out.println(paragraph);
                    // }
                    // // Display the document's header and footer text
                    // System.out.println("Footer text: " + wordExtractor.getFooterText());
                    // System.out.println("Header text: " + wordExtractor.getHeaderText());
                    target.write(wordExtractor.getText());
                } else if (textExtractor instanceof org.apache.poi.xwpf.extractor.XWPFWordExtractor) {
                    org.apache.poi.xwpf.extractor.XWPFWordExtractor wordExtractor = (org.apache.poi.xwpf.extractor.XWPFWordExtractor) textExtractor;
                    // String[] paragraphText = wordExtractor.getParagraphText();
                    // for (String paragraph : paragraphText) {
                    // System.out.println(paragraph);
                    // }
                    // // Display the document's header and footer text
                    // System.out.println("Footer text: " + wordExtractor.getFooterText());
                    // System.out.println("Header text: " + wordExtractor.getHeaderText());
                    target.write(wordExtractor.getText());
                }
                // PowerPoint Presentation.
                else if (textExtractor instanceof org.apache.poi.hslf.extractor.PowerPointExtractor) {
                    org.apache.poi.hslf.extractor.PowerPointExtractor powerPointExtractor = (org.apache.poi.hslf.extractor.PowerPointExtractor) textExtractor;
                    // System.out.println("Text: " + powerPointExtractor.getText());
                    // System.out.println("Notes: " + powerPointExtractor.getNotes());
                    target.write(powerPointExtractor.getText());
                } else if (textExtractor instanceof org.apache.poi.sl.extractor.SlideShowExtractor) {
                    org.apache.poi.sl.extractor.SlideShowExtractor slideShowExtractor = (org.apache.poi.sl.extractor.SlideShowExtractor) textExtractor;
                    target.write(slideShowExtractor.getText());
                } else if (textExtractor instanceof org.apache.poi.xslf.extractor.XSLFPowerPointExtractor) {
                    org.apache.poi.xslf.extractor.XSLFPowerPointExtractor powerPointExtractor = (org.apache.poi.xslf.extractor.XSLFPowerPointExtractor) textExtractor;
                    target.write(powerPointExtractor.getText());
                }
                // Visio Drawing
                else if (textExtractor instanceof org.apache.poi.hdgf.extractor.VisioTextExtractor) {
                    org.apache.poi.hdgf.extractor.VisioTextExtractor visioTextExtractor = (org.apache.poi.hdgf.extractor.VisioTextExtractor) textExtractor;
                    target.write(visioTextExtractor.getText());
                } else if (textExtractor instanceof org.apache.poi.xdgf.extractor.XDGFVisioExtractor) {
                    org.apache.poi.xdgf.extractor.XDGFVisioExtractor visioTextExtractor = (org.apache.poi.xdgf.extractor.XDGFVisioExtractor) textExtractor;
                    target.write(visioTextExtractor.getText());
                }
                // Outlook
                else if (textExtractor instanceof org.apache.poi.hsmf.extractor.OutlookTextExtactor) {
                    org.apache.poi.hsmf.extractor.OutlookTextExtactor outlookTextExtactor = (org.apache.poi.hsmf.extractor.OutlookTextExtactor) textExtractor;
                    target.write(outlookTextExtactor.getText());
                }
                //
                else {
                    new RuntimeException("cannot extract").printStackTrace();
                }
            }
        }
    }
}
