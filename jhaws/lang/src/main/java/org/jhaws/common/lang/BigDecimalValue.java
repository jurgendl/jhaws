package org.jhaws.common.lang;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BigDecimalValue extends Value<BigDecimal> {
    private static final long serialVersionUID = -1530814975734843133L;

    public BigDecimalValue() {
        super(new BigDecimal(0));
    }

    public BigDecimalValue(BigDecimal value) {
        super(value);
    }

    public BigDecimalValue add(BigDecimal i) {
        set(get().add(i));
        return this;
    }

    public BigDecimalValue remove(BigDecimal i) {
        set(get().subtract(i));
        return this;
    }
}
