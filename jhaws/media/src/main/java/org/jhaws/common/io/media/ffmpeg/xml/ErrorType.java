
package org.jhaws.common.io.media.ffmpeg.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;

/**
 * <p>
 * Java class for errorType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class. <pre>
 * &lt;complexType name="errorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="string" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errorType")
public class ErrorType {

    @XmlAttribute(name = "code", required = true)
    protected int code;

    @XmlAttribute(name = "string", required = true)
    protected String string;

    /**
     * Gets the value of the code property.
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * Gets the value of the string property.
     *
     * @return possible object is {@link String }
     */
    public String getString() {
        return string;
    }

    /**
     * Sets the value of the string property.
     *
     * @param value allowed object is {@link String }
     */
    public void setString(String value) {
        this.string = value;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public ErrorType withCode(int value) {
        setCode(value);
        return this;
    }

    public ErrorType withString(String value) {
        setString(value);
        return this;
    }

}
