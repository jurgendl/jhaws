
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
 * Java class for programsType complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="programsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="program" type="{http://www.ffmpeg.org/schema/ffprobe}programType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "programsType", propOrder = { "program" })
public class ProgramsType {

    protected List<ProgramType> program;

    /**
     * Gets the value of the program property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be
     * present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the program property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getProgram().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link ProgramType }
     *
     *
     */
    public List<ProgramType> getProgram() {
        if (program == null) {
            program = new ArrayList<>();
        }
        return this.program;
    }

    /**
     * Generates a String representation of the contents of this type. This is an extension method, produced by the 'ts' xjc plugin
     *
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JAXBToStringStyle.DEFAULT_STYLE);
    }

    public ProgramsType withProgram(ProgramType... values) {
        if (values != null) {
            for (ProgramType value : values) {
                getProgram().add(value);
            }
        }
        return this;
    }

    public ProgramsType withProgram(Collection<ProgramType> values) {
        if (values != null) {
            getProgram().addAll(values);
        }
        return this;
    }

    public void setProgram(List<ProgramType> value) {
        this.program = value;
    }

}
