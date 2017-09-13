package org.jhaws.common.lucene;

import java.util.Arrays;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;
import org.jhaws.common.lucene.imaging.ImageSimilarities;
import org.junit.Test;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;

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
        ImageSimilarities results;
        results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), tmp, xml, 5.0, Arrays.asList(CEDD.class, FCTH.class));
        System.out.println("=========================");
        results.forEach(System.out::print);
        System.out.println("=========================");
        results = new ImageIndexer().findDuplicates(null, tmp, xml, null, CEDD.class);
        System.out.println("=========================");
        results.forEach(System.out::print);
        System.out.println("=========================");
        results = new ImageIndexer().findDuplicates(null, tmp, xml, null, FCTH.class);
        System.out.println("=========================");
        results.forEach(System.out::print);
        System.out.println("=========================");
    }
}
