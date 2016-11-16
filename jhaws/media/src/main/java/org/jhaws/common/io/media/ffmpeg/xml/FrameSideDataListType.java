
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
 * <p>Java class for frameSideDataListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="frameSideDataListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="side_data" type="{http://www.ffmpeg.org/schema/ffprobe}frameSideDataType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "frameSideDataListType", propOrder = {
    "sideData"
})
public class FrameSideDataListType {

    @XmlElement(name = "side_data", required = true)
    protected List<FrameSideDataType> sideData;

    /**
     * Gets the value of the sideData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sideData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSideData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FrameSideDataType }
     * 
     * 
     */
    public List<FrameSideDataType> getSideData() {
        if (sideData == null) {
            sideData = new ArrayList<FrameSideDataType>();
        }
        return this.sideData;
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

    public FrameSideDataListType withSideData(FrameSideDataType... values) {
        if (values!= null) {
            for (FrameSideDataType value: values) {
                getSideData().add(value);
            }
        }
        return this;
    }

    public FrameSideDataListType withSideData(Collection<FrameSideDataType> values) {
        if (values!= null) {
            getSideData().addAll(values);
        }
        return this;
    }

    public void setSideData(List<FrameSideDataType> value) {
        this.sideData = value;
    }

}
