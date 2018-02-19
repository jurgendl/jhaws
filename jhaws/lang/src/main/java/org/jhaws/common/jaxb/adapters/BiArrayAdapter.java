package org.jhaws.common.jaxb.adapters;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public abstract class BiArrayAdapter<N extends Number> extends XmlAdapter<String, N[][]> {
    private static final String SEP = ",";

    @Override
    public String marshal(N[][] v) {
        if (v == null) return null;
        return "[" + Arrays.stream(v).map(ia -> "[" + Arrays.stream(ia).map(String::valueOf).collect(Collectors.joining(SEP)) + "]").collect(
                Collectors.joining(SEP + "\n")) + "]";
    }

    @Override
    public N[][] unmarshal(String v) {
        if (v == null) return null;
        return (N[][]) Arrays.stream(v.replace("\n", "").replace("[[", "").replace("]]", "").split("\\],\\["))
                .map(s -> (N[]) Arrays.stream(s.split(SEP)).map(this::parse).toArray(this::single))
                .toArray(this::bi);
    }

    abstract N parse(String v);

    abstract N[] single(int i);

    abstract N[][] bi(int i);
}