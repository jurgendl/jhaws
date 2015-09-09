package org.jhaws.common.net.client.latest;

import org.htmlcleaner.TagNode;

public class TextArea implements InputElement {
    private static final long serialVersionUID = 1209409116199015320L;

    private final String id;

    private final String name;

    private String value;

    public TextArea(TagNode textareanode) {
        this.name = textareanode.getAttributeByName("name");
        this.id = textareanode.getAttributeByName("id");
        this.value = textareanode.getText().toString();
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
        return InputType.textarea;
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
        return "input[type=textarea,name=" + this.name + ",id=" + this.id + ",value=" + this.value + "]";
    }
}
