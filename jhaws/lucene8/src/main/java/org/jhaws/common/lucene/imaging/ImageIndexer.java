package org.jhaws.common.lucene.imaging;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jhaws.common.io.jaxb.JAXBMarshalling;
import org.jhaws.common.lang.EnhancedHashMap;

import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder.HashingMode;
import net.semanticmetadata.lire.imageanalysis.features.GlobalFeature;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
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
	private static JAXBMarshalling jaxbMarshalling = new JAXBMarshalling(ImageSimilarities.class);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			List<Class<? extends GlobalFeature>> f = new ArrayList<>();
			if (args.length > 4) {
				for (int i = 4; i < args.length; i++) {
					System.out.println("**" + args[i] + "**");
					if (!"null".equals(args[i])) {
						f.add((Class<? extends GlobalFeature>) Class
								.forName("net.semanticmetadata.lire.imageanalysis.features.global." + args[i]));
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
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public ImageSimilarities findDuplicates(FilePath index, FilePath root, FilePath report, Double max,
			Class<? extends GlobalFeature> feature) {
		return findDuplicatesExt(index, root, report, max, feature == null ? null : Arrays.asList(feature));
	}

	public ImageSimilarities findDuplicatesExt(FilePath index, FilePath root, FilePath report, Double max,
			List<Class<? extends GlobalFeature>> features) {
		ImageSimilarities sim = new ImageSimilarities();
		if (features == null) {
			features = Arrays.asList(net.semanticmetadata.lire.imageanalysis.features.global.FCTH.class);
		}
		System.out.println(features);
		if (max == null)
			max = 5.0;
		EnhancedHashMap<ImageSimilarity, ImageSimilarity> results = new EnhancedHashMap<>();
		Map<String, int[]> wh = new HashMap<>();
		Map<String, Long> size = new HashMap<>();
		Map<Class<? extends GlobalFeature>, String> names = new HashMap<>();
		features.forEach(feature -> {
			try {
				names.put(feature, feature.getDeclaredConstructor().newInstance().getFeatureName().toString());
			} catch (Exception ex) {
				names.put(feature, feature.getName());
			}
		});
		try {
			List<String> images = FileUtils.getAllImages(root.toFile(), false);
			if (images == null)
				images = Collections.<String>emptyList();
			if (index != null)
				index.delete();
			GlobalDocumentBuilder nonGrayScaleGlobalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None,
					false);
			features.forEach(nonGrayScaleGlobalDocumentBuilder::addExtractor);
			GlobalDocumentBuilder grayScaleGlobalDocumentBuilder = new GlobalDocumentBuilder(false, HashingMode.None,
					false);
			features.stream().filter(feature -> !FCTH.class.equals(feature))
					.forEach(grayScaleGlobalDocumentBuilder::addExtractor);
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
			int x = 0;
			long start0 = System.currentTimeMillis();
			for (String imageFilePath : images) {
				System.out.println("Indexing " + imageFilePath);
				long start = System.currentTimeMillis();
				BufferedImage img = null;
				try {
					img = ImageIO.read(new FileInputStream(imageFilePath));
					boolean grayscale = checkGrayscale(img);
					wh.put(imageFilePath, new int[] { img.getWidth(), img.getHeight() });
					size.put(imageFilePath, new FilePath(imageFilePath).getFileSize());
					Document document = (grayscale ? grayScaleGlobalDocumentBuilder : nonGrayScaleGlobalDocumentBuilder)
							.createDocument(img, imageFilePath);
					iw.addDocument(document);
					iw.commit();
				} catch (Exception e) {
					System.err.println("Error reading image or indexing it. " + imageFilePath);
					e.printStackTrace();
				}
				x++;
				System.out.println(
						"" + x + "/" + images.size() + " - " + new FilePath(imageFilePath).getHumanReadableFileSize()
								+ " - " + (System.currentTimeMillis() - start));
				if (img != null) {
					BufferedImage _img = img;
					Double _max = max;
					System.out.println("Searching duplicates of " + imageFilePath);
					IndexReader ir = DirectoryReader.open(iw);
					boolean grayscale = checkGrayscale(_img);
					features.stream().filter(feature -> !grayscale || !FCTH.class.equals(feature)).forEach(feature -> {
						try {
							System.out.println("method " + names.get(feature));
							ImageSearcher searcher = new GenericFastImageSearcher(100, feature, true, ir);
							ImageSearchHits hits = searcher.search(_img, ir);
							for (int i = 0; i < hits.length(); i++) {
								String fileName = ir.document(hits.documentID(i))
										.getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
								double score = hits.score(i);
								if (score > _max) {
									continue;
								}
								if (imageFilePath.equals(fileName)) {
									continue;
								}
								System.out.println(score + ": \t" + fileName);
								ImageSimilarity e = new ImageSimilarity(imageFilePath, fileName, score,
										wh.get(imageFilePath), wh.get(fileName), size.get(imageFilePath),
										size.get(fileName), names.get(feature));
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
							System.out.println();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					});
					ir.close();
				}
			}
			iw.close();
			System.out.println("--- " + (System.currentTimeMillis() - start0) + "----");
			System.out.println();
			results.keySet().stream().sorted().forEach(sim.getImageSimilarities()::add);
			if (report != null) {
				jaxbMarshalling.marshall(sim, report.toPath());
				System.out.println("-------------------------------------------------");
				System.out.println(report.readAll());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("============================= done ============================= ");
		}
		return sim;
	}

	private boolean checkGrayscale(BufferedImage image) {
		Raster ras = image.getRaster();

		// Number of Color elements
		@SuppressWarnings("unused")
		int elem = ras.getNumDataElements();

		int width = image.getWidth();
		int height = image.getHeight();

		int pixel, red, green, blue;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// scan through each pixel
				pixel = image.getRGB(i, j);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;

				// check if R=G=B
				if (red != green || green != blue) {
					return true;
				}
			}
		}
		return false;
	}
}
