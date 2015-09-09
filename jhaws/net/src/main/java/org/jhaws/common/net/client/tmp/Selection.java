package org.jhaws.common.net.client.tmp;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.TagNode;

public class Selection implements InputElement {
    private static final long serialVersionUID = -434561563846856302L;

    private final String id;

    private final String name;

    private String value;

    private Map<String, String> options = new LinkedHashMap<String, String>();

    private final boolean multiple;

    public Selection(TagNode selectnode) {
        List<? extends TagNode> optionlist = selectnode.getElementListByName("option", true);
        String selected = null;

        for (TagNode optionnode : optionlist) {
            String v = optionnode.getAttributeByName("value");
            String t = optionnode.getText().toString().trim();

            if (v == null) {
                v = optionnode.getText().toString().trim();
            }

            this.options.put(v, t);

            if ("selected".equals(optionnode.getAttributeByName("selected"))) {
                selected = v;
            }
        }

        this.value = selected;

        this.name = selectnode.getAttributeByName("name");
        this.id = selectnode.getAttributeByName("id");
        this.multiple = "multiple".equals(selectnode.getAttributeByName("multiple"));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Map<String, String> getOptions() {
        return Collections.unmodifiableMap(this.options);
    }

    @Override
    public InputType getType() {
        return InputType.select;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public boolean isMultiple() {
        return this.multiple;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "input[type=select,name=" + this.name + ",id=" + this.id + ",multiple=" + this.multiple + ",options=" + this.options + ",value="
                + this.value + "]";
    }
}
