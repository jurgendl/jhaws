package org.tools.hqlbuilder.webservice.wicket.converter;

import org.apache.wicket.model.IModel;

public class ModelConverter<X, T> implements IModel<T>, Converter<X, T> {
    private static final long serialVersionUID = -7204131932307962506L;

    protected final IModel<X> backingModel;

    protected final Converter<X, T> converter;

    public ModelConverter(IModel<X> backingModel, Converter<X, T> converter) {
        this.backingModel = backingModel;
        this.converter = converter;
    }

    @Override
    public void detach() {
        backingModel.detach();
    }

    @Override
    public T getObject() {
        X object = backingModel.getObject();
        return object == null ? null : toType(object);
    }

    @Override
    public void setObject(T object) {
        backingModel.setObject(object == null ? null : fromType(object));
    }

    @Override
    public T toType(X object) {
        return converter.toType(object);
    }

    @Override
    public X fromType(T object) {
        return converter.fromType(object);
    }
}