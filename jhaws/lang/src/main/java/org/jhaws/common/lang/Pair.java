package org.jhaws.common.lang;

import java.util.Map.Entry;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;

@XmlRootElement
public class Pair<P> extends KeyValue<P, P> implements Comparable<Pair<P>> {
    private static final long serialVersionUID = 2133551453748910250L;

    public static <P> Pair<P> pair(P key, P value) {
        return new Pair<>(key, value);
    }

    public Pair() {
        super();
    }

    public Pair(Entry<P, P> entry) {
        super(entry);
    }

    public Pair(P keyValue) {
        super(keyValue, keyValue);
    }

    public Pair(P key, P value) {
        super(key, value);
    }

    public <I> boolean isEquals(Function<P, I> pf) {
        return isEquals(pf, pf);
    }

    public <I> boolean notEquals(Function<P, I> pf) {
        return !isEquals(pf);
    }

    @Override
    public int compareTo(Pair<P> o) {
        return new CompareToBuilder().append(key, o.key).append(value, o.value).toComparison();
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
