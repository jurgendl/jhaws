package org.tools.hqlbuilder.webservice.wicket.forms;

public class RangeFieldSettings<N extends Number & Comparable<N>> extends AbstractNumberFieldSettings<N, RangeFieldSettings<N>> {
    private static final long serialVersionUID = -3095218945374192038L;

    protected N tickStep;

    public RangeFieldSettings(boolean required, N minimum, N maximum, N step) {
        super(required, minimum, maximum, step);
    }

    public RangeFieldSettings(N minimum, N maximum, N step) {
        super(minimum, maximum, step);
    }

    public N getTickStep() {
        return this.tickStep;
    }

    public RangeFieldSettings<N> setTickStep(N tickStep) {
        this.tickStep = tickStep;
        return this;
    }
}
