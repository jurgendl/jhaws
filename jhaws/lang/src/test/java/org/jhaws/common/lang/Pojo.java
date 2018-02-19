package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pojo {
    private String veld1;

    private int veld2;

    private String veld3;

    private NestedPojo nestedPojo = new NestedPojo();

    private String nestedPojoVeld;

    private CommonNestedPojo commonNested;

    private Collection<CommonNestedPojo> collection = new ArrayList<>();

    private CommonNestedPojo[] array;

    public String getVeld1() {
        return this.veld1;
    }

    public int getVeld2() {
        return this.veld2;
    }

    public String getVeld3() {
        return this.veld3;
    }

    public void setVeld1(String veld1) {
        this.veld1 = veld1;
    }

    public void setVeld2(int veld2) {
        this.veld2 = veld2;
    }

    public void setVeld3(String veld3) {
        this.veld3 = veld3;
    }

    public NestedPojo getNestedPojo() {
        return this.nestedPojo;
    }

    public void setNestedPojo(NestedPojo nestedPojo) {
        this.nestedPojo = nestedPojo;
    }

    public String getNestedPojoVeld() {
        return this.nestedPojoVeld;
    }

    public void setNestedPojoVeld(String nestedPojoVeld) {
        this.nestedPojoVeld = nestedPojoVeld;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("veld1", veld1)
                .append("veld2", veld2)
                .append("veld3", veld3)
                .append("nestedPojo", nestedPojo)
                .append("nestedPojoVeld", nestedPojoVeld)
                .append("commonNested", commonNested)
                .toString();
    }

    public CommonNestedPojo getCommonNested() {
        return this.commonNested;
    }

    public void setCommonNested(CommonNestedPojo commonNested) {
        this.commonNested = commonNested;
    }

    public Collection<CommonNestedPojo> getCollection() {
        return this.collection;
    }

    public void setCollection(Collection<CommonNestedPojo> collection) {
        this.collection = collection;
    }

    public CommonNestedPojo[] getArray() {
        return this.array;
    }

    public void setArray(CommonNestedPojo[] array) {
        this.array = array;
    }
}