
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for pixelFormatComponentType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class. <pre>
 * &lt;complexType name="pixelFormatComponentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="bit_depth" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pixelFormatComponentType")
public class PixelFormatComponentType {

    @XmlAttribute(name = "index", required = true)
    protected int index;

    @XmlAttribute(name = "bit_depth", required = true)
    protected int bitDepth;

    /**
     * Gets the value of the index property.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the value of the bitDepth property.
     */
    public int getBitDepth() {
        return bitDepth;
    }

    /**
     * Sets the value of the bitDepth property.
     */
    public void setBitDepth(int value) {
        this.bitDepth = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public PixelFormatComponentType withIndex(int value) {
        setIndex(value);
        return this;
    }

    public PixelFormatComponentType withBitDepth(int value) {
        setBitDepth(value);
        return this;
    }

}
