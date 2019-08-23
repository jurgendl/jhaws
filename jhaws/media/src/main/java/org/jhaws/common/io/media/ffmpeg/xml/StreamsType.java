
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
 * Java class for streamsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="streamsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="stream" type="{http://www.ffmpeg.org/schema/ffprobe}streamType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamsType", propOrder = { "stream" })
public class StreamsType {

    protected List<StreamType> stream;

    /**
     * Gets the value of the stream property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the stream property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getStream().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link StreamType }
     *
     *
     */
    public List<StreamType> getStream() {
        if (stream == null) {
            stream = new ArrayList<>();
        }
        return this.stream;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public StreamsType withStream(StreamType... values) {
        if (values != null) {
            for (StreamType value : values) {
                getStream().add(value);
            }
        }
        return this;
    }

    public StreamsType withStream(Collection<StreamType> values) {
        if (values != null) {
            getStream().addAll(values);
        }
        return this;
    }

    public void setStream(List<StreamType> value) {
        this.stream = value;
    }

}
