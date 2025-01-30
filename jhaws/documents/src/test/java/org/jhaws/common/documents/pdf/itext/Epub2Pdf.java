package org.jhaws.common.documents.pdf.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.FilePath.Filters;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.lang.Value;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

public class Epub2Pdf {
	public static void main(String[] args) {
		try {
			String rootfolder = "?/";
			String sevenzipexe = "7z.exe";
			FilePath fp = new FilePath(rootfolder);
			FilePath sevenzip = new FilePath(sevenzipexe);
			fp.list(false, new Filters.ExtensionFilter("epub")).forEach(epub -> {
				HashMap<String, String> env = new HashMap<>();
				Value<Process> processHolder = new Value<>();
				Consumer<String> c = s -> System.out.println(s);
				String outdir = epub.dropExtension().getAbsolutePath();
				List<String> asList = Arrays.asList("\"" + sevenzip.getAbsolutePath() + "\"", "x", "-y",
						"-o" + "\"" + outdir + "\"", "\"" + epub.getAbsolutePath() + "\"");
				System.out.println(asList.stream().collect(Collectors.joining(" ")));
				Processes.process(
						Processes.config(asList, c).processHolder(processHolder).throwExitValue(true).env(env).dir(fp));
				FilePath op = new FilePath(outdir);
				FilePath images = op.child("OEBPS").child("images");
				if (!images.exists()) {
					images = op.child("OEBPS").child("image");
				}
				FilePath cover = images.list(h -> h.toString().contains("cover")).get(0);
				List<FilePath> igs = images.list(h -> !h.toString().contains("cover")).stream()
						.sorted((o1, o2) -> new CompareToBuilder().append(o1, o2).toComparison())
						.collect(Collectors.toList());
				igs.add(0, cover);
				FilePath pdfout = op.appendExtension("pdf");
				try (OutputStream out = pdfout.newOutputStream();) {
					PdfWriter writer = new PdfWriter(out);
					PdfDocument pdf = new PdfDocument(writer);
					Document document = new Document(pdf);
					igs.stream().forEach(img -> {
						try {
							ImageData data = ImageDataFactory.create(img.getAbsolutePath());
							Image image = new Image(data);
							document.add(image);
						} catch (Exception ex) {
							throw new RuntimeException(ex);
						}
					});
					document.close();
					out.flush();
					out.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
