package org.jhaws.common.lucene.imaging;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

public class ImageSimilarity implements Serializable, Comparable<ImageSimilarity> {
    private static final long serialVersionUID = 3511643000528919430L;

    public String a;

    public String b;

    public double similarity;

    public ImageSimilarity() {
        super();
    }

    public ImageSimilarity(String a, String b, double similarity) {
        this();
        if (a.compareTo(b) > 0) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
        this.similarity = similarity;
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
        return new CompareToBuilder().append(similarity, o.similarity).toComparison();
    }

    @Override
    public String toString() {
        return a + "\t" + similarity + "\t" + b + "\r\n";
    }
}