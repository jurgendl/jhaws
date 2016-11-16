
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for programVersionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="programVersionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="copyright" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="build_date" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="build_time" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="compiler_ident" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="configuration" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "programVersionType")
public class ProgramVersionType {

    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "copyright", required = true)
    protected String copyright;
    @XmlAttribute(name = "build_date")
    protected String buildDate;
    @XmlAttribute(name = "build_time")
    protected String buildTime;
    @XmlAttribute(name = "compiler_ident", required = true)
    protected String compilerIdent;
    @XmlAttribute(name = "configuration", required = true)
    protected String configuration;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the copyright property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the buildDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildDate() {
        return buildDate;
    }

    /**
     * Sets the value of the buildDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildDate(String value) {
        this.buildDate = value;
    }

    /**
     * Gets the value of the buildTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildTime() {
        return buildTime;
    }

    /**
     * Sets the value of the buildTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildTime(String value) {
        this.buildTime = value;
    }

    /**
     * Gets the value of the compilerIdent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompilerIdent() {
        return compilerIdent;
    }

    /**
     * Sets the value of the compilerIdent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompilerIdent(String value) {
        this.compilerIdent = value;
    }

    /**
     * Gets the value of the configuration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Sets the value of the configuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfiguration(String value) {
        this.configuration = value;
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

    public ProgramVersionType withVersion(String value) {
        setVersion(value);
        return this;
    }

    public ProgramVersionType withCopyright(String value) {
        setCopyright(value);
        return this;
    }

    public ProgramVersionType withBuildDate(String value) {
        setBuildDate(value);
        return this;
    }

    public ProgramVersionType withBuildTime(String value) {
        setBuildTime(value);
        return this;
    }

    public ProgramVersionType withCompilerIdent(String value) {
        setCompilerIdent(value);
        return this;
    }

    public ProgramVersionType withConfiguration(String value) {
        setConfiguration(value);
        return this;
    }

}
