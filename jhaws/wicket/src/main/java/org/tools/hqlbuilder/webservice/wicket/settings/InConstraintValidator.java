package org.tools.hqlbuilder.webservice.wicket.settings;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InConstraintValidator implements ConstraintValidator<In, String> {
    private List<String> values;

    @Override
    public final void initialize(final In annotation) {
        values = Arrays.asList(annotation.values());
    }

    @Override
    public final boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return values.contains(value);
    }
}