
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for subtitleType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class. <pre>
 * &lt;complexType name="subtitleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="media_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="subtitle" />
 *       &lt;attribute name="pts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pts_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="format" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="start_display_time" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="end_display_time" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="num_rects" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subtitleType")
public class SubtitleType {

    @XmlAttribute(name = "media_type", required = true)
    protected String mediaType;

    @XmlAttribute(name = "pts")
    protected Long pts;

    @XmlAttribute(name = "pts_time")
    protected Float ptsTime;

    @XmlAttribute(name = "format")
    protected Integer format;

    @XmlAttribute(name = "start_display_time")
    protected Integer startDisplayTime;

    @XmlAttribute(name = "end_display_time")
    protected Integer endDisplayTime;

    @XmlAttribute(name = "num_rects")
    protected Integer numRects;

    /**
     * Gets the value of the mediaType property.
     *
     * @return possible object is {@link String }
     */
    public String getMediaType() {
        if (mediaType == null) {
            return "subtitle";
        } else {
            return mediaType;
        }
    }

    /**
     * Sets the value of the mediaType property.
     *
     * @param value allowed object is {@link String }
     */
    public void setMediaType(String value) {
        this.mediaType = value;
    }

    /**
     * Gets the value of the pts property.
     *
     * @return possible object is {@link Long }
     */
    public Long getPts() {
        return pts;
    }

    /**
     * Sets the value of the pts property.
     *
     * @param value allowed object is {@link Long }
     */
    public void setPts(Long value) {
        this.pts = value;
    }

    /**
     * Gets the value of the ptsTime property.
     *
     * @return possible object is {@link Float }
     */
    public Float getPtsTime() {
        return ptsTime;
    }

    /**
     * Sets the value of the ptsTime property.
     *
     * @param value allowed object is {@link Float }
     */
    public void setPtsTime(Float value) {
        this.ptsTime = value;
    }

    /**
     * Gets the value of the format property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setFormat(Integer value) {
        this.format = value;
    }

    /**
     * Gets the value of the startDisplayTime property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getStartDisplayTime() {
        return startDisplayTime;
    }

    /**
     * Sets the value of the startDisplayTime property.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setStartDisplayTime(Integer value) {
        this.startDisplayTime = value;
    }

    /**
     * Gets the value of the endDisplayTime property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getEndDisplayTime() {
        return endDisplayTime;
    }

    /**
     * Sets the value of the endDisplayTime property.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setEndDisplayTime(Integer value) {
        this.endDisplayTime = value;
    }

    /**
     * Gets the value of the numRects property.
     *
     * @return possible object is {@link Integer }
     */
    public Integer getNumRects() {
        return numRects;
    }

    /**
     * Sets the value of the numRects property.
     *
     * @param value allowed object is {@link Integer }
     */
    public void setNumRects(Integer value) {
        this.numRects = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public SubtitleType withMediaType(String value) {
        setMediaType(value);
        return this;
    }

    public SubtitleType withPts(Long value) {
        setPts(value);
        return this;
    }

    public SubtitleType withPtsTime(Float value) {
        setPtsTime(value);
        return this;
    }

    public SubtitleType withFormat(Integer value) {
        setFormat(value);
        return this;
    }

    public SubtitleType withStartDisplayTime(Integer value) {
        setStartDisplayTime(value);
        return this;
    }

    public SubtitleType withEndDisplayTime(Integer value) {
        setEndDisplayTime(value);
        return this;
    }

    public SubtitleType withNumRects(Integer value) {
        setNumRects(value);
        return this;
    }

}
