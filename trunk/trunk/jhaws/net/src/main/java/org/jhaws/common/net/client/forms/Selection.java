package org.jhaws.common.net.client.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlcleaner.TagNode;

/**
 * Selection
 */
public class Selection implements InputElement {
    private static final long serialVersionUID = -434561563846856302L;

    /** options */
    private final List<String> options = new ArrayList<String>();

    /** name */
    private final String id;

    /** name */
    private final String name;

    /** selected */
    private String value;

    /** multiple */
    private final boolean multiple;

    @SuppressWarnings("unchecked")
    public Selection(TagNode selectnode) {
        List<TagNode> optionlist = selectnode.getElementListByName("option", true);
        String selected = null;

        for (TagNode optionnode : optionlist) {
            String v = optionnode.getAttributeByName("value");

            if (v == null) {
                v = optionnode.getText().toString().trim();
            }

            this.options.add(v);

            if ("selected".equals(optionnode.getAttributeByName("selected"))) {
                selected = v;
            }
        }

        this.value = selected;

        this.name = selectnode.getAttributeByName("name");
        this.id = selectnode.getAttributeByName("id");
        this.multiple = "multiple".equals(selectnode.getAttributeByName("multiple"));
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

    public List<String> getOptions() {
        return Collections.unmodifiableList(this.options);
    }

    /**
     * 
     * @see ui.html.test.InputElement#getType()
     */
    @Override
    public InputType getType() {
        return InputType.select;
    }

    /**
     * 
     * @see org.jhaws.common.net.client.forms.InputElement#getValue()
     */
    @Override
    public String getValue() {
        return this.value;
    }

    public boolean isMultiple() {
        return this.multiple;
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
        return "input[type=select,name=" + this.name + ",id=" + this.id + ",multiple=" + this.multiple + ",options=" + this.options + ",value="
                + this.value + "]";
    }
}
