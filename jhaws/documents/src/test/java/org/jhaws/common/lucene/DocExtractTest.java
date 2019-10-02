package org.jhaws.common.lucene;

import java.io.InputStream;

import org.apache.tika.Tika;
import org.junit.Assert;
import org.junit.Test;

public class DocExtractTest {
	@Test
	public void testXml() {
		test("../logback.xml");
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
			InputStream doc = getClass().getClassLoader().getResourceAsStream("docs/" + docName);
			Tika tika = new Tika();
			System.out.println(tika.parseToString(doc).length());
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			Assert.fail("" + ex);
		}
	}
}
