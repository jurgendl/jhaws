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
        FilePath tmp = FilePath.createTempDirectory().getParentPath().child("" + System.currentTimeMillis()).createDirectory();
        System.out.println(tmp);
        for (int i = 0; i < 10; i++) {
            new FilePath(ImageIndexerTest.class.getClassLoader(), i + ".jpg").writeTo(tmp.child(i + ".jpg"));
        }
        double max = 15.0;
        ImageSimilarities results;
        System.out.println("==================================================");
        System.out.println("-------------- RUN 1 ==================================================");
        results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), tmp, tmp.child("dubsreport1.xml"), max,
                Arrays.asList(CEDD.class, FCTH.class, EdgeHistogram.class, ColorLayout.class, ScalableColor.class, Tamura.class, Gabor.class));
        System.out.println("==================================================");
        System.out.println("==================================================");
        results.forEach(System.out::print);
        System.out.println("==================================================");
        System.out.println("-------------- RUN 2 ==================================================");
        results = new ImageIndexer().findDuplicatesExt(tmp.child("dubsindex"), tmp, tmp.child("dubsreport2.xml"), max,
                Arrays.asList(CEDD.class, FCTH.class, EdgeHistogram.class, ColorLayout.class, ScalableColor.class, Tamura.class, Gabor.class));
        System.out.println("==================================================");
        System.out.println("==================================================");
    }
}
