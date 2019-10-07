package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.io.win.WinRegistryAlt;

// AZW, CBC, CBR, CBZ, ePUB, FB2, HTM, HTML, LIT, LRF, MOBI, ODT, OPF, RB, PDB, PDF, PML, PMLZ, PRC, RECIPE, RTF, SHTM, SHTML, TCR, TXT, XHTM, XHTML
public class CalibreTextExtracter implements FileTextExtracter {
	FilePath exec;

	public CalibreTextExtracter() {
		try {
			String loc = WinRegistryAlt.readString(WinRegistryAlt.HKEY_LOCAL_MACHINE,
					"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\App Paths\\calibre.exe", "Path");
			exec = new FilePath(loc, "ebook-convert.exe");
		} catch (Exception ex) {
			try {
				exec = new FilePath(System.getenv("CALIBRE")).checkExists();
			} catch (Exception ex2) {
				System.err.println("${CALIBRE} executable not found");
			}
		}
	}

	@Override
	public List<String> accepts() {
		return Arrays.asList("mobi", "azw", "azw3", "cbc", "cbr", "cbz", "epub", "fb2");
	}

	@Override
	public void extract(InputStream stream, FilePath target) throws IOException {
		FilePath tmp = FilePath.createTempFile(target.getFileNameString(), "pdf");
		tmp.write(stream);
		extract(tmp, target);

	}

	@Override
	public void extract(FilePath file, FilePath txt) {
		LocalDateTime pdfLastModifiedDateTime = file.getLastModifiedDateTime();
		if (txt.notExists() || pdfLastModifiedDateTime.isAfter(txt.getLastModifiedDateTime())) {
			System.out.println(file);
			extractpdftext(exec, file, txt);
			System.out.println(txt);
		}
	}

	public static void extractpdftext(FilePath exe, FilePath file, FilePath txt) {
		if (txt == null) {
			txt = file.appendExtension("txt");
		} else {
			txt.getParentPath().createDirectory();
		}
		Processes
				.callProcess(
						null, true, Arrays.asList("\"" + exe.getAbsolutePath() + "\"",
								"\"" + file.getAbsolutePath() + "\"", "\"" + txt.getAbsolutePath() + "\""),
						txt.getParentPath(), new Processes.Log());
	}
}
