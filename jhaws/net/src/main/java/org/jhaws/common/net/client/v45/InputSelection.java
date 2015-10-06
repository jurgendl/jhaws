package org.jhaws.common.net.client.v45;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlcleaner.TagNode;

public class InputSelection extends Input {
    private static final long serialVersionUID = -4190412373066318504L;

    private final List<String> options = new ArrayList<String>();

    public InputSelection(TagNode inputnode) {
        super(inputnode);
        this.options.add(this.getValue());

        if (!"checked".equals(inputnode.getAttributeByName("checked"))) {
            this.setValue(null);
        }
    }

    protected void addOption(String option) {
        this.options.add(option);
    }

    protected List<String> getOptions() {
        return Collections.unmodifiableList(this.options);
    }

    @Override
    public String toString() {
        return super.toString() + ",options=" + this.options;
    }
}
