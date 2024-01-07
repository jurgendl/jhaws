package org.jhaws.common.documents.pdf.itext;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.jhaws.common.io.FilePath;

public class ImgToCbz {
	static String template = "<?xml version=\"1.0\"?>\r\n"
			+ "<ComicInfo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\r\n"
			+ "	<Title></Title>\r\n" + "	<Summary></Summary>\r\n" + "	<Number></Number>\r\n"
			+ "	<Count></Count>\r\n" + "	<Year></Year>\r\n" + "	<Month></Month>\r\n" + "	<Writer></Writer>\r\n"
			+ "	<Publisher></Publisher>\r\n" + "	<Genre></Genre>\r\n" + "	<BlackAndWhite></BlackAndWhite>\r\n"
			+ "	<Manga></Manga>\r\n" + "	<Characters></Characters>\r\n" + "	<PageCount></PageCount>\r\n"
			+ "	<Pages>\r\n"
			+ "		<Page Image=\"0\" Type=\"FrontCover\" ImageSize=\"0\" ImageWidth=\"0\" ImageHeight=\"0\" />\r\n"
			+ "	</Pages>\r\n" + "</ComicInfo>";

	static public byte[] cbz(String n, List<FilePath> images) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ZipOutputStream zout = new ZipOutputStream(out);) {
			// zout.setMethod(ZipOutputStream.STORED);
			// zout.setLevel(java.util.zip.Deflater.NO_COMPRESSION);
			// ByteArrayOutputStream metaOut = new ByteArrayOutputStream();
			// ComicDownloader.write(metaOut, meta, true);
			// zout.setComment(new String(metaOut.toByteArray(), "utf-8"));

			{
				// https://wiki.mobileread.com/wiki/CBR_and_CBZ#ComicBookInfo
				template = template.replace("<Title></Title>", "<Title>" + n + "</Title>");
				template = template.replace("<Summary></Summary>", "");
				template = template.replace("<Count></Count>", "");
				template = template.replace("<Year></Year>", "");
				template = template.replace("<Month></Month>", "");
				template = template.replace("<Writer></Writer>", "");
				template = template.replace("<Publisher></Publisher>", "");
				template = template.replace("<Genre></Genre>", "");
				template = template.replace("<BlackAndWhite></BlackAndWhite>", "<BlackAndWhite>No</BlackAndWhite>");
				template = template.replace("<Manga>No</Manga>", "<Manga>Yes</Manga>");
				template = template.replace("<Characters></Characters>", "");
				template = template.replace("<PageCount></PageCount>", "<PageCount>" + images.size() + "</PageCount>");
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < images.size(); j++) {
					FilePath image = images.get(j);
					BufferedImage bi = ImageIO.read(image.toFile());
					sb.append("<Page Image=\"" + j + "\" ImageSize=\"" + image.getFileSize() + "\" ImageWidth=\""
							+ bi.getWidth() + "\" ImageHeight=\"" + bi.getHeight() + "\" />\n");
				}
				template = template.replace(
						"<Page Image=\"0\" Type=\"FrontCover\" ImageSize=\"0\" ImageWidth=\"0\" ImageHeight=\"0\" />",
						sb.toString());
				byte[] ib = template.getBytes();

				ZipEntry zipentry = new ZipEntry("ComicInfo.xml");
				zipentry.setMethod(ZipEntry.DEFLATED);
				zout.putNextEntry(zipentry);
				zout.write(ib, 0, ib.length);
				zout.closeEntry();
			}

			images.stream().sorted().forEach(i -> {
				try {
					String iname = i.getName();
					ZipEntry zipentry = new ZipEntry(iname);
					zipentry.setMethod(ZipEntry.STORED);
					zipentry.setSize(i.getFileSize());
					zipentry.setCompressedSize(i.getFileSize());
					zipentry.setCrc(i.crc32());
					zout.putNextEntry(zipentry);
					byte[] ib = i.readAllBytes();
					zout.write(ib, 0, ib.length);
					zout.closeEntry();
				} catch (IOException ex) {
					throw new UncheckedIOException(ex);
				}
			});

			out.flush();
			return out.toByteArray();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}
}
