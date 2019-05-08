package org.jhaws.common.lang;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BigIntegerValue extends Value<BigInteger> {
    private static final long serialVersionUID = -1530814975734843133L;

    public BigIntegerValue() {
        super(BigInteger.valueOf(0));
    }

    public BigIntegerValue(BigInteger value) {
        super(value);
    }

    public BigIntegerValue add(BigInteger i) {
        set(get().add(i));
        return this;
    }

    public BigIntegerValue remove(BigInteger i) {
        set(get().subtract(i));
        return this;
    }
}
