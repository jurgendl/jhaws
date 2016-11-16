
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for streamDispositionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamDispositionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="default" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="dub" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="original" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="comment" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="lyrics" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="karaoke" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="forced" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="hearing_impaired" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="visual_impaired" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="clean_effects" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="attached_pic" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="timed_thumbnails" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamDispositionType")
public class StreamDispositionType {

    @XmlAttribute(name = "default", required = true)
    protected int _default;
    @XmlAttribute(name = "dub", required = true)
    protected int dub;
    @XmlAttribute(name = "original", required = true)
    protected int original;
    @XmlAttribute(name = "comment", required = true)
    protected int comment;
    @XmlAttribute(name = "lyrics", required = true)
    protected int lyrics;
    @XmlAttribute(name = "karaoke", required = true)
    protected int karaoke;
    @XmlAttribute(name = "forced", required = true)
    protected int forced;
    @XmlAttribute(name = "hearing_impaired", required = true)
    protected int hearingImpaired;
    @XmlAttribute(name = "visual_impaired", required = true)
    protected int visualImpaired;
    @XmlAttribute(name = "clean_effects", required = true)
    protected int cleanEffects;
    @XmlAttribute(name = "attached_pic", required = true)
    protected int attachedPic;
    @XmlAttribute(name = "timed_thumbnails", required = true)
    protected int timedThumbnails;

    /**
     * Gets the value of the default property.
     * 
     */
    public int getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     */
    public void setDefault(int value) {
        this._default = value;
    }

    /**
     * Gets the value of the dub property.
     * 
     */
    public int getDub() {
        return dub;
    }

    /**
     * Sets the value of the dub property.
     * 
     */
    public void setDub(int value) {
        this.dub = value;
    }

    /**
     * Gets the value of the original property.
     * 
     */
    public int getOriginal() {
        return original;
    }

    /**
     * Sets the value of the original property.
     * 
     */
    public void setOriginal(int value) {
        this.original = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     */
    public int getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     */
    public void setComment(int value) {
        this.comment = value;
    }

    /**
     * Gets the value of the lyrics property.
     * 
     */
    public int getLyrics() {
        return lyrics;
    }

    /**
     * Sets the value of the lyrics property.
     * 
     */
    public void setLyrics(int value) {
        this.lyrics = value;
    }

    /**
     * Gets the value of the karaoke property.
     * 
     */
    public int getKaraoke() {
        return karaoke;
    }

    /**
     * Sets the value of the karaoke property.
     * 
     */
    public void setKaraoke(int value) {
        this.karaoke = value;
    }

    /**
     * Gets the value of the forced property.
     * 
     */
    public int getForced() {
        return forced;
    }

    /**
     * Sets the value of the forced property.
     * 
     */
    public void setForced(int value) {
        this.forced = value;
    }

    /**
     * Gets the value of the hearingImpaired property.
     * 
     */
    public int getHearingImpaired() {
        return hearingImpaired;
    }

    /**
     * Sets the value of the hearingImpaired property.
     * 
     */
    public void setHearingImpaired(int value) {
        this.hearingImpaired = value;
    }

    /**
     * Gets the value of the visualImpaired property.
     * 
     */
    public int getVisualImpaired() {
        return visualImpaired;
    }

    /**
     * Sets the value of the visualImpaired property.
     * 
     */
    public void setVisualImpaired(int value) {
        this.visualImpaired = value;
    }

    /**
     * Gets the value of the cleanEffects property.
     * 
     */
    public int getCleanEffects() {
        return cleanEffects;
    }

    /**
     * Sets the value of the cleanEffects property.
     * 
     */
    public void setCleanEffects(int value) {
        this.cleanEffects = value;
    }

    /**
     * Gets the value of the attachedPic property.
     * 
     */
    public int getAttachedPic() {
        return attachedPic;
    }

    /**
     * Sets the value of the attachedPic property.
     * 
     */
    public void setAttachedPic(int value) {
        this.attachedPic = value;
    }

    /**
     * Gets the value of the timedThumbnails property.
     * 
     */
    public int getTimedThumbnails() {
        return timedThumbnails;
    }

    /**
     * Sets the value of the timedThumbnails property.
     * 
     */
    public void setTimedThumbnails(int value) {
        this.timedThumbnails = value;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public StreamDispositionType withDefault(int value) {
        setDefault(value);
        return this;
    }

    public StreamDispositionType withDub(int value) {
        setDub(value);
        return this;
    }

    public StreamDispositionType withOriginal(int value) {
        setOriginal(value);
        return this;
    }

    public StreamDispositionType withComment(int value) {
        setComment(value);
        return this;
    }

    public StreamDispositionType withLyrics(int value) {
        setLyrics(value);
        return this;
    }

    public StreamDispositionType withKaraoke(int value) {
        setKaraoke(value);
        return this;
    }

    public StreamDispositionType withForced(int value) {
        setForced(value);
        return this;
    }

    public StreamDispositionType withHearingImpaired(int value) {
        setHearingImpaired(value);
        return this;
    }

    public StreamDispositionType withVisualImpaired(int value) {
        setVisualImpaired(value);
        return this;
    }

    public StreamDispositionType withCleanEffects(int value) {
        setCleanEffects(value);
        return this;
    }

    public StreamDispositionType withAttachedPic(int value) {
        setAttachedPic(value);
        return this;
    }

    public StreamDispositionType withTimedThumbnails(int value) {
        setTimedThumbnails(value);
        return this;
    }

}
