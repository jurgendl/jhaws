package org.jhaws.common.net.client.forms;

import org.htmlcleaner.TagNode;

/**
 * Input
 */
public class Input implements InputElement {
    private static final long serialVersionUID = 8640479033953741491L;

    /** name */
    private final String id;

    /** name */
    private final String name;

    /** type */
    private final InputType type;

    /** value */
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

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#getType()
     */
    @Override
    public InputType getType() {
        return this.type;
    }

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#getValue()
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "input[type=" + this.type + ",name=" + this.name + ",id=" + this.id + ",value=" + this.value + "]";
    }
}
