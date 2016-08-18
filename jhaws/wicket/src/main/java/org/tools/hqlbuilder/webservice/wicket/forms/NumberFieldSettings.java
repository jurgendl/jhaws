package org.tools.hqlbuilder.webservice.wicket.forms;

public class NumberFieldSettings<N extends Number & Comparable<N>> extends AbstractNumberFieldSettings<N, NumberFieldSettings<N>> {
    private static final long serialVersionUID = 3281551248020647957L;

    public NumberFieldSettings(boolean required, N minimum, N maximum, N step) {
        super(required, minimum, maximum, step);
    }

    public NumberFieldSettings(N minimum, N maximum, N step) {
        super(minimum, maximum, step);
    }
}
