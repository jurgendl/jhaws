
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for frameSideDataType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="frameSideDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="side_data_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="side_data_size" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="timecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "frameSideDataType")
public class FrameSideDataType {

    @XmlAttribute(name = "side_data_type")
    protected String sideDataType;

    @XmlAttribute(name = "side_data_size")
    protected Integer sideDataSize;

    @XmlAttribute(name = "timecode")
    protected String timecode;

    /**
     * Gets the value of the sideDataType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSideDataType() {
        return sideDataType;
    }

    /**
     * Sets the value of the sideDataType property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setSideDataType(String value) {
        this.sideDataType = value;
    }

    /**
     * Gets the value of the sideDataSize property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getSideDataSize() {
        return sideDataSize;
    }

    /**
     * Sets the value of the sideDataSize property.
     * 
     * @param value allowed object is {@link Integer }
     * 
     */
    public void setSideDataSize(Integer value) {
        this.sideDataSize = value;
    }

    /**
     * Gets the value of the timecode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTimecode() {
        return timecode;
    }

    /**
     * Sets the value of the timecode property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setTimecode(String value) {
        this.timecode = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public FrameSideDataType withSideDataType(String value) {
        setSideDataType(value);
        return this;
    }

    public FrameSideDataType withSideDataSize(Integer value) {
        setSideDataSize(value);
        return this;
    }

    public FrameSideDataType withTimecode(String value) {
        setTimecode(value);
        return this;
    }

}
