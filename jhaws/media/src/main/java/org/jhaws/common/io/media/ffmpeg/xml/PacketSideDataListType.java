
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
 * <p>Java class for packetSideDataListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="packetSideDataListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="side_data" type="{http://www.ffmpeg.org/schema/ffprobe}packetSideDataType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "packetSideDataListType", propOrder = {
    "sideData"
})
public class PacketSideDataListType {

    @XmlElement(name = "side_data", required = true)
    protected List<PacketSideDataType> sideData;

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
     * {@link PacketSideDataType }
     * 
     * 
     */
    public List<PacketSideDataType> getSideData() {
        if (sideData == null) {
            sideData = new ArrayList<PacketSideDataType>();
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

    public PacketSideDataListType withSideData(PacketSideDataType... values) {
        if (values!= null) {
            for (PacketSideDataType value: values) {
                getSideData().add(value);
            }
        }
        return this;
    }

    public PacketSideDataListType withSideData(Collection<PacketSideDataType> values) {
        if (values!= null) {
            getSideData().addAll(values);
        }
        return this;
    }

    public void setSideData(List<PacketSideDataType> value) {
        this.sideData = value;
    }

}
