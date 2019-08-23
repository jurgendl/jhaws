
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for pixelFormatType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="pixelFormatType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flags" type="{http://www.ffmpeg.org/schema/ffprobe}pixelFormatFlagsType" minOccurs="0"/>
 *         &lt;element name="components" type="{http://www.ffmpeg.org/schema/ffprobe}pixelFormatComponentsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nb_components" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="log2_chroma_w" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="log2_chroma_h" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="bits_per_pixel" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pixelFormatType", propOrder = { "flags", "components" })
public class PixelFormatType {

    protected PixelFormatFlagsType flags;

    protected PixelFormatComponentsType components;

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "nb_components", required = true)
    protected int nbComponents;

    @XmlAttribute(name = "log2_chroma_w")
    protected Integer log2ChromaW;

    @XmlAttribute(name = "log2_chroma_h")
    protected Integer log2ChromaH;

    @XmlAttribute(name = "bits_per_pixel")
    protected Integer bitsPerPixel;

    /**
     * Gets the value of the flags property.
     *
     * @return possible object is {@link PixelFormatFlagsType }
     *
     */
    public PixelFormatFlagsType getFlags() {
        return flags;
    }

    /**
     * Sets the value of the flags property.
     *
     * @param value allowed object is {@link PixelFormatFlagsType }
     *
     */
    public void setFlags(PixelFormatFlagsType value) {
        this.flags = value;
    }

    /**
     * Gets the value of the components property.
     *
     * @return possible object is {@link PixelFormatComponentsType }
     *
     */
    public PixelFormatComponentsType getComponents() {
        return components;
    }

    /**
     * Sets the value of the components property.
     *
     * @param value allowed object is {@link PixelFormatComponentsType }
     *
     */
    public void setComponents(PixelFormatComponentsType value) {
        this.components = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nbComponents property.
     *
     */
    public int getNbComponents() {
        return nbComponents;
    }

    /**
     * Sets the value of the nbComponents property.
     *
     */
    public void setNbComponents(int value) {
        this.nbComponents = value;
    }

    /**
     * Gets the value of the log2ChromaW property.
     *
     * @return possible object is {@link Integer }
     *
     */
    public Integer getLog2ChromaW() {
        return log2ChromaW;
    }

    /**
     * Sets the value of the log2ChromaW property.
     *
     * @param value allowed object is {@link Integer }
     *
     */
    public void setLog2ChromaW(Integer value) {
        this.log2ChromaW = value;
    }

    /**
     * Gets the value of the log2ChromaH property.
     *
     * @return possible object is {@link Integer }
     *
     */
    public Integer getLog2ChromaH() {
        return log2ChromaH;
    }

    /**
     * Sets the value of the log2ChromaH property.
     *
     * @param value allowed object is {@link Integer }
     *
     */
    public void setLog2ChromaH(Integer value) {
        this.log2ChromaH = value;
    }

    /**
     * Gets the value of the bitsPerPixel property.
     *
     * @return possible object is {@link Integer }
     *
     */
    public Integer getBitsPerPixel() {
        return bitsPerPixel;
    }

    /**
     * Sets the value of the bitsPerPixel property.
     *
     * @param value allowed object is {@link Integer }
     *
     */
    public void setBitsPerPixel(Integer value) {
        this.bitsPerPixel = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public PixelFormatType withFlags(PixelFormatFlagsType value) {
        setFlags(value);
        return this;
    }

    public PixelFormatType withComponents(PixelFormatComponentsType value) {
        setComponents(value);
        return this;
    }

    public PixelFormatType withName(String value) {
        setName(value);
        return this;
    }

    public PixelFormatType withNbComponents(int value) {
        setNbComponents(value);
        return this;
    }

    public PixelFormatType withLog2ChromaW(Integer value) {
        setLog2ChromaW(value);
        return this;
    }

    public PixelFormatType withLog2ChromaH(Integer value) {
        setLog2ChromaH(value);
        return this;
    }

    public PixelFormatType withBitsPerPixel(Integer value) {
        setBitsPerPixel(value);
        return this;
    }

}
