package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jhaws.common.io.FilePath;

public class OfficeXlsxFileTextExtracter implements FileTextExtracter {
	@Override
	public List<String> accepts() {
		return Arrays.asList("xlsx");
	}

	@Override
	public void extract(InputStream inputFile, FilePath target) throws IOException {
		XSSFWorkbook xlsx = new XSSFWorkbook(inputFile);
		try (XSSFExcelExtractor xe = new XSSFExcelExtractor(xlsx);) {
			target.write(xe.getText());
		}
	}
}
