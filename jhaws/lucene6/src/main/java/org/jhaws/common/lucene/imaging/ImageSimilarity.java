package org.jhaws.common.lucene.imaging;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.CompareToBuilder;

@XmlRootElement(name = "image-similarity")
public class ImageSimilarity implements Serializable, Comparable<ImageSimilarity> {
    private static final long serialVersionUID = 3511643000528919430L;

    @XmlAttribute
    public String a;

    @XmlAttribute
    public String b;

    @XmlAttribute(name = "a-wh")
    public int[] aWH;

    @XmlAttribute(name = "b-wh")
    public int[] bWH;

    @XmlAttribute(name = "a-size")
    public long aSize;

    @XmlAttribute(name = "b-size")
    public long bSize;

    @XmlAttribute
    public double similarity;

    @XmlAttribute
    public String similarityType;

    public ImageSimilarity() {
        super();
    }

    public ImageSimilarity(String a, String b, double similarity, int[] aWH, int[] bWH, long aSize, long bSize, String similarityType) {
        this();
        if (a.compareTo(b) < 0) {
            this.a = a;
            this.b = b;
            this.aWH = aWH;
            this.bWH = bWH;
            this.aSize = aSize;
            this.bSize = bSize;
        } else {
            this.a = b;
            this.b = a;
            this.aWH = bWH;
            this.bWH = aWH;
            this.aSize = bSize;
            this.bSize = aSize;
        }
        this.similarity = similarity;
        this.similarityType = similarityType;
    }

    public String getA() {
        return this.a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return this.b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.a == null) ? 0 : this.a.hashCode());
        result = prime * result + ((this.b == null) ? 0 : this.b.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ImageSimilarity other = (ImageSimilarity) obj;
        if (this.a == null) {
            if (other.a != null) return false;
        } else if (!this.a.equals(other.a)) return false;
        if (this.b == null) {
            if (other.b != null) return false;
        } else if (!this.b.equals(other.b)) return false;
        return true;
    }

    @Override
    public int compareTo(ImageSimilarity o) {
        return new CompareToBuilder().append(similarity, o.similarity).append(a, o.a).append(b, o.b).toComparison();
    }

    @Override
    public String toString() {
        return a + "\t" + similarity + "\t" + b + "\t" + aWH[0] + "x" + aWH[1] + "\t" + bWH[0] + "x" + bWH[1] + "\t" + aSize + "\t" + bSize + "\r\n";
    }

    public int[] getaWH() {
        return this.aWH;
    }

    public void setaWH(int[] aWH) {
        this.aWH = aWH;
    }

    public int[] getbWH() {
        return this.bWH;
    }

    public void setbWH(int[] bWH) {
        this.bWH = bWH;
    }

    public long getaSize() {
        return this.aSize;
    }

    public void setaSize(long aSize) {
        this.aSize = aSize;
    }

    public long getbSize() {
        return this.bSize;
    }

    public void setbSize(long bSize) {
        this.bSize = bSize;
    }

    public String getSimilarityType() {
        return this.similarityType;
    }

    public void setSimilarityType(String similarityType) {
        this.similarityType = similarityType;
    }
}