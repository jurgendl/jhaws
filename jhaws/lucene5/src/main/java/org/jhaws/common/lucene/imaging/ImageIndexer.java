package org.jhaws.common.lucene.imaging;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.jhaws.common.io.FilePath;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder.HashingMode;
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
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
// https://github.com/aoldemeier/image-similarity-with-lire/blob/master/src/main/java/de/mayflower/samplecode/SimilaritySearchWithLIRE/SimilaritySearchWithLIRE.java
// https://www.researchgate.net/publication/221573372_Lire_lucene_image_retrieval_an_extensible_java_CBIR_library
public class ImageIndexer {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            new ImageIndexer().findDuplicates(//
                    "null".equals(args[0]) ? null : new FilePath(args[0]), //
                    new FilePath(args[1]), //
                    "null".equals(args[2]) ? null : new FilePath(args[2]), //
                    "null".equals(args[3]) ? 5.0 : Double.parseDouble(args[3]), //
                    (Class<? extends GlobalFeature>) Class
                            .forName("net.semanticmetadata.lire.imageanalysis.features.global." + ("null".equals(args[4]) ? "FCTH" : args[4]))//
            );
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public SortedSet<ImageSimilarity> findDuplicates(FilePath index, FilePath root, FilePath report, Double max,
            Class<? extends GlobalFeature> feature) {
        SortedSet<ImageSimilarity> results = new TreeSet<>();
        try {
            List<String> images = FileUtils.getAllImages(root.toFile(), false);
            if (images == null) images = Collections.<String> emptyList();
            if (index != null) index.delete();
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None, false);
            globalDocumentBuilder.addExtractor(feature);
            // globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.CEDD.class);
            // globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.FCTH.class);
            // globalDocumentBuilder
            // .addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout.class);
            // globalDocumentBuilder
            // .addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.EdgeHistogram.class);
            // globalDocumentBuilder
            // .addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.ScalableColor.class);
            // globalDocumentBuilder
            // .addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram.class);
            // globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.Tamura.class);
            // globalDocumentBuilder.addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.Gabor.class);
            // globalDocumentBuilder
            // .addExtractor(net.semanticmetadata.lire.imageanalysis.features.global.joint.JointHistogram.class);
            IndexWriterConfig conf = new IndexWriterConfig(new WhitespaceAnalyzer());
            BaseDirectory fsDir;
            if (index == null) {
                fsDir = new RAMDirectory();
            } else {
                fsDir = FSDirectory.open(index.getPath());
            }
            IndexWriter iw = new IndexWriter(fsDir, conf);
            Document tmpDoc = new Document();
            iw.addDocument(tmpDoc);
            iw.deleteAll();
            iw.commit();
            BufferedWriter out = report == null ? null : report.newBufferedWriter(Charset.forName("utf8"));
            int x = 0;
            long start0 = System.currentTimeMillis();
            for (Iterator<String> it = images.iterator(); it.hasNext();) {
                String imageFilePath = it.next();
                System.out.println("Indexing " + imageFilePath);
                long start = System.currentTimeMillis();
                BufferedImage img = null;
                try {
                    img = ImageIO.read(new FileInputStream(imageFilePath));
                    Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                    iw.addDocument(document);
                    iw.commit();
                } catch (Exception e) {
                    System.err.println("Error reading image or indexing it. " + imageFilePath);
                    e.printStackTrace();
                }
                x++;
                System.out.println("" + x + "/" + images.size() + " - " + new FilePath(imageFilePath).getHumanReadableByteCount() + " - "
                        + (System.currentTimeMillis() - start));
                if (img != null) {
                    System.out.println("Searching duplicates of " + imageFilePath);
                    IndexReader ir = DirectoryReader.open(iw);
                    ImageSearcher searcher = new GenericFastImageSearcher(100, feature, true, ir);
                    ImageSearchHits hits = searcher.search(img, ir);
                    for (int i = 0; i < hits.length(); i++) {
                        String fileName = ir.document(hits.documentID(i)).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
                        double score = hits.score(i);
                        if (score > max) {
                            continue;
                        }
                        if (imageFilePath.equals(fileName)) {
                            continue;
                        }
                        System.out.println(score + ": \t" + fileName);
                        if (out != null) out.write(imageFilePath + "\t" + score + "\t" + fileName + "\r\n");
                        results.add(new ImageSimilarity(imageFilePath, fileName, score));
                    }
                    System.out.println();
                    if (out != null) out.flush();
                    ir.close();
                }
            }
            iw.close();
            System.out.println("--- " + (System.currentTimeMillis() - start0) + "----");
            System.out.println();
            if (out != null) out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("============================= done ============================= ");
        }
        return results;
    }
}