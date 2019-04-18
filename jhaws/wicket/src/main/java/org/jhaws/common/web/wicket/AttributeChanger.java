package org.jhaws.common.web.wicket;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;

public class AttributeChanger {
    private static final String SEPARATOR = " ";
    private static final String CLASS = "class";

    private AttributeChanger() {
        super();
    }

    public static AttributeRemover remove(String attributeName, IModel<?> value) {
        return AttributeRemover.remove(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeRemover remove(String attributeName, Serializable value) {
        return AttributeRemover.remove(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeModifier replace(String attributeName, IModel<?> value) {
        return AttributeModifier.replace(attributeName, value);
    }

    public static AttributeModifier replace(String attributeName, Serializable value) {
        return AttributeModifier.replace(attributeName, value);
    }

    public static AttributeAppender append(String attributeName, IModel<?> value) {
        return AttributeModifier.append(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender append(String attributeName, Serializable value) {
        return AttributeModifier.append(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender prepend(String attributeName, IModel<?> value) {
        return AttributeModifier.prepend(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender prepend(String attributeName, Serializable value) {
        return AttributeModifier.prepend(attributeName, value).setSeparator(SEPARATOR);
    }

    public static AttributeRemover removeClass(IModel<?> value) {
        return AttributeRemover.remove(CLASS, value).setSeparator(SEPARATOR);
    }

    public static AttributeRemover removeClass(Serializable value) {
        return AttributeRemover.remove(CLASS, value).setSeparator(SEPARATOR);
    }

    public static AttributeModifier replaceClass(IModel<?> value) {
        return AttributeModifier.replace(CLASS, value);
    }

    public static AttributeModifier replaceClass(Serializable value) {
        return AttributeModifier.replace(CLASS, value);
    }

    public static AttributeAppender appendClass(IModel<?> value) {
        return AttributeModifier.append(CLASS, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender appendClass(Serializable value) {
        return AttributeModifier.append(CLASS, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender prependClass(IModel<?> value) {
        return AttributeModifier.prepend(CLASS, value).setSeparator(SEPARATOR);
    }

    public static AttributeAppender prependClass(Serializable value) {
        return AttributeModifier.prepend(CLASS, value).setSeparator(SEPARATOR);
    }
}
