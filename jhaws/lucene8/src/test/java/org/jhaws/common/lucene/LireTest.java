package org.jhaws.common.lucene;

import javax.imageio.ImageIO;

import org.jhaws.common.encoding.Base64;
import org.jhaws.common.io.FilePath;
import org.junit.jupiter.api.Test;

import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.utils.ImageUtils;

public class LireTest {
	@Test
	public void test() {
		try {
			java.awt.image.BufferedImage img = ImageIO.read(new FilePath(LireTest.class, "0.jpg").newInputStream());
			if (Math.max(img.getHeight(), img.getWidth()) > 1024) {
				img = ImageUtils.scaleImage(img, 1024);
			}
			CEDD cedd = new CEDD();
			cedd.extract(img);
			System.out.println(Base64.base64EncodeToString(cedd.getByteArrayRepresentation()));
			System.out.println(Base64.base64EncodeToString(cedd.getByteHistogram()));
			// int[] hash1 = net.semanticmetadata.lire.indexers.hashing.BitSampling.generateHashes(cedd.getFeatureVector());
			net.semanticmetadata.lire.indexers.hashing.LocalitySensitiveHashing.generateHashFunctions();
			// int[] hash2 = net.semanticmetadata.lire.indexers.hashing.LocalitySensitiveHashing.generateHashes(cedd.getByteHistogram());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
