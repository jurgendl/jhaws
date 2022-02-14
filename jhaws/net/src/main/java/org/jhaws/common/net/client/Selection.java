package org.jhaws.common.net.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Selection implements InputElement {
    private static final long serialVersionUID = -434561563846856302L;

    private final String id;

    private final String name;

    private String value;

    private Map<String, String> options = new LinkedHashMap<>();

    private final boolean multiple;

    public Selection(org.jsoup.nodes.Element selectnode) {
        List<? extends org.jsoup.nodes.Element> optionlist = selectnode.select("option");
        String selected = null;

        for (org.jsoup.nodes.Element optionnode : optionlist) {
            String v = optionnode.attr("value");
            String t = optionnode.html().toString().trim();

            if (v == null) {
                v = optionnode.html().toString().trim();
            }

            this.options.put(v, t);

            if ("selected".equals(optionnode.attr("selected"))) {
                selected = v;
            }
        }

        this.value = selected;

        this.name = selectnode.attr("name");
        this.id = selectnode.attr("id");
        this.multiple = "multiple".equals(selectnode.attr("multiple"));
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
        return "input[type=select,name=" + this.name + ",id=" + this.id + ",multiple=" + this.multiple + ",options=" + this.options + ",value=" + this.value + "]";
    }
}
