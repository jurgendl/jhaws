package org.jhaws.common.lucene.imaging;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "image-similarity-def")
public class ImageSimilarityDef {
    @XmlAttribute
    public double similarity;

    @XmlAttribute
    public String similarityType;

    public ImageSimilarityDef() {
        super();
    }

    public ImageSimilarityDef(String similarityType, double similarity) {
        super();
        this.similarityType = similarityType;
        this.similarity = similarity;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public String getSimilarityType() {
        return this.similarityType;
    }

    public void setSimilarityType(String similarityType) {
        this.similarityType = similarityType;
    }

    @Override
    public String toString() {
        return similarityType + "=" + similarity;
    }
}
