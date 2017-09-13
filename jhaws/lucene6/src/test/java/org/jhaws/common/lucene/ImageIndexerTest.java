package org.jhaws.common.lucene;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;
import org.jhaws.common.lucene.imaging.ImageSimilarities;
import org.junit.Test;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;

public class ImageIndexerTest {
    @Test
    public void test() {
        FilePath tmp = FilePath.createTempDirectory();
        FilePath xml = tmp.child("dubsreport.xml");
        new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_0.png").writeTo(tmp.child("lucene0.png"));
        new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_1.png").writeTo(tmp.child("lucene1.png"));
        new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_0b.jpg").writeTo(tmp.child("lucene_0b.jpg"));
        new FilePath(ImageIndexerTest.class.getClassLoader(), "lucene_1b.jpg").writeTo(tmp.child("lucene_1b.jpg"));
        ImageSimilarities results;
        results = new ImageIndexer().findDuplicates(tmp.child("dubsindex"), tmp, xml, 5.0, CEDD.class);
        results = new ImageIndexer().findDuplicates(null, tmp, xml, null, null);
        System.out.println("=========================");
        results.forEach(System.out::print);
        System.out.println("=========================");
        System.out.println(xml.readAll());
    }
}
