package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class And<T> implements Predicate<List<T>> {
    static public <T> And<T> and(@SuppressWarnings("unchecked") T... options) {
        return new And<T>(options);
    }

    static public <T> And<T> and(List<T> options) {
        return new And<T>(options);
    }

    @SuppressWarnings("unchecked")
    static public <X> And<X> cast(Object o) {
        return (And<X>) o;
    }

    protected List<T> and;

    public And() {
        super();
    }

    public And(@SuppressWarnings("unchecked") T... and) {
        this.and = new ArrayList<T>(Arrays.asList(and));
    }

    public And(List<T> and) {
        this.and = and;
    }

    public List<T> getAnd() {
        return this.and;
    }

    public void setAnd(List<T> and) {
        this.and = and;
    }

    public And<T> add(T t) {
        return and(t);
    }

    public And<T> and(T t) {
        if (and == null)
            and = new ArrayList<T>(Arrays.asList(t));
        else
            and.add(t);
        return this;
    }

    @Override
    public String toString() {
        return and.toString().replace(", ", "&").replace("[", "").replace("]", "");
    }

    @Override
    public boolean test(List<T> t) {
        return and.equals(t);
    }

    public List<T> all() {
        return and;
    }

    public boolean empty() {
        return and.isEmpty();
    }
}
