package org.jhaws.common.lang;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringValue extends Value<String> {
    private static final long serialVersionUID = 6327052574471636557L;

    public StringValue() {
        super(null);
    }

    public StringValue(String value) {
        super(value);
    }

    public StringValue replace(char oldChar, char newChar) {
        operate(Objects::nonNull, s -> s.replace(oldChar, newChar));
        return this;
    }

    public StringValue replace(CharSequence target, CharSequence replacement) {
        operate(Objects::nonNull, s -> s.replace(target, replacement));
        return this;
    }

    public StringValue replaceAll(String regex, String replacement) {
        operate(Objects::nonNull, s -> s.replaceAll(regex, replacement));
        return this;
    }

    public StringValue replaceFirst(String regex, String replacement) {
        operate(Objects::nonNull, s -> s.replaceFirst(regex, replacement));
        return this;
    }

    public StringValue replaceLast(String regex, String replacement) {
        operate(Objects::nonNull, s -> StringUtils.replaceLast(s, regex, replacement));
        return this;
    }

    public StringValue append(String t) {
        operate(Objects::nonNull, s -> s + t, () -> t);
        return this;
    }

    public StringValue appendn(String t) {
        operate(Objects::nonNull, s -> s + "\n" + t, () -> t);
        return this;
    }
}
