package org.jhaws.common.net.client;

import org.apache.commons.lang3.StringUtils;

public class Input implements InputElement {
    private static final long serialVersionUID = 8640479033953741491L;

    private final String id;

    private final String name;

    private final InputType type;

    private String value;

    public Input(InputType type, String id, String name) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Input(org.jsoup.nodes.Element inputnode) {
        String t = inputnode.attr("type");
        if (StringUtils.isNotBlank(t)) {
            this.type = InputType.valueOf(t.toLowerCase());
        } else {
            System.out.println("NO TYPE " + inputnode);
            this.type = null;
        }
        this.name = inputnode.attr("name");
        this.value = inputnode.attr("value");
        this.id = inputnode.attr("id");
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public InputType getType() {
        return this.type;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "input[type=" + this.type + ",name=" + this.name + ",id=" + this.id + ",value=" + this.value + "]";
    }
}
