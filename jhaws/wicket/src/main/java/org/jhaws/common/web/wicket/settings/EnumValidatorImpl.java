package org.jhaws.common.web.wicket.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    List<String> valueList = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || valueList.contains(value);
    }

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();
        boolean identified = Supplier.class.isAssignableFrom(enumClass);
        Enum<?>[] enumValArr = enumClass.getEnumConstants();
        for (Enum<?> enumVal : enumValArr) {
            if (identified) {
                valueList.add(String.valueOf(Supplier.class.cast(enumVal).get()));
            } else {
                valueList.add(enumVal.name());
            }
        }
    }
}