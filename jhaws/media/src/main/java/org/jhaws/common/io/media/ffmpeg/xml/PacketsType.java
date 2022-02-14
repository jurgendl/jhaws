
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
 * Java class for packetsType complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class. <pre>
 * &lt;complexType name="packetsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="packet" type="{http://www.ffmpeg.org/schema/ffprobe}packetType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "packetsType", propOrder = { "packet" })
public class PacketsType {

    protected List<PacketType> packet;

    /**
     * Gets the value of the packet property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the packet property.
     * <p>
     * For example, to add a new item, do as follows: <pre>
     * getPacket().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link PacketType }
     */
    public List<PacketType> getPacket() {
        if (packet == null) {
            packet = new ArrayList<>();
        }
        return this.packet;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public PacketsType withPacket(PacketType... values) {
        if (values != null) {
            for (PacketType value : values) {
                getPacket().add(value);
            }
        }
        return this;
    }

    public PacketsType withPacket(Collection<PacketType> values) {
        if (values != null) {
            getPacket().addAll(values);
        }
        return this;
    }

    public void setPacket(List<PacketType> value) {
        this.packet = value;
    }

}
