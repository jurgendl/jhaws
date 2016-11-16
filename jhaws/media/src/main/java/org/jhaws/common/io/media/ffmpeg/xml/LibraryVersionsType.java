
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
 * <p>Java class for libraryVersionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="libraryVersionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="library_version" type="{http://www.ffmpeg.org/schema/ffprobe}libraryVersionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libraryVersionsType", propOrder = {
    "libraryVersion"
})
public class LibraryVersionsType {

    @XmlElement(name = "library_version")
    protected List<LibraryVersionType> libraryVersion;

    /**
     * Gets the value of the libraryVersion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the libraryVersion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLibraryVersion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LibraryVersionType }
     * 
     * 
     */
    public List<LibraryVersionType> getLibraryVersion() {
        if (libraryVersion == null) {
            libraryVersion = new ArrayList<LibraryVersionType>();
        }
        return this.libraryVersion;
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

    public LibraryVersionsType withLibraryVersion(LibraryVersionType... values) {
        if (values!= null) {
            for (LibraryVersionType value: values) {
                getLibraryVersion().add(value);
            }
        }
        return this;
    }

    public LibraryVersionsType withLibraryVersion(Collection<LibraryVersionType> values) {
        if (values!= null) {
            getLibraryVersion().addAll(values);
        }
        return this;
    }

    public void setLibraryVersion(List<LibraryVersionType> value) {
        this.libraryVersion = value;
    }

}
