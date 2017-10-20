package org.tools.hqlbuilder.webservice.wicket.renderer;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EnumOptionRenderer<T extends Enum<? super T>> implements IOptionRenderer<T> {
    private static final long serialVersionUID = 3093280303783185203L;

    public EnumOptionRenderer() {
        super();
    }

    @Override
    public String getDisplayValue(T object) {
        return object == null ? null : object.name();
    }

    @Override
    public IModel<T> getModel(T value) {
        return Model.of(value);
    }
}
