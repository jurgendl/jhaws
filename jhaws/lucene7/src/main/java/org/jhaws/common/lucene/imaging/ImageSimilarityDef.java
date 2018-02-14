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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.similarityType == null) ? 0 : this.similarityType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ImageSimilarityDef other = (ImageSimilarityDef) obj;
        if (this.similarityType == null) {
            if (other.similarityType != null) return false;
        } else if (!this.similarityType.equals(other.similarityType)) return false;
        return true;
    }
}
