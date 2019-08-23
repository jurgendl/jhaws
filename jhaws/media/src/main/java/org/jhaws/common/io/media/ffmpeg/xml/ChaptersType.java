
package org.jhaws.common.io.media.ffmpeg.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for chaptersType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="chaptersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="chapter" type="{http://www.ffmpeg.org/schema/ffprobe}chapterType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "chaptersType", propOrder = { "chapter" })
public class ChaptersType {

    protected List<ChapterType> chapter;

    /**
     * Gets the value of the chapter property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the chapter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getChapter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ChapterType }
     *
     *
     */
    public List<ChapterType> getChapter() {
        if (chapter == null) {
            chapter = new ArrayList<>();
        }
        return this.chapter;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public ChaptersType withChapter(ChapterType... values) {
        if (values != null) {
            for (ChapterType value : values) {
                getChapter().add(value);
            }
        }
        return this;
    }

    public ChaptersType withChapter(Collection<ChapterType> values) {
        if (values != null) {
            getChapter().addAll(values);
        }
        return this;
    }

    public void setChapter(List<ChapterType> value) {
        this.chapter = value;
    }

}
