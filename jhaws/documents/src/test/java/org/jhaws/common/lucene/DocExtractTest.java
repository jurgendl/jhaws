package org.jhaws.common.lucene;

import java.io.IOException;
import java.io.InputStream;
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
	public void testEpub() {
		test("don-quijoti-epub3.epub");
	}

	@Test
	public void testCsv() {
		test("file_example_ODS_5000.csv");
	}

	@Test
	public void testMobi() {
		FilePath in = new FilePath(getClass(), "docs/" + "don-quijoti.mobi");
		FilePath out = FilePath.getTempDirectory().child("don-quijoti.mobi.txt");
		ebook(in, out);
	}

	@Test
	public void testAzw3() {
		FilePath in = new FilePath(getClass(), "docs/" + "don-quijoti.azw3");
		FilePath out = FilePath.getTempDirectory().child("don-quijoti.azw3.txt");
		ebook(in, out);
	}

	@Test
	public void testFb2() {
		FilePath in = new FilePath(getClass(), "docs/" + "don-quijoti.fb2");
		FilePath out = FilePath.getTempDirectory().child("don-quijoti.fb2.txt");
		ebook(in, out);
	}

	private void ebook(FilePath in, FilePath out) {
		try {
			FileTextExtracterService s = new FileTextExtracterService();
			s.extract(in, out);
			System.out.println(in.getHumanReadableFileSize());
			System.out.println(out.getHumanReadableFileSize());
		} catch (IOException ex) {
			ex.printStackTrace();
			Assert.fail();
		}
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
			FilePath path = new FilePath(getClass(), "docs/" + docName);
			System.out.println(path.getHumanReadableFileSize());
			System.out.println(
					FilePath.getHumanReadableFileSize((long) TikaHelper.parse(path.newInputStream()).length()));
			System.out.println(
					FilePath.getHumanReadableFileSize((long) TikaHelper.parseToString(path.newInputStream()).length()));
			// System.out.println("---------------------------------------------");
			// System.out.println(TikaHelper.parse(getClass().getClassLoader().getResourceAsStream("docs/"
			// + docName)));
			// System.out.println("---------------------------------------------");
			// System.out.println(TikaHelper.parseToString(getClass().getClassLoader().getResourceAsStream("docs/"
			// + docName)));
			System.out.println("============================================");
		} catch (Exception ex) {
			ex.printStackTrace(System.out);
			Assert.fail("" + ex);
		}
	}

	@Test
	public void testPdfOptions() {
		InputStream in = getClass().getClassLoader().getResourceAsStream("docs/" + "file-example_PDF_1MB.pdf");
		testPdfOptions(in);
	}

	private void testPdfOptions(InputStream in) {
		FileTextExtracterService s = new FileTextExtracterService();
		FilePath tmp = FilePath.getTempDirectory().child(String.valueOf(System.currentTimeMillis()))
				.appendExtension("pdf");
		tmp.write(in);
		FilePath target = new FilePath(tmp).appendExtension("txt");
		List<FileTextExtracter> fileTextExtracters = s.getFileTextExtracters(tmp);
		fileTextExtracters.forEach(i -> {
			long start = System.currentTimeMillis();
			try {
				i.extract(tmp.newBufferedInputStream(), target);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.out.println(i.getClass() + " // " + target.getFileSize() + " // "
					+ (System.currentTimeMillis() - start) + "ms");
		});
	}
}
