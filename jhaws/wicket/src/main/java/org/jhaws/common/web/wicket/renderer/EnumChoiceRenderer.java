package org.jhaws.common.web.wicket.renderer;

import java.util.List;

import org.apache.wicket.model.IModel;

public class EnumChoiceRenderer<T extends Enum<? super T>> implements org.apache.wicket.markup.html.form.IChoiceRenderer<T> {
    private static final long serialVersionUID = 4155598479192255747L;

    @Override
    public Object getDisplayValue(T object) {
        return object == null ? "" : object.name();
    }

    @Override
    public String getIdValue(T object, int index) {
        return object == null ? "" : object.name();
    }

    @Override
    public T getObject(String id, IModel<? extends List<? extends T>> choices) {
        return choices.getObject().stream().filter(it -> it.name().contentEquals(id)).findAny().orElse(null);
    }
}
