package org.jhaws.common.lucene;

import java.io.IOException;
import java.util.List;

import org.jhaws.common.documents.FileTextExtracter;
import org.jhaws.common.documents.FileTextExtracterService;
import org.jhaws.common.documents.TikaHelper;
import org.jhaws.common.io.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class DocExtractTest {
    @Test
    public void testXml() {
        test("../logback.xml");
    }

    @Test
    public void testHtml() {
        test("html.html");
    }

    @Test
    public void testOdp() {
        test("file_example_ODP_1MB.odp");
    }

    @Test
    public void testOds() {
        test("file_example_ODS_5000.ods");
    }

    @Test
    public void testPpt() {
        test("file_example_PPT_1MB.ppt");
    }

    @Test
    public void testPptx() {
        test("file_example_PPT_1MB.pptx");
    }

    @Test
    public void testXls() {
        test("file_example_XLS_5000.xls");
    }

    @Test
    public void testXlsx() {
        test("file_example_XLSX_5000.xlsx");
    }

    @Test
    public void testPdf() {
        test("file-example_PDF_1MB.pdf");
    }

    @Test
    public void testDoc() {
        test("file-sample_1MB.doc");
    }

    @Test
    public void testDocx() {
        test("file-sample_1MB.docx");
    }

    @Test
    public void testOdf() {
        test("file-sample_1MB.odt");
    }

    @Test
    public void testRtf() {
        test("file-sample_1MB.rtf");
    }

    private void test(String docName) {
        try {
            System.out.println(docName);
            System.out.println(TikaHelper.parse(getClass().getClassLoader().getResourceAsStream("docs/" + docName)).length());
            System.out.println(TikaHelper.parseToString(getClass().getClassLoader().getResourceAsStream("docs/" + docName)).length());
            // System.out.println("---------------------------------------------");
            // System.out.println(TikaHelper.parse(getClass().getClassLoader().getResourceAsStream("docs/" + docName)));
            // System.out.println("---------------------------------------------");
            // System.out.println(TikaHelper.parseToString(getClass().getClassLoader().getResourceAsStream("docs/" + docName)));
            System.out.println("============================================");
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            Assert.fail("" + ex);
        }
    }

    @Test
    public void testPdfOptions() {
        FileTextExtracterService s = new FileTextExtracterService();
        FilePath tmp = FilePath.getTempDirectory().child(String.valueOf(System.currentTimeMillis())).appendExtension("pdf");
        tmp.write(getClass().getClassLoader().getResourceAsStream("docs/" + "file-example_PDF_1MB.pdf"));
        FilePath target = new FilePath(tmp).appendExtension("txt");
        List<FileTextExtracter> fileTextExtracters = s.getFileTextExtracters(tmp);
        fileTextExtracters.forEach(i -> {
            System.out.println(i);
            long start = System.currentTimeMillis();
            try {
                i.extract(tmp.newBufferedInputStream(), target);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(target.getFileSize());
            System.out.println((System.currentTimeMillis() - start) + "ms");
        });
    }
}
