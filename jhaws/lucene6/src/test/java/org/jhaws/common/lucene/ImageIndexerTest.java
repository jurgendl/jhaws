package org.jhaws.common.lucene;

import java.util.Arrays;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;
import org.jhaws.common.lucene.imaging.ImageSimilarities;
import org.junit.Test;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.global.Gabor;
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor;
import net.semanticmetadata.lire.imageanalysis.features.global.Tamura;

public class ImageIndexerTest {
	@Test
	public void test() {
		FilePath tmp = FilePath.createTempDirectory();
		FilePath xml = tmp.child("dubsreport.xml");
		new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_0.jpg").writeTo(tmp.child("lucene_0.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_1.jpg").writeTo(tmp.child("lucene_1.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_2.png").writeTo(tmp.child("lucene_2.png"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_3.jpg").writeTo(tmp.child("lucene_3.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_4.jpg").writeTo(tmp.child("lucene_4.jpg"));
		// new FilePath(ImageIndexerTest.class.getClassLoader(),
		// "lucene_5.gif").writeTo(tmp.child("lucene_5.gif"));
		ImageSimilarities results;
		results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), Arrays.asList(tmp), xml, 5.0,
				Arrays.asList(CEDD.class, FCTH.class, EdgeHistogram.class, ColorLayout.class, ScalableColor.class,
						Tamura.class, Gabor.class));
		System.out.println("=========================");
		results.forEach(System.out::print);
		System.out.println("=========================");
	}

	public static void main(String[] args) {
		FilePath p = new FilePath(args.length == 0 ? "src/test/resources/" : args[0]);
		FilePath xml = p.child("dubsreport.xml");
		ImageSimilarities results = new ImageIndexer().findDuplicatesExt(p.child("dubsindex"), Arrays.asList(p), xml,
				5.0, Arrays.asList(CEDD.class, FCTH.class, EdgeHistogram.class, ColorLayout.class, ScalableColor.class,
						Tamura.class, Gabor.class));
		System.out.println("=========================");
		results.forEach(System.out::print);
		System.out.println("=========================");
	}
}
