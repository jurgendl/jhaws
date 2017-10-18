package org.tools.hqlbuilder.webservice.wicket.renderer;

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
}
