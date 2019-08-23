
package org.jhaws.common.io.media.ffmpeg.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for pixelFormatsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="pixelFormatsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pixel_format" type="{http://www.ffmpeg.org/schema/ffprobe}pixelFormatType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pixelFormatsType", propOrder = { "pixelFormat" })
public class PixelFormatsType {

    @XmlElement(name = "pixel_format")
    protected List<PixelFormatType> pixelFormat;

    /**
     * Gets the value of the pixelFormat property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the pixelFormat property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getPixelFormat().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link PixelFormatType }
     *
     *
     */
    public List<PixelFormatType> getPixelFormat() {
        if (pixelFormat == null) {
            pixelFormat = new ArrayList<>();
        }
        return this.pixelFormat;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public PixelFormatsType withPixelFormat(PixelFormatType... values) {
        if (values != null) {
            for (PixelFormatType value : values) {
                getPixelFormat().add(value);
            }
        }
        return this;
    }

    public PixelFormatsType withPixelFormat(Collection<PixelFormatType> values) {
        if (values != null) {
            getPixelFormat().addAll(values);
        }
        return this;
    }

    public void setPixelFormat(List<PixelFormatType> value) {
        this.pixelFormat = value;
    }

}
