
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
 * Java class for pixelFormatComponentsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pixelFormatComponentsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="component" type="{http://www.ffmpeg.org/schema/ffprobe}pixelFormatComponentType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pixelFormatComponentsType", propOrder = { "component" })
public class PixelFormatComponentsType {

    protected List<PixelFormatComponentType> component;

    /**
     * Gets the value of the component property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the component property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getComponent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link PixelFormatComponentType }
     * 
     * 
     */
    public List<PixelFormatComponentType> getComponent() {
        if (component == null) {
            component = new ArrayList<PixelFormatComponentType>();
        }
        return this.component;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public PixelFormatComponentsType withComponent(PixelFormatComponentType... values) {
        if (values != null) {
            for (PixelFormatComponentType value : values) {
                getComponent().add(value);
            }
        }
        return this;
    }

    public PixelFormatComponentsType withComponent(Collection<PixelFormatComponentType> values) {
        if (values != null) {
            getComponent().addAll(values);
        }
        return this;
    }

    public void setComponent(List<PixelFormatComponentType> value) {
        this.component = value;
    }

}
