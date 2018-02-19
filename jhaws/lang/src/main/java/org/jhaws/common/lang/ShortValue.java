package org.jhaws.common.lang;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShortValue extends Value<Short> {
    private static final long serialVersionUID = -1530814975734843133L;

    public ShortValue() {
        super((short) 0);
    }

    public ShortValue(short value) {
        super(value);
    }

    public ShortValue add() {
        return add((short) 1);
    }

    public ShortValue remove() {
        return add((short) -1);
    }

    public ShortValue add(short i) {
        set((short) (get() + i));
        return this;
    }

    public ShortValue remove(short i) {
        return add((short) -i);
    }
}
