
package org.jhaws.common.io.media.ffmpeg.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.cxf.xjc.runtime.JAXBToStringStyle;


/**
 * <p>Java class for frameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="frameType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tag" type="{http://www.ffmpeg.org/schema/ffprobe}tagType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="side_data_list" type="{http://www.ffmpeg.org/schema/ffprobe}frameSideDataListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="media_type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="stream_index" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="key_frame" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="pts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pts_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="pkt_pts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pkt_pts_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="pkt_dts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pkt_dts_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="best_effort_timestamp" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="best_effort_timestamp_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="pkt_duration" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pkt_duration_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="pkt_pos" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pkt_size" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sample_fmt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nb_samples" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="channels" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="channel_layout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="pix_fmt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sample_aspect_ratio" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pict_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="coded_picture_number" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="display_picture_number" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="interlaced_frame" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="top_field_first" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="repeat_pict" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "frameType", propOrder = {
    "tag",
    "sideDataList"
})
public class FrameType {

    protected List<TagType> tag;
    @XmlElement(name = "side_data_list")
    protected FrameSideDataListType sideDataList;
    @XmlAttribute(name = "media_type", required = true)
    protected String mediaType;
    @XmlAttribute(name = "stream_index")
    protected Integer streamIndex;
    @XmlAttribute(name = "key_frame", required = true)
    protected int keyFrame;
    @XmlAttribute(name = "pts")
    protected Long pts;
    @XmlAttribute(name = "pts_time")
    protected Float ptsTime;
    @XmlAttribute(name = "pkt_pts")
    protected Long pktPts;
    @XmlAttribute(name = "pkt_pts_time")
    protected Float pktPtsTime;
    @XmlAttribute(name = "pkt_dts")
    protected Long pktDts;
    @XmlAttribute(name = "pkt_dts_time")
    protected Float pktDtsTime;
    @XmlAttribute(name = "best_effort_timestamp")
    protected Long bestEffortTimestamp;
    @XmlAttribute(name = "best_effort_timestamp_time")
    protected Float bestEffortTimestampTime;
    @XmlAttribute(name = "pkt_duration")
    protected Long pktDuration;
    @XmlAttribute(name = "pkt_duration_time")
    protected Float pktDurationTime;
    @XmlAttribute(name = "pkt_pos")
    protected Long pktPos;
    @XmlAttribute(name = "pkt_size")
    protected Integer pktSize;
    @XmlAttribute(name = "sample_fmt")
    protected String sampleFmt;
    @XmlAttribute(name = "nb_samples")
    protected Long nbSamples;
    @XmlAttribute(name = "channels")
    protected Integer channels;
    @XmlAttribute(name = "channel_layout")
    protected String channelLayout;
    @XmlAttribute(name = "width")
    protected Long width;
    @XmlAttribute(name = "height")
    protected Long height;
    @XmlAttribute(name = "pix_fmt")
    protected String pixFmt;
    @XmlAttribute(name = "sample_aspect_ratio")
    protected String sampleAspectRatio;
    @XmlAttribute(name = "pict_type")
    protected String pictType;
    @XmlAttribute(name = "coded_picture_number")
    protected Long codedPictureNumber;
    @XmlAttribute(name = "display_picture_number")
    protected Long displayPictureNumber;
    @XmlAttribute(name = "interlaced_frame")
    protected Integer interlacedFrame;
    @XmlAttribute(name = "top_field_first")
    protected Integer topFieldFirst;
    @XmlAttribute(name = "repeat_pict")
    protected Integer repeatPict;

    /**
     * Gets the value of the tag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTag().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TagType }
     * 
     * 
     */
    public List<TagType> getTag() {
        if (tag == null) {
            tag = new ArrayList<TagType>();
        }
        return this.tag;
    }

    /**
     * Gets the value of the sideDataList property.
     * 
     * @return
     *     possible object is
     *     {@link FrameSideDataListType }
     *     
     */
    public FrameSideDataListType getSideDataList() {
        return sideDataList;
    }

    /**
     * Sets the value of the sideDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FrameSideDataListType }
     *     
     */
    public void setSideDataList(FrameSideDataListType value) {
        this.sideDataList = value;
    }

    /**
     * Gets the value of the mediaType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the value of the mediaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaType(String value) {
        this.mediaType = value;
    }

    /**
     * Gets the value of the streamIndex property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStreamIndex() {
        return streamIndex;
    }

    /**
     * Sets the value of the streamIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStreamIndex(Integer value) {
        this.streamIndex = value;
    }

    /**
     * Gets the value of the keyFrame property.
     * 
     */
    public int getKeyFrame() {
        return keyFrame;
    }

    /**
     * Sets the value of the keyFrame property.
     * 
     */
    public void setKeyFrame(int value) {
        this.keyFrame = value;
    }

    /**
     * Gets the value of the pts property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPts() {
        return pts;
    }

    /**
     * Sets the value of the pts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPts(Long value) {
        this.pts = value;
    }

    /**
     * Gets the value of the ptsTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPtsTime() {
        return ptsTime;
    }

    /**
     * Sets the value of the ptsTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPtsTime(Float value) {
        this.ptsTime = value;
    }

    /**
     * Gets the value of the pktPts property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPktPts() {
        return pktPts;
    }

    /**
     * Sets the value of the pktPts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPktPts(Long value) {
        this.pktPts = value;
    }

    /**
     * Gets the value of the pktPtsTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPktPtsTime() {
        return pktPtsTime;
    }

    /**
     * Sets the value of the pktPtsTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPktPtsTime(Float value) {
        this.pktPtsTime = value;
    }

    /**
     * Gets the value of the pktDts property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPktDts() {
        return pktDts;
    }

    /**
     * Sets the value of the pktDts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPktDts(Long value) {
        this.pktDts = value;
    }

    /**
     * Gets the value of the pktDtsTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPktDtsTime() {
        return pktDtsTime;
    }

    /**
     * Sets the value of the pktDtsTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPktDtsTime(Float value) {
        this.pktDtsTime = value;
    }

    /**
     * Gets the value of the bestEffortTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBestEffortTimestamp() {
        return bestEffortTimestamp;
    }

    /**
     * Sets the value of the bestEffortTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBestEffortTimestamp(Long value) {
        this.bestEffortTimestamp = value;
    }

    /**
     * Gets the value of the bestEffortTimestampTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getBestEffortTimestampTime() {
        return bestEffortTimestampTime;
    }

    /**
     * Sets the value of the bestEffortTimestampTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setBestEffortTimestampTime(Float value) {
        this.bestEffortTimestampTime = value;
    }

    /**
     * Gets the value of the pktDuration property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPktDuration() {
        return pktDuration;
    }

    /**
     * Sets the value of the pktDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPktDuration(Long value) {
        this.pktDuration = value;
    }

    /**
     * Gets the value of the pktDurationTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPktDurationTime() {
        return pktDurationTime;
    }

    /**
     * Sets the value of the pktDurationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPktDurationTime(Float value) {
        this.pktDurationTime = value;
    }

    /**
     * Gets the value of the pktPos property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPktPos() {
        return pktPos;
    }

    /**
     * Sets the value of the pktPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPktPos(Long value) {
        this.pktPos = value;
    }

    /**
     * Gets the value of the pktSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPktSize() {
        return pktSize;
    }

    /**
     * Sets the value of the pktSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPktSize(Integer value) {
        this.pktSize = value;
    }

    /**
     * Gets the value of the sampleFmt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSampleFmt() {
        return sampleFmt;
    }

    /**
     * Sets the value of the sampleFmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSampleFmt(String value) {
        this.sampleFmt = value;
    }

    /**
     * Gets the value of the nbSamples property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNbSamples() {
        return nbSamples;
    }

    /**
     * Sets the value of the nbSamples property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNbSamples(Long value) {
        this.nbSamples = value;
    }

    /**
     * Gets the value of the channels property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getChannels() {
        return channels;
    }

    /**
     * Sets the value of the channels property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setChannels(Integer value) {
        this.channels = value;
    }

    /**
     * Gets the value of the channelLayout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelLayout() {
        return channelLayout;
    }

    /**
     * Sets the value of the channelLayout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelLayout(String value) {
        this.channelLayout = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setWidth(Long value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHeight(Long value) {
        this.height = value;
    }

    /**
     * Gets the value of the pixFmt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPixFmt() {
        return pixFmt;
    }

    /**
     * Sets the value of the pixFmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPixFmt(String value) {
        this.pixFmt = value;
    }

    /**
     * Gets the value of the sampleAspectRatio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSampleAspectRatio() {
        return sampleAspectRatio;
    }

    /**
     * Sets the value of the sampleAspectRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSampleAspectRatio(String value) {
        this.sampleAspectRatio = value;
    }

    /**
     * Gets the value of the pictType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPictType() {
        return pictType;
    }

    /**
     * Sets the value of the pictType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPictType(String value) {
        this.pictType = value;
    }

    /**
     * Gets the value of the codedPictureNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodedPictureNumber() {
        return codedPictureNumber;
    }

    /**
     * Sets the value of the codedPictureNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodedPictureNumber(Long value) {
        this.codedPictureNumber = value;
    }

    /**
     * Gets the value of the displayPictureNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDisplayPictureNumber() {
        return displayPictureNumber;
    }

    /**
     * Sets the value of the displayPictureNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDisplayPictureNumber(Long value) {
        this.displayPictureNumber = value;
    }

    /**
     * Gets the value of the interlacedFrame property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInterlacedFrame() {
        return interlacedFrame;
    }

    /**
     * Sets the value of the interlacedFrame property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInterlacedFrame(Integer value) {
        this.interlacedFrame = value;
    }

    /**
     * Gets the value of the topFieldFirst property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTopFieldFirst() {
        return topFieldFirst;
    }

    /**
     * Sets the value of the topFieldFirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTopFieldFirst(Integer value) {
        this.topFieldFirst = value;
    }

    /**
     * Gets the value of the repeatPict property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRepeatPict() {
        return repeatPict;
    }

    /**
     * Sets the value of the repeatPict property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRepeatPict(Integer value) {
        this.repeatPict = value;
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

    public FrameType withTag(TagType... values) {
        if (values!= null) {
            for (TagType value: values) {
                getTag().add(value);
            }
        }
        return this;
    }

    public FrameType withTag(Collection<TagType> values) {
        if (values!= null) {
            getTag().addAll(values);
        }
        return this;
    }

    public FrameType withSideDataList(FrameSideDataListType value) {
        setSideDataList(value);
        return this;
    }

    public FrameType withMediaType(String value) {
        setMediaType(value);
        return this;
    }

    public FrameType withStreamIndex(Integer value) {
        setStreamIndex(value);
        return this;
    }

    public FrameType withKeyFrame(int value) {
        setKeyFrame(value);
        return this;
    }

    public FrameType withPts(Long value) {
        setPts(value);
        return this;
    }

    public FrameType withPtsTime(Float value) {
        setPtsTime(value);
        return this;
    }

    public FrameType withPktPts(Long value) {
        setPktPts(value);
        return this;
    }

    public FrameType withPktPtsTime(Float value) {
        setPktPtsTime(value);
        return this;
    }

    public FrameType withPktDts(Long value) {
        setPktDts(value);
        return this;
    }

    public FrameType withPktDtsTime(Float value) {
        setPktDtsTime(value);
        return this;
    }

    public FrameType withBestEffortTimestamp(Long value) {
        setBestEffortTimestamp(value);
        return this;
    }

    public FrameType withBestEffortTimestampTime(Float value) {
        setBestEffortTimestampTime(value);
        return this;
    }

    public FrameType withPktDuration(Long value) {
        setPktDuration(value);
        return this;
    }

    public FrameType withPktDurationTime(Float value) {
        setPktDurationTime(value);
        return this;
    }

    public FrameType withPktPos(Long value) {
        setPktPos(value);
        return this;
    }

    public FrameType withPktSize(Integer value) {
        setPktSize(value);
        return this;
    }

    public FrameType withSampleFmt(String value) {
        setSampleFmt(value);
        return this;
    }

    public FrameType withNbSamples(Long value) {
        setNbSamples(value);
        return this;
    }

    public FrameType withChannels(Integer value) {
        setChannels(value);
        return this;
    }

    public FrameType withChannelLayout(String value) {
        setChannelLayout(value);
        return this;
    }

    public FrameType withWidth(Long value) {
        setWidth(value);
        return this;
    }

    public FrameType withHeight(Long value) {
        setHeight(value);
        return this;
    }

    public FrameType withPixFmt(String value) {
        setPixFmt(value);
        return this;
    }

    public FrameType withSampleAspectRatio(String value) {
        setSampleAspectRatio(value);
        return this;
    }

    public FrameType withPictType(String value) {
        setPictType(value);
        return this;
    }

    public FrameType withCodedPictureNumber(Long value) {
        setCodedPictureNumber(value);
        return this;
    }

    public FrameType withDisplayPictureNumber(Long value) {
        setDisplayPictureNumber(value);
        return this;
    }

    public FrameType withInterlacedFrame(Integer value) {
        setInterlacedFrame(value);
        return this;
    }

    public FrameType withTopFieldFirst(Integer value) {
        setTopFieldFirst(value);
        return this;
    }

    public FrameType withRepeatPict(Integer value) {
        setRepeatPict(value);
        return this;
    }

    public void setTag(List<TagType> value) {
        this.tag = value;
    }

}
