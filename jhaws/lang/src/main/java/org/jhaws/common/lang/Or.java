package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Or<T> implements Predicate<T> {
    static public <T> Or<T> or(@SuppressWarnings("unchecked") T... options) {
        return new Or<>(options);
    }

    static public Or<Integer> range(int from, int endInclusive) {
        return new Or<>(IntStream.range(from, endInclusive + 1).boxed().toArray(i -> new Integer[endInclusive - from + 1]));
    }

    static public <T> Or<T> or(List<T> options) {
        return new Or<>(options);
    }

    @SuppressWarnings("unchecked")
    static public <X> Or<X> cast(Object o) {
        return (Or<X>) o;
    }

    protected List<T> or;

    public Or() {
        super();
    }

    public Or(@SuppressWarnings("unchecked") T... or) {
        this.or = new ArrayList<>(Arrays.asList(or));
    }

    public Or(List<T> or) {
        this.or = or;
    }

    public List<T> getOr() {
        return this.or;
    }

    public void setOr(List<T> or) {
        this.or = or;
    }

    public Or<T> add(T t) {
        return or(t);
    }

    public Or<T> or(T t) {
        if (or == null)
            or = new ArrayList<>(Arrays.asList(t));
        else
            or.add(t);
        return this;
    }

    @Override
    public String toString() {
        return or.toString().replace(", ", "|").replace("[", "").replace("]", "");
    }

    @Override
    public boolean test(T t) {
        return or.contains(t);
    }

    public List<T> all() {
        return or;
    }

    public T first() {
        return or.get(0);
    }

    public boolean empty() {
        return or.isEmpty();
    }
}
