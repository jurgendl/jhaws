
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
 * <p>Java class for streamType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="disposition" type="{http://www.ffmpeg.org/schema/ffprobe}streamDispositionType" minOccurs="0"/>
 *         &lt;element name="tag" type="{http://www.ffmpeg.org/schema/ffprobe}tagType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="side_data_list" type="{http://www.ffmpeg.org/schema/ffprobe}packetSideDataListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="codec_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codec_long_name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="profile" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codec_type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codec_time_base" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codec_tag" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codec_tag_string" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="extradata" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="extradata_hash" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="coded_width" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="coded_height" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="has_b_frames" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sample_aspect_ratio" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="display_aspect_ratio" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="pix_fmt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="color_range" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="color_space" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="color_transfer" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="color_primaries" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="chroma_location" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="field_order" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="timecode" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="refs" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="sample_fmt" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sample_rate" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="channels" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="channel_layout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bits_per_sample" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="r_frame_rate" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="avg_frame_rate" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="time_base" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="start_pts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="start_time" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="duration_ts" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="bit_rate" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="max_bit_rate" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="bits_per_raw_sample" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="nb_frames" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="nb_read_frames" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="nb_read_packets" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamType", propOrder = {
    "disposition",
    "tag",
    "sideDataList"
})
public class StreamType {

    protected StreamDispositionType disposition;
    protected List<TagType> tag;
    @XmlElement(name = "side_data_list")
    protected PacketSideDataListType sideDataList;
    @XmlAttribute(name = "index", required = true)
    protected int index;
    @XmlAttribute(name = "codec_name")
    protected String codecName;
    @XmlAttribute(name = "codec_long_name")
    protected String codecLongName;
    @XmlAttribute(name = "profile")
    protected String profile;
    @XmlAttribute(name = "codec_type")
    protected String codecType;
    @XmlAttribute(name = "codec_time_base", required = true)
    protected String codecTimeBase;
    @XmlAttribute(name = "codec_tag", required = true)
    protected String codecTag;
    @XmlAttribute(name = "codec_tag_string", required = true)
    protected String codecTagString;
    @XmlAttribute(name = "extradata")
    protected String extradata;
    @XmlAttribute(name = "extradata_hash")
    protected String extradataHash;
    @XmlAttribute(name = "width")
    protected Integer width;
    @XmlAttribute(name = "height")
    protected Integer height;
    @XmlAttribute(name = "coded_width")
    protected Integer codedWidth;
    @XmlAttribute(name = "coded_height")
    protected Integer codedHeight;
    @XmlAttribute(name = "has_b_frames")
    protected Integer hasBFrames;
    @XmlAttribute(name = "sample_aspect_ratio")
    protected String sampleAspectRatio;
    @XmlAttribute(name = "display_aspect_ratio")
    protected String displayAspectRatio;
    @XmlAttribute(name = "pix_fmt")
    protected String pixFmt;
    @XmlAttribute(name = "level")
    protected Integer level;
    @XmlAttribute(name = "color_range")
    protected String colorRange;
    @XmlAttribute(name = "color_space")
    protected String colorSpace;
    @XmlAttribute(name = "color_transfer")
    protected String colorTransfer;
    @XmlAttribute(name = "color_primaries")
    protected String colorPrimaries;
    @XmlAttribute(name = "chroma_location")
    protected String chromaLocation;
    @XmlAttribute(name = "field_order")
    protected String fieldOrder;
    @XmlAttribute(name = "timecode")
    protected String timecode;
    @XmlAttribute(name = "refs")
    protected Integer refs;
    @XmlAttribute(name = "sample_fmt")
    protected String sampleFmt;
    @XmlAttribute(name = "sample_rate")
    protected Integer sampleRate;
    @XmlAttribute(name = "channels")
    protected Integer channels;
    @XmlAttribute(name = "channel_layout")
    protected String channelLayout;
    @XmlAttribute(name = "bits_per_sample")
    protected Integer bitsPerSample;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "r_frame_rate", required = true)
    protected String rFrameRate;
    @XmlAttribute(name = "avg_frame_rate", required = true)
    protected String avgFrameRate;
    @XmlAttribute(name = "time_base", required = true)
    protected String timeBase;
    @XmlAttribute(name = "start_pts")
    protected Long startPts;
    @XmlAttribute(name = "start_time")
    protected Float startTime;
    @XmlAttribute(name = "duration_ts")
    protected Long durationTs;
    @XmlAttribute(name = "duration")
    protected Float duration;
    @XmlAttribute(name = "bit_rate")
    protected Integer bitRate;
    @XmlAttribute(name = "max_bit_rate")
    protected Integer maxBitRate;
    @XmlAttribute(name = "bits_per_raw_sample")
    protected Integer bitsPerRawSample;
    @XmlAttribute(name = "nb_frames")
    protected Integer nbFrames;
    @XmlAttribute(name = "nb_read_frames")
    protected Integer nbReadFrames;
    @XmlAttribute(name = "nb_read_packets")
    protected Integer nbReadPackets;

    /**
     * Gets the value of the disposition property.
     * 
     * @return
     *     possible object is
     *     {@link StreamDispositionType }
     *     
     */
    public StreamDispositionType getDisposition() {
        return disposition;
    }

    /**
     * Sets the value of the disposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamDispositionType }
     *     
     */
    public void setDisposition(StreamDispositionType value) {
        this.disposition = value;
    }

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
     *     {@link PacketSideDataListType }
     *     
     */
    public PacketSideDataListType getSideDataList() {
        return sideDataList;
    }

    /**
     * Sets the value of the sideDataList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PacketSideDataListType }
     *     
     */
    public void setSideDataList(PacketSideDataListType value) {
        this.sideDataList = value;
    }

    /**
     * Gets the value of the index property.
     * 
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     * 
     */
    public void setIndex(int value) {
        this.index = value;
    }

    /**
     * Gets the value of the codecName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecName() {
        return codecName;
    }

    /**
     * Sets the value of the codecName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecName(String value) {
        this.codecName = value;
    }

    /**
     * Gets the value of the codecLongName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecLongName() {
        return codecLongName;
    }

    /**
     * Sets the value of the codecLongName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecLongName(String value) {
        this.codecLongName = value;
    }

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfile(String value) {
        this.profile = value;
    }

    /**
     * Gets the value of the codecType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecType() {
        return codecType;
    }

    /**
     * Sets the value of the codecType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecType(String value) {
        this.codecType = value;
    }

    /**
     * Gets the value of the codecTimeBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecTimeBase() {
        return codecTimeBase;
    }

    /**
     * Sets the value of the codecTimeBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecTimeBase(String value) {
        this.codecTimeBase = value;
    }

    /**
     * Gets the value of the codecTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecTag() {
        return codecTag;
    }

    /**
     * Sets the value of the codecTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecTag(String value) {
        this.codecTag = value;
    }

    /**
     * Gets the value of the codecTagString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodecTagString() {
        return codecTagString;
    }

    /**
     * Sets the value of the codecTagString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodecTagString(String value) {
        this.codecTagString = value;
    }

    /**
     * Gets the value of the extradata property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtradata() {
        return extradata;
    }

    /**
     * Sets the value of the extradata property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtradata(String value) {
        this.extradata = value;
    }

    /**
     * Gets the value of the extradataHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtradataHash() {
        return extradataHash;
    }

    /**
     * Sets the value of the extradataHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtradataHash(String value) {
        this.extradataHash = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

    /**
     * Gets the value of the codedWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodedWidth() {
        return codedWidth;
    }

    /**
     * Sets the value of the codedWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodedWidth(Integer value) {
        this.codedWidth = value;
    }

    /**
     * Gets the value of the codedHeight property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodedHeight() {
        return codedHeight;
    }

    /**
     * Sets the value of the codedHeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodedHeight(Integer value) {
        this.codedHeight = value;
    }

    /**
     * Gets the value of the hasBFrames property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHasBFrames() {
        return hasBFrames;
    }

    /**
     * Sets the value of the hasBFrames property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHasBFrames(Integer value) {
        this.hasBFrames = value;
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
     * Gets the value of the displayAspectRatio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    /**
     * Sets the value of the displayAspectRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayAspectRatio(String value) {
        this.displayAspectRatio = value;
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
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLevel(Integer value) {
        this.level = value;
    }

    /**
     * Gets the value of the colorRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorRange() {
        return colorRange;
    }

    /**
     * Sets the value of the colorRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorRange(String value) {
        this.colorRange = value;
    }

    /**
     * Gets the value of the colorSpace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorSpace() {
        return colorSpace;
    }

    /**
     * Sets the value of the colorSpace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorSpace(String value) {
        this.colorSpace = value;
    }

    /**
     * Gets the value of the colorTransfer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorTransfer() {
        return colorTransfer;
    }

    /**
     * Sets the value of the colorTransfer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorTransfer(String value) {
        this.colorTransfer = value;
    }

    /**
     * Gets the value of the colorPrimaries property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorPrimaries() {
        return colorPrimaries;
    }

    /**
     * Sets the value of the colorPrimaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorPrimaries(String value) {
        this.colorPrimaries = value;
    }

    /**
     * Gets the value of the chromaLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChromaLocation() {
        return chromaLocation;
    }

    /**
     * Sets the value of the chromaLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChromaLocation(String value) {
        this.chromaLocation = value;
    }

    /**
     * Gets the value of the fieldOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldOrder() {
        return fieldOrder;
    }

    /**
     * Sets the value of the fieldOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldOrder(String value) {
        this.fieldOrder = value;
    }

    /**
     * Gets the value of the timecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimecode() {
        return timecode;
    }

    /**
     * Sets the value of the timecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimecode(String value) {
        this.timecode = value;
    }

    /**
     * Gets the value of the refs property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRefs() {
        return refs;
    }

    /**
     * Sets the value of the refs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRefs(Integer value) {
        this.refs = value;
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
     * Gets the value of the sampleRate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSampleRate() {
        return sampleRate;
    }

    /**
     * Sets the value of the sampleRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSampleRate(Integer value) {
        this.sampleRate = value;
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
     * Gets the value of the bitsPerSample property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBitsPerSample() {
        return bitsPerSample;
    }

    /**
     * Sets the value of the bitsPerSample property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBitsPerSample(Integer value) {
        this.bitsPerSample = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the rFrameRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRFrameRate() {
        return rFrameRate;
    }

    /**
     * Sets the value of the rFrameRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRFrameRate(String value) {
        this.rFrameRate = value;
    }

    /**
     * Gets the value of the avgFrameRate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvgFrameRate() {
        return avgFrameRate;
    }

    /**
     * Sets the value of the avgFrameRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvgFrameRate(String value) {
        this.avgFrameRate = value;
    }

    /**
     * Gets the value of the timeBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeBase() {
        return timeBase;
    }

    /**
     * Sets the value of the timeBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeBase(String value) {
        this.timeBase = value;
    }

    /**
     * Gets the value of the startPts property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStartPts() {
        return startPts;
    }

    /**
     * Sets the value of the startPts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStartPts(Long value) {
        this.startPts = value;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setStartTime(Float value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the durationTs property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDurationTs() {
        return durationTs;
    }

    /**
     * Sets the value of the durationTs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDurationTs(Long value) {
        this.durationTs = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDuration(Float value) {
        this.duration = value;
    }

    /**
     * Gets the value of the bitRate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBitRate() {
        return bitRate;
    }

    /**
     * Sets the value of the bitRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBitRate(Integer value) {
        this.bitRate = value;
    }

    /**
     * Gets the value of the maxBitRate property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxBitRate() {
        return maxBitRate;
    }

    /**
     * Sets the value of the maxBitRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxBitRate(Integer value) {
        this.maxBitRate = value;
    }

    /**
     * Gets the value of the bitsPerRawSample property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBitsPerRawSample() {
        return bitsPerRawSample;
    }

    /**
     * Sets the value of the bitsPerRawSample property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBitsPerRawSample(Integer value) {
        this.bitsPerRawSample = value;
    }

    /**
     * Gets the value of the nbFrames property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbFrames() {
        return nbFrames;
    }

    /**
     * Sets the value of the nbFrames property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbFrames(Integer value) {
        this.nbFrames = value;
    }

    /**
     * Gets the value of the nbReadFrames property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbReadFrames() {
        return nbReadFrames;
    }

    /**
     * Sets the value of the nbReadFrames property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbReadFrames(Integer value) {
        this.nbReadFrames = value;
    }

    /**
     * Gets the value of the nbReadPackets property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNbReadPackets() {
        return nbReadPackets;
    }

    /**
     * Sets the value of the nbReadPackets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNbReadPackets(Integer value) {
        this.nbReadPackets = value;
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

    public StreamType withDisposition(StreamDispositionType value) {
        setDisposition(value);
        return this;
    }

    public StreamType withTag(TagType... values) {
        if (values!= null) {
            for (TagType value: values) {
                getTag().add(value);
            }
        }
        return this;
    }

    public StreamType withTag(Collection<TagType> values) {
        if (values!= null) {
            getTag().addAll(values);
        }
        return this;
    }

    public StreamType withSideDataList(PacketSideDataListType value) {
        setSideDataList(value);
        return this;
    }

    public StreamType withIndex(int value) {
        setIndex(value);
        return this;
    }

    public StreamType withCodecName(String value) {
        setCodecName(value);
        return this;
    }

    public StreamType withCodecLongName(String value) {
        setCodecLongName(value);
        return this;
    }

    public StreamType withProfile(String value) {
        setProfile(value);
        return this;
    }

    public StreamType withCodecType(String value) {
        setCodecType(value);
        return this;
    }

    public StreamType withCodecTimeBase(String value) {
        setCodecTimeBase(value);
        return this;
    }

    public StreamType withCodecTag(String value) {
        setCodecTag(value);
        return this;
    }

    public StreamType withCodecTagString(String value) {
        setCodecTagString(value);
        return this;
    }

    public StreamType withExtradata(String value) {
        setExtradata(value);
        return this;
    }

    public StreamType withExtradataHash(String value) {
        setExtradataHash(value);
        return this;
    }

    public StreamType withWidth(Integer value) {
        setWidth(value);
        return this;
    }

    public StreamType withHeight(Integer value) {
        setHeight(value);
        return this;
    }

    public StreamType withCodedWidth(Integer value) {
        setCodedWidth(value);
        return this;
    }

    public StreamType withCodedHeight(Integer value) {
        setCodedHeight(value);
        return this;
    }

    public StreamType withHasBFrames(Integer value) {
        setHasBFrames(value);
        return this;
    }

    public StreamType withSampleAspectRatio(String value) {
        setSampleAspectRatio(value);
        return this;
    }

    public StreamType withDisplayAspectRatio(String value) {
        setDisplayAspectRatio(value);
        return this;
    }

    public StreamType withPixFmt(String value) {
        setPixFmt(value);
        return this;
    }

    public StreamType withLevel(Integer value) {
        setLevel(value);
        return this;
    }

    public StreamType withColorRange(String value) {
        setColorRange(value);
        return this;
    }

    public StreamType withColorSpace(String value) {
        setColorSpace(value);
        return this;
    }

    public StreamType withColorTransfer(String value) {
        setColorTransfer(value);
        return this;
    }

    public StreamType withColorPrimaries(String value) {
        setColorPrimaries(value);
        return this;
    }

    public StreamType withChromaLocation(String value) {
        setChromaLocation(value);
        return this;
    }

    public StreamType withFieldOrder(String value) {
        setFieldOrder(value);
        return this;
    }

    public StreamType withTimecode(String value) {
        setTimecode(value);
        return this;
    }

    public StreamType withRefs(Integer value) {
        setRefs(value);
        return this;
    }

    public StreamType withSampleFmt(String value) {
        setSampleFmt(value);
        return this;
    }

    public StreamType withSampleRate(Integer value) {
        setSampleRate(value);
        return this;
    }

    public StreamType withChannels(Integer value) {
        setChannels(value);
        return this;
    }

    public StreamType withChannelLayout(String value) {
        setChannelLayout(value);
        return this;
    }

    public StreamType withBitsPerSample(Integer value) {
        setBitsPerSample(value);
        return this;
    }

    public StreamType withId(String value) {
        setId(value);
        return this;
    }

    public StreamType withRFrameRate(String value) {
        setRFrameRate(value);
        return this;
    }

    public StreamType withAvgFrameRate(String value) {
        setAvgFrameRate(value);
        return this;
    }

    public StreamType withTimeBase(String value) {
        setTimeBase(value);
        return this;
    }

    public StreamType withStartPts(Long value) {
        setStartPts(value);
        return this;
    }

    public StreamType withStartTime(Float value) {
        setStartTime(value);
        return this;
    }

    public StreamType withDurationTs(Long value) {
        setDurationTs(value);
        return this;
    }

    public StreamType withDuration(Float value) {
        setDuration(value);
        return this;
    }

    public StreamType withBitRate(Integer value) {
        setBitRate(value);
        return this;
    }

    public StreamType withMaxBitRate(Integer value) {
        setMaxBitRate(value);
        return this;
    }

    public StreamType withBitsPerRawSample(Integer value) {
        setBitsPerRawSample(value);
        return this;
    }

    public StreamType withNbFrames(Integer value) {
        setNbFrames(value);
        return this;
    }

    public StreamType withNbReadFrames(Integer value) {
        setNbReadFrames(value);
        return this;
    }

    public StreamType withNbReadPackets(Integer value) {
        setNbReadPackets(value);
        return this;
    }

    public void setTag(List<TagType> value) {
        this.tag = value;
    }

}
