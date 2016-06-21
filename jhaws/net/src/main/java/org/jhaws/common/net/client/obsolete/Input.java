package org.jhaws.common.net.client.obsolete;

import org.htmlcleaner.TagNode;

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

    public Input(TagNode inputnode) {
        this.type = InputType.valueOf(inputnode.getAttributeByName("type").toLowerCase());
        this.name = inputnode.getAttributeByName("name");
        this.value = inputnode.getAttributeByName("value");
        this.id = inputnode.getAttributeByName("id");
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
