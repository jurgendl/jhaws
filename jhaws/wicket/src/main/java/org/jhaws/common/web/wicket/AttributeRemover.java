package org.jhaws.common.web.wicket;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

public class AttributeRemover extends AttributeModifier {
    public static AttributeRemover remove(String attributeName, Serializable value) {
        Args.notEmpty(attributeName, "attributeName");
        return remove(attributeName, createModel(value));
    }

    public static AttributeRemover remove(String attributeName, IModel<?> value) {
        Args.notEmpty(attributeName, "attributeName");
        return new AttributeRemover(attributeName, value).setSeparator(" ");
    }

    private static IModel<?> createModel(final Serializable value) {
        IModel<Serializable> model;
        // WICKET UPGRADE
        /* if (value == VALUELESS_ATTRIBUTE_ADD) { model = new ValuelessAttributeAddModel(); } else if (value == VALUELESS_ATTRIBUTE_REMOVE) { model = new
         * ValuelessAttributeRemoveModel(); } else */ {
            model = Model.of(value);
        }
        return model;
    }

    private static final long serialVersionUID = 1L;

    private String separator = " ";

    @Deprecated
    public AttributeRemover(String attribute, boolean addAttributeIfNotPresent, IModel<?> appendModel, String separator) {
        this(attribute, appendModel, separator);
    }

    public AttributeRemover(String attribute, IModel<?> replaceModel) {
        super(attribute, replaceModel);
    }

    public AttributeRemover(String attribute, Serializable value) {
        super(attribute, value);
    }

    public AttributeRemover(String attribute, Serializable value, String separator) {
        super(attribute, value);
        setSeparator(separator);
    }

    public AttributeRemover(String attribute, IModel<?> appendModel, String separator) {
        super(attribute, appendModel);
        setSeparator(separator);
    }

    public String getSeparator() {
        return separator;
    }

    public AttributeRemover setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    @Override
    protected String newValue(String currentValue, String removeValue) {
        if (Strings.isEmpty(currentValue)) {
            return currentValue;
        }
        if (Strings.isEmpty(removeValue)) {
            return currentValue;
        }
        return Arrays.stream(currentValue.split(getSeparator())).filter(v -> !v.equals(removeValue)).collect(Collectors.joining(getSeparator()));
    }

    @Override
    public String toString() {
        String attributeModifier = super.toString();
        attributeModifier = attributeModifier.substring(0, attributeModifier.length() - 2) + ", separator=" + separator + "]";
        return attributeModifier;
    }
}
