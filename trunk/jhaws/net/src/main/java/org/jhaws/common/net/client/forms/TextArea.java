package org.jhaws.common.net.client.forms;

import org.htmlcleaner.TagNode;

/**
 * TextArea
 */
public class TextArea implements InputElement {
    private static final long serialVersionUID = 1209409116199015320L;

    /** name */
    private final String id;

    /** name */
    private final String name;

    /** value */
    private String value;

    public TextArea(TagNode textareanode) {
        this.name = textareanode.getAttributeByName("name");
        this.id = textareanode.getAttributeByName("id");
        this.value = textareanode.getText().toString();
    }

    /**
     * getId
     * 
     * @return the id
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * 
     * @see ui.html.test.InputElement#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @see ui.html.test.InputElement#getType()
     */
    @Override
    public InputType getType() {
        return InputType.textarea;
    }

    /**
     * 
     * @see ui.html.test.InputElement#getValue()
     */

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 
     * @see ui.html.test.InputElement#setValue(java.lang.String)
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
        return "input[type=textarea,name=" + this.name + ",id=" + this.id + ",value=" + this.value + "]";
    }
}
