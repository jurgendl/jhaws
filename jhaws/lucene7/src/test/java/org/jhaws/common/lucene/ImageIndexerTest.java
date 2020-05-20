package org.jhaws.common.lucene;

import java.util.Arrays;
import java.util.List;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;
import org.jhaws.common.lucene.imaging.ImageSimilarities;
import org.junit.Test;

import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.imageanalysis.features.global.Gabor;
import net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor;

public class ImageIndexerTest {
	@Test
	public void test() {
		FilePath tmp = FilePath.createTempDirectory().getParentPath().child("" + System.currentTimeMillis())
				.createDirectory();
		System.out.println(tmp);
		new FilePath(ImageIndexerTest.class.getClassLoader(), "0.jpg").writeTo(tmp.child("0.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "1.jpg").writeTo(tmp.child("1.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "1.png").writeTo(tmp.child("1.png"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "1.gif").writeTo(tmp.child("1.gif"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "2a.jpg").writeTo(tmp.child("2a.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "2b.jpg").writeTo(tmp.child("2b.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "3.jpg").writeTo(tmp.child("3.jpg"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "3.png").writeTo(tmp.child("3.png"));
		new FilePath(ImageIndexerTest.class.getClassLoader(), "3.gif").writeTo(tmp.child("3.gif"));
		double max = 15.0;
		List<Class<? extends GlobalFeature>> methods = Arrays.asList(CEDD.class, FCTH.class, EdgeHistogram.class,
				ColorLayout.class, ScalableColor.class, /* Tamura.class, */ Gabor.class);
		ImageSimilarities results;
		System.out.println("==================================================");
		System.out.println("-------------- RUN 1 =============================");
		results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), Arrays.asList(tmp),
				tmp.child("dubsreport1.xml"), max, methods);
		System.out.println("==================================================");
		System.out.println("==================================================");
		results.forEach(System.out::print);
		System.out.println("==================================================");
		System.out.println("-------------- RUN 2 =============================");
		results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), Arrays.asList(tmp),
				tmp.child("dubsreport2.xml"), max, methods);
		System.out.println("==================================================");
		System.out.println("==================================================");
		results.forEach(System.out::print);
		System.out.println("==================================================");
		System.out.println("-------------- RUN 2 =============================");
	}
}
