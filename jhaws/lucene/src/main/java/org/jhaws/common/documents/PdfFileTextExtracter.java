package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;

public class PdfFileTextExtracter implements FileTextExtracter {
	private FilePath xpdfexe;

	@Override
	public void extract(InputStream stream, FilePath target) throws IOException {
		FilePath tmp = FilePath.createDefaultTempFile(target.getFileNameString(), "pdf");
		tmp.write(stream);
		extract(tmp, target);
	}

	@Override
	public void extract(FilePath pdf, FilePath txt) {
		LocalDateTime pdfLastModifiedDateTime = pdf.getLastModifiedDateTime();
		if (txt.notExists() || pdfLastModifiedDateTime.isAfter(txt.getLastModifiedDateTime())) {
			System.out.println(pdf);
			extractpdftext(getXpdfexe(), pdf, txt);
			System.out.println(txt);
		}
	}

	public static void extractpdftext(FilePath xpdfexe, FilePath pdf, FilePath txt) {
		if (txt == null) {
			txt = pdf.appendExtension("txt");
		} else {
			txt.getParentPath().createDirectoryIfNotExists();
		}
		Processes.callProcess(true,
				Arrays.asList("\"" + xpdfexe.getAbsolutePath() + "\"", "-enc", "Latin1", "-eol", "dos",
						// "-nopgbrk", // "\u000C" // FORM FEED (FF)
						"\"" + pdf.getAbsolutePath() + "\"", "\"" + txt.getAbsolutePath() + "\""),
				txt.getParentPath(), new Processes.Log());
	}

	public static FilePath xpdf(String version) throws MalformedURLException, IOException {
		FilePath xpdf = new FilePath(System.getProperty("user.home")).child("xpdfbin-win-" + version).createDirectoryIfNotExists();
		FilePath xpdfzip = xpdf.child("xpdfbin-win-" + version + ".zip");
		if (xpdfzip.notExists()) {
			// http://www.foolabs.com/xpdf/
			FilePath tmp = xpdfzip.appendExtension("partial");
			tmp.download(new URL("ftp://ftp.foolabs.com/pub/xpdf/xpdfbin-win-" + version + ".zip"));
			tmp.renameTo(xpdfzip);
		}
		FilePath xpdfexe = xpdf.child("xpdfbin-win-" + version).child("bin64").child("pdftotext.exe");
		if (xpdfexe.notExists()) {
			xpdfzip.unzip(xpdf);
		}
		return xpdfexe;
	}

	public FilePath getXpdfexe() {
		if (xpdfexe == null) {
			try {
				xpdfexe = xpdf("3.04");
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}
		return xpdfexe;
	}
}
