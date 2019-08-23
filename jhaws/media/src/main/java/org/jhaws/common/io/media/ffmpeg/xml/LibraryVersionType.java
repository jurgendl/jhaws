
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for libraryVersionType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="libraryVersionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="major" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="minor" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="micro" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="ident" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libraryVersionType")
public class LibraryVersionType {

    @XmlAttribute(name = "name", required = true)
    protected String name;

    @XmlAttribute(name = "major", required = true)
    protected int major;

    @XmlAttribute(name = "minor", required = true)
    protected int minor;

    @XmlAttribute(name = "micro", required = true)
    protected int micro;

    @XmlAttribute(name = "version", required = true)
    protected int version;

    @XmlAttribute(name = "ident", required = true)
    protected String ident;

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
     * Gets the value of the major property.
     *
     */
    public int getMajor() {
        return major;
    }

    /**
     * Sets the value of the major property.
     *
     */
    public void setMajor(int value) {
        this.major = value;
    }

    /**
     * Gets the value of the minor property.
     *
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Sets the value of the minor property.
     *
     */
    public void setMinor(int value) {
        this.minor = value;
    }

    /**
     * Gets the value of the micro property.
     *
     */
    public int getMicro() {
        return micro;
    }

    /**
     * Sets the value of the micro property.
     *
     */
    public void setMicro(int value) {
        this.micro = value;
    }

    /**
     * Gets the value of the version property.
     *
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     */
    public void setVersion(int value) {
        this.version = value;
    }

    /**
     * Gets the value of the ident property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIdent() {
        return ident;
    }

    /**
     * Sets the value of the ident property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIdent(String value) {
        this.ident = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public LibraryVersionType withName(String value) {
        setName(value);
        return this;
    }

    public LibraryVersionType withMajor(int value) {
        setMajor(value);
        return this;
    }

    public LibraryVersionType withMinor(int value) {
        setMinor(value);
        return this;
    }

    public LibraryVersionType withMicro(int value) {
        setMicro(value);
        return this;
    }

    public LibraryVersionType withVersion(int value) {
        setVersion(value);
        return this;
    }

    public LibraryVersionType withIdent(String value) {
        setIdent(value);
        return this;
    }

}
