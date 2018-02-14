package org.jhaws.common.lucene.imaging;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Bits;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.lang.EnhancedHashMap;

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
    protected static final String NAME = "name";

    protected static final String LASTMOD = "lastmod";

    protected static final String SIZE = "size";

    protected static final String H = "h";

    protected static final String W = "w";

    protected static JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(ImageSimilarities.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            List<Class<? extends GlobalFeature>> f = new ArrayList<>();
            if (args.length > 4) {
                for (int i = 4; i < args.length; i++) {
                    System.out.println("**" + args[i] + "**");
                    if (!"null".equals(args[i])) {
                        f.add((Class<? extends GlobalFeature>) Class.forName("net.semanticmetadata.lire.imageanalysis.features.global." + args[i]));
                    }
                }
            }
            System.out.println(f);
            new ImageIndexer().findDuplicatesExt(//
                    "null".equals(args[0]) ? null : new FilePath(args[0]), //
                    new FilePath(args[1]), //
                    "null".equals(args[2]) ? null : new FilePath(args[2]), //
                    "null".equals(args[3]) ? 5.0 : Double.parseDouble(args[3]), //
                    f.isEmpty() ? null : f//
            );
        } catch (NumberFormatException ex) {
            ex.printStackTrace(System.out);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public ImageSimilarities findDuplicates(FilePath index, FilePath root, FilePath report, Double max, Class<? extends GlobalFeature> feature) {
        return findDuplicatesExt(index, root, report, max, feature == null ? null : Arrays.asList(feature));
    }

    public ImageSimilarities findDuplicatesExt(FilePath index, FilePath root, FilePath report, Double max,
            List<Class<? extends GlobalFeature>> features) {
        ImageSimilarities sim = new ImageSimilarities();
        if (features == null) {
            features = Arrays.asList(net.semanticmetadata.lire.imageanalysis.features.global.FCTH.class);
        }
        System.out.println(features);
        if (max == null) {
            max = 5.0;
        }
        EnhancedHashMap<ImageSimilarity, ImageSimilarity> results = new EnhancedHashMap<>();
        Map<Class<? extends GlobalFeature>, String> names = new HashMap<>();
        features.forEach(feature -> {
            try {
                names.put(feature, feature.newInstance().getFeatureName().toString());
            } catch (Exception ex) {
                names.put(feature, feature.getName());
            }
        });
        try {
            List<String> images = FileUtils.getAllImages(root.toFile(), false);
            if (images == null) images = Collections.<String> emptyList();
            GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None, false);
            features.forEach(globalDocumentBuilder::addExtractor);
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
            WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
            IndexWriterConfig conf = new IndexWriterConfig(analyzer);
            BaseDirectory fsDir;
            if (index == null) {
                fsDir = new RAMDirectory();
                System.out.println("RAM");
            } else {
                fsDir = NIOFSDirectory.open(index.getPath());
                System.out.println(index.getAbsolutePath());
            }
            System.out.println("directory openened");
            try {
                Map<String, Long> data = new HashMap<>();
                try {
                    IndexReader ir = DirectoryReader.open(fsDir);
                    System.out.println("reader openened");
                    try {
                        data = list(ir);
                    } finally {
                        ir.close();
                        System.out.println("reader closed " + !ir.hasDeletions());
                    }
                } catch (org.apache.lucene.index.IndexNotFoundException ex) {
                    //
                }
                {
                    IndexWriter iw = new IndexWriter(fsDir, conf);
                    System.out.println("writer openened");
                    try {
                        List<String> ids = index(analyzer, data, images, globalDocumentBuilder, iw);
                        System.out.println("#" + iw.numDocs());
                        Set<String> existing = new HashSet<>(data.keySet());
                        existing.removeAll(ids);
                        cleanup(analyzer, iw, existing);
                    } finally {
                        iw.commit();
                        iw.close();
                        System.out.println("writer closed " + !iw.hasUncommittedChanges() + " " + !iw.hasPendingMerges());
                    }
                }
                {
                    IndexReader ir = DirectoryReader.open(fsDir);
                    System.out.println("reader openened");
                    try {
                        sim(ir, max, features, results, names, images, fsDir);
                    } finally {
                        ir.close();
                        System.out.println("reader closed " + !ir.hasDeletions());
                    }
                }
            } finally {
                fsDir.close();
                System.out.println("directory closed");
            }
            results.keySet().stream().sorted().forEach(sim.getImageSimilarities()::add);
            if (report != null) {
                jaxbMarshalling.marshall(sim, report.toPath());
                System.out.println("-------------------------------------------------");
                System.out.println(report.readAll());
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            System.out.println("============================= done ============================= ");
        }
        return sim;
    }

    protected void cleanup(WhitespaceAnalyzer analyzer, IndexWriter iw, Set<String> existing) {
        existing.forEach(id -> deleteDocument(analyzer, iw, id));
    }

    protected Map<String, Long> list(IndexReader ir) {
        Map<String, Long> data = new HashMap<>();
        Bits liveDocs = MultiFields.getLiveDocs(ir);
        for (int i = 0; i < ir.maxDoc(); i++) {
            if (liveDocs != null && !liveDocs.get(i)) continue;
            try {
                Document doc = ir.document(i);
                String name = doc.getField(NAME).stringValue();
                long lastmod = doc.getField(LASTMOD).numericValue().longValue();
                data.put(name, lastmod);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }
        return data;
    }

    protected List<String> index(WhitespaceAnalyzer analyzer, Map<String, Long> data, List<String> images,
            GlobalDocumentBuilder globalDocumentBuilder, IndexWriter iw) {
        int x = 0;
        long start = System.currentTimeMillis();
        List<String> ids = new ArrayList<>();
        for (Iterator<String> it = images.iterator(); it.hasNext();) {
            long start2 = System.currentTimeMillis();
            FilePath ifp = new FilePath(it.next());
            String id = ifp.getAbsolutePath().replace("\\", "/");
            ids.add(id);
            indexPerImage(analyzer, data, globalDocumentBuilder, iw, ifp, id);
            x++;
            System.out.println("" + x + "/" + images.size() + " - " + (ifp == null ? null : ifp.getHumanReadableFileSize()) + " - "
                    + (System.currentTimeMillis() - start2));
        }
        System.out.println("--- " + (System.currentTimeMillis() - start) + "----");
        System.out.println();
        return ids;
    }

    protected void indexPerImage(WhitespaceAnalyzer analyzer, Map<String, Long> data, GlobalDocumentBuilder globalDocumentBuilder, IndexWriter iw,
            FilePath ifp, String id) {
        try {
            Long lastmod = data.get(id);
            if (lastmod != null) {
                if (ifp.getLastModifiedTime().toMillis() > lastmod) {
                    System.out.println("Indexing: newer: " + id);
                    deleteDocument(analyzer, iw, id);
                    indexPerImagePerformIndexing(globalDocumentBuilder, iw, ifp, id);
                } else {
                    System.out.println("Indexing: no update needed: " + id);
                }
            } else {
                System.out.println("Indexing: new: " + id);
                indexPerImagePerformIndexing(globalDocumentBuilder, iw, ifp, id);
            }
        } catch (Exception e) {
            System.out.println("Error reading image or indexing it. " + id + ": " + e);
        }
    }

    protected void deleteDocument(WhitespaceAnalyzer analyzer, IndexWriter iw, String id) {
        System.out.println("deleting " + id);
        try {
            iw.deleteDocuments(new QueryParser(NAME, analyzer).parse(id));
            iw.commit();
        } catch (IOException | ParseException ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected void indexPerImagePerformIndexing(GlobalDocumentBuilder globalDocumentBuilder, IndexWriter iw, FilePath ifp, String id)
            throws IOException {
        try (InputStream in = ifp.newInputStream()) {
            BufferedImage img = ImageIO.read(in);
            int aW = img.getWidth();
            int aH = img.getHeight();
            long aSize = ifp.getFileSize();
            Document document = globalDocumentBuilder.createDocument(img, id);
            document.add(new StringField(NAME, id, Store.YES));
            document.add(new StoredField(W, aW));
            document.add(new StoredField(H, aH));
            document.add(new StoredField(SIZE, aSize));
            document.add(new StoredField(LASTMOD, ifp.getLastModifiedTime().toMillis()));
            iw.addDocument(document);
            iw.commit();
        }
    }

    protected void sim(IndexReader ir, Double max, List<Class<? extends GlobalFeature>> features,
            EnhancedHashMap<ImageSimilarity, ImageSimilarity> results, Map<Class<? extends GlobalFeature>, String> names, List<String> images,
            BaseDirectory fsDir) throws IOException, FileNotFoundException {
        for (Iterator<String> it = images.iterator(); it.hasNext();) {
            simPerImage(features, results, names, max, ir, it);
        }
    }

    protected void simPerImage(List<Class<? extends GlobalFeature>> features, EnhancedHashMap<ImageSimilarity, ImageSimilarity> results,
            Map<Class<? extends GlobalFeature>, String> names, double max, IndexReader ir, Iterator<String> it)
            throws IOException, FileNotFoundException {
        FilePath ifp = new FilePath(it.next());
        String id = ifp.getAbsolutePath().replace("\\", "/");
        System.out.println("Searching duplicates of " + id);
        try (InputStream in = ifp.newInputStream()) {
            BufferedImage img = ImageIO.read(in);
            int aW = img.getWidth();
            int aH = img.getHeight();
            long aSize = ifp.getFileSize();
            features.forEach(feature -> simPerImagePerFeature(results, names, max, ir, id, img, aW, aH, aSize, feature));
        }
    }

    protected void simPerImagePerFeature(EnhancedHashMap<ImageSimilarity, ImageSimilarity> results, Map<Class<? extends GlobalFeature>, String> names,
            double max, IndexReader ir, String id, BufferedImage img, int aW, int aH, long aSize, Class<? extends GlobalFeature> feature) {
        try {
            System.out.println("method " + names.get(feature));
            ImageSearcher searcher = new GenericFastImageSearcher(100, feature, true, ir);
            ImageSearchHits hits = searcher.search(img, ir);
            for (int i = 0; i < hits.length(); i++) {
                simPerImagePerFeaturePerHit(results, names, max, ir, id, aW, aH, aSize, feature, hits, i);
            }
            System.out.println();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected void simPerImagePerFeaturePerHit(EnhancedHashMap<ImageSimilarity, ImageSimilarity> results,
            Map<Class<? extends GlobalFeature>, String> names, double max, IndexReader ir, String id, int aW, int aH, long aSize,
            Class<? extends GlobalFeature> feature, ImageSearchHits hits, int i) throws IOException {
        Document document = ir.document(hits.documentID(i));
        String fileName = document.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
        double score = hits.score(i);
        if (!(score > max)) {
            if (!(id.equals(fileName))) {
                System.out.println(score + ": \t" + fileName);
                String similarityType = names.get(feature);
                int bW = document.getField(W).numericValue().intValue();
                int bH = document.getField(H).numericValue().intValue();
                long bSize = document.getField(SIZE).numericValue().intValue();
                ImageSimilarity e = new ImageSimilarity(id, fileName, score, new int[] { aW, aH }, new int[] { bW, bH }, aSize, bSize,
                        similarityType);
                if (results.containsKey(e)) {
                    ImageSimilarity exist = results.get(e);
                    results.remove(e);
                    exist.addSimilarity(names.get(feature), score);
                    System.out.println(exist);
                    results.put(exist, exist);
                } else {
                    System.out.println(e);
                    results.put(e, e);
                }
            }
        }
    }
}
