package org.tools.hqlbuilder.webservice.wicket.forms;

public abstract class AbstractNumberFieldSettings<N extends Number & Comparable<N>, T extends AbstractNumberFieldSettings<N, T>> extends
        AbstractFormElementSettings<T> {
    private static final long serialVersionUID = 5784680461033564819L;

    protected N minimum;

    protected N maximum;

    protected N step;

    public AbstractNumberFieldSettings(N minimum, N maximum, N step) {
        setMinimum(minimum);
        setMaximum(maximum);
        setStep(step);
    }

    public AbstractNumberFieldSettings(boolean required, N minimum, N maximum, N step) {
        super(required);
        setMinimum(minimum);
        setMaximum(maximum);
        setStep(step);
    }

    public N getMinimum() {
        return this.minimum;
    }

    public N getMaximum() {
        return this.maximum;
    }

    public N getStep() {
        return this.step;
    }

    public AbstractNumberFieldSettings<N, T> setMinimum(N minimum) {
        this.minimum = minimum;
        return this;
    }

    public AbstractNumberFieldSettings<N, T> setMaximum(N maximum) {
        this.maximum = maximum;
        return this;
    }

    public AbstractNumberFieldSettings<N, T> setStep(N step) {
        this.step = step;
        return this;
    }
}
