package org.jhaws.common.lucene;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;

public class ImageIndexerTest {
	public static void main(String[] args) {
		new ImageIndexer().findDuplicates(new FilePath("d:/tmp/dubsindex"), new FilePath("F:/bbs/aaaaaaaaaa"),
				new FilePath("d:/tmp/dubsreport.txt"), 6.0, CEDD.class);
	}
}
