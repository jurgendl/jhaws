package org.jhaws.common.lucene.imaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "image-similarities")
public class ImageSimilarities implements Iterable<ImageSimilarity> {
    @XmlElement(name = "similarity")
    @XmlElementWrapper(name = "similarities")
    private List<ImageSimilarity> imageSimilarities = new ArrayList<>();

    public List<ImageSimilarity> getImageSimilarities() {
        return this.imageSimilarities;
    }

    public void setImageSimilarities(List<ImageSimilarity> imageSimilarities) {
        this.imageSimilarities = imageSimilarities;
    }

    @Override
    public Iterator<ImageSimilarity> iterator() {
        return imageSimilarities.iterator();
    }
}
