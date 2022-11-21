package org.jhaws.common.web.wicket.components;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

@SuppressWarnings("serial")
public class SupplyingModel<T> extends LoadableDetachableModel<T> {
	private SerializableSupplier<T> supplier;

	public SupplyingModel(SerializableSupplier<T> supplier) {
		setSupplier(supplier);
	}

	public <M> SupplyingModel(IModel<M> parentModel, SerializableFunction<M, T> supplier) {
		setSupplier(() -> supplier.apply(parentModel.getObject()));
	}

	protected void setSupplier(SerializableSupplier<T> supplier) {
		this.supplier = supplier;
	}

	@Override
	protected T load() {
		return supplier.get();
	}
}
