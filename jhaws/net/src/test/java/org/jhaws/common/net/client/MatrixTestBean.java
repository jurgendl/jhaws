package org.jhaws.common.net.client;

import java.util.List;

import javax.ws.rs.MatrixParam;

public class MatrixTestBean implements MatrixTestBeanI {
    @MatrixParam("key1")
    private List<String> key1;

    @MatrixParam("key2")
    private String key2;

    @Override
    public String getKey2() {
        return this.key2;
    }

    @Override
    public void setKey2(String key2) {
        this.key2 = key2;
    }

    @Override
    public List<String> getKey1() {
        return this.key1;
    }

    @Override
    public void setKey1(List<String> key1) {
        this.key1 = key1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MatrixTestBean [");
        if (this.key1 != null) {
            builder.append("key1=");
            builder.append(this.key1);
            builder.append(", ");
        }
        if (this.key2 != null) {
            builder.append("key2=");
            builder.append(this.key2);
        }
        builder.append("]");
        return builder.toString();
    }
}
