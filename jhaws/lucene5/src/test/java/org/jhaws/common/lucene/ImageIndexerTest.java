package org.jhaws.common.lucene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder.HashingMode;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.utils.FileUtils;

// https://github.com/dermotte/LIRE
public class ImageIndexerTest {
    public static void main(String[] args) {
        try {
            ArrayList<String> images = FileUtils.getAllImages(new File("C:/tmp/dubs"), false);
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None, false);
            globalDocumentBuilder.addExtractor(CEDD.class);
            globalDocumentBuilder.addExtractor(FCTH.class);
            globalDocumentBuilder.addExtractor(AutoColorCorrelogram.class);
            IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
            IndexWriter iw = new IndexWriter(FSDirectory.open(Paths.get("C:/tmp/dubsindex")), conf);
            for (Iterator<String> it = images.iterator(); it.hasNext();) {
                String imageFilePath = it.next();
                System.out.println("Indexing " + imageFilePath);
                try {
                    BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                    Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                    iw.addDocument(document);
                } catch (Exception e) {
                    System.err.println("Error reading image or indexing it.");
                    e.printStackTrace();
                }
            }
            iw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
