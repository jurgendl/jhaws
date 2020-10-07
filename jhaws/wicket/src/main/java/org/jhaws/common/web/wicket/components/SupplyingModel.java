package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

@SuppressWarnings("serial")
public class SupplyingModel<T> extends LoadableDetachableModel<T> {
    private Supplier<T> supplier;

    public <S extends Supplier<T> & Serializable> SupplyingModel(S supplier) {
        setSupplier(supplier);
    }

    public <M, S extends Function<M, T> & Serializable> SupplyingModel(IModel<? extends M> parentModel, S supplier) {
        setSupplier(() -> supplier.apply(parentModel.getObject()));
    }

    protected <S extends Supplier<T> & Serializable> void setSupplier(S supplier) {
        this.supplier = supplier;
    }

    @Override
    protected T load() {
        return supplier.get();
    }
}
