package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class DTO {
    private String veld1;

    private Integer veld2;

    private Object veld3;

    private String nestedDTOVeld;

    private NestedDTO nestedDTO;

    private CommonNestedDTO commonNested;

    private Collection<CommonNestedDTO> collection = new ArrayList<>();

    private CommonNestedDTO[] array;

    public String getVeld1() {
        return this.veld1;
    }

    public Integer getVeld2() {
        return this.veld2;
    }

    public Object getVeld3() {
        return this.veld3;
    }

    public void setVeld1(String veld1) {
        this.veld1 = veld1;
    }

    public void setVeld2(Integer veld2) {
        this.veld2 = veld2;
    }

    public void setVeld3(Object veld3) {
        this.veld3 = veld3;
    }

    public String getNestedDTOVeld() {
        return this.nestedDTOVeld;
    }

    public NestedDTO getNestedDTO() {
        return this.nestedDTO;
    }

    public void setNestedDTOVeld(String nestedDTOVeld) {
        this.nestedDTOVeld = nestedDTOVeld;
    }

    public void setNestedDTO(NestedDTO nestedDTO) {
        this.nestedDTO = nestedDTO;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("veld1", veld1)
                .append("veld2", veld2)
                .append("veld3", veld3)
                .append("nestedDTOVeld", nestedDTOVeld)
                .append("nestedDTO", nestedDTO)
                .append("commonNested", commonNested)
                .toString();
    }

    public CommonNestedDTO getCommonNested() {
        return this.commonNested;
    }

    public void setCommonNested(CommonNestedDTO commonNested) {
        this.commonNested = commonNested;
    }

    public Collection<CommonNestedDTO> getCollection() {
        return this.collection;
    }

    public void setCollection(Collection<CommonNestedDTO> collection) {
        this.collection = collection;
    }

    public CommonNestedDTO[] getArray() {
        return this.array;
    }

    public void setArray(CommonNestedDTO[] array) {
        this.array = array;
    }
}