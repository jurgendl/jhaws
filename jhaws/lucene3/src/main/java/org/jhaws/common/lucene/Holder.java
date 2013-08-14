package org.jhaws.common.lucene;

public class Holder<T> {
    public T value;

    public Holder() {
        super();
    }

    public Holder(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
