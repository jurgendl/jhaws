package org.jhaws.common.lucene;

import org.jhaws.common.io.FilePath;
import org.jhaws.common.lucene.imaging.ImageIndexer;

public class ImageIndexerTest {
    public static void main(String[] args) {
        new ImageIndexer().findDuplicates(new FilePath("C:/tmp/dubsindex"), new FilePath("C:/tmp/dubs"), new FilePath("C:/tmp/dubsreport.txt"));
    }
}
