package org.jhaws.common.web.wicket.renderer;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class StringOptionRenderer implements IOptionRenderer<String> {
    private static final long serialVersionUID = 3093280303783185203L;

    public StringOptionRenderer() {
        super();
    }

    @Override
    public String getDisplayValue(String object) {
        return object == null ? null : String.valueOf(object);
    }

    @Override
    public IModel<String> getModel(String value) {
        return Model.of(value);
    }
}
