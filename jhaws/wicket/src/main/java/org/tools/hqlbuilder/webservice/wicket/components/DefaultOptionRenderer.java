package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class DefaultOptionRenderer<T extends Serializable> implements IOptionRenderer<T> {
    private static final long serialVersionUID = 3093280303783185203L;

    public DefaultOptionRenderer() {
        super();
    }

    @Override
    public String getDisplayValue(T object) {
        return String.valueOf(object);
    }

    @Override
    public IModel<T> getModel(T value) {
        return Model.of(value);
    }
}
