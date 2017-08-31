package org.jhaws.common.lucene.imaging;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.jhaws.common.io.FilePath;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder.HashingMode;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import net.semanticmetadata.lire.utils.FileUtils;

// http://www.semanticmetadata.net/lire/
// https://github.com/dermotte/LIRE/blob/master/src/main/docs/developer-docs/docs/index.md
// http://www.itec.uni-klu.ac.at/~mlux/lire-release/
// https://github.com/dermotte/LIRE
// https://github.com/dermotte/LIRE/blob/master/src/main/docs/developer-docs/docs/hashing.md
// https://github.com/dermotte/LIRE/blob/master/src/main/docs/developer-docs/docs/metricindexing.md
// https://blog.mayflower.de/1755-Image-similarity-search-with-LIRE.html
// http://blog.thedigitalgroup.com/rajendras/2015/06/19/lire-lucene-image-retrieval/
public class ImageIndexer {
    public static void main(String[] args) {
        new ImageIndexer().findDuplicates(new FilePath(args[0]), new FilePath(args[1]), new FilePath(args[2]),Double.parseDouble(args[3]));
    }

    public void findDuplicates(FilePath index, FilePath root, FilePath report,Double max) {
        try {
            index.deleteAllIfExists();
            List<String> images = FileUtils.getAllImages(root.toFile(), false);
            if (images == null) images = Collections.<String> emptyList();
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None, false);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.CEDD.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.FCTH.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.Tamura.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.Gabor.class);
            globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.joint.JointHistogram.class);
            IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
            IndexWriter iw = new IndexWriter(FSDirectory.open(index.getPath()), conf);
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
            System.out.println();
            IndexReader ir = DirectoryReader.open(FSDirectory.open(index.getPath()));
            ImageSearcher searcher = new GenericFastImageSearcher(100, CEDD.class, true, ir);
            BufferedWriter out = report.newBufferedWriter();
            List<String> done = new ArrayList<>();
            for (Iterator<String> it = images.iterator(); it.hasNext();) {
                String imageFilePath = it.next();
                if (done.contains(imageFilePath)) continue;
                done.add(imageFilePath);
                System.out.println("Searching duplicates of " + imageFilePath);
                BufferedImage img = ImageIO.read(new FilePath(imageFilePath).toFile());
                ImageSearchHits hits = searcher.search(img, ir);
                for (int i = 0; i < hits.length(); i++) {
                    String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
                    double score = hits.score(i);
                    if (score > max) {
                        done.add(fileName);
                    	continue;
                    }
                    if (imageFilePath.equals(fileName)) continue;
                    System.out.println(score + ": \t" + fileName);
                    out.write(imageFilePath + "\t" + score + "\t" + fileName + "\n");
                }
                System.out.println();
                out.flush();
            }
            out.close();
            System.out.println("============================= done ============================= ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
