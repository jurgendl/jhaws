package org.jhaws.common.lucene;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;
import org.jhaws.common.lucene.imaging.ImageSimilarities;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;

public class ImageIndexerTest {
    public static void main(String[] args) {
        FilePath xml = new FilePath("c:/tmp/dubsreport.xml");
        ImageSimilarities results = new ImageIndexer().findDuplicates(new FilePath("c:/tmp/dubsindex"), new FilePath("c:/tmp/dubs"), xml, 5.0,
                CEDD.class);
        results = new ImageIndexer().findDuplicates(null, new FilePath("c:/tmp/dubs"), null, null, null);
        System.out.println("=========================");
        results.forEach(System.out::print);
        System.out.println("=========================");
        System.out.println(xml.readAll());
    }
}
