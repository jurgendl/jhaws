package org.jhaws.common.web.wicket.components;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

@SuppressWarnings("serial")
public class ReadOnceModel<T extends Serializable> implements IModel<T> {
	protected SerializableSupplier<T> supplier;

	private T object;

	public ReadOnceModel(SerializableSupplier<T> supplier) {
		setSupplier(supplier);
	}

	public <M> ReadOnceModel(IModel<M> parentModel, SerializableFunction<M, T> supplier) {
		setSupplier(() -> supplier.apply(parentModel.getObject()));
	}

	public ReadOnceModel(SupplyingModel<T> model) {
		setSupplier(() -> model.getObject());
	}

	protected void setSupplier(SerializableSupplier<T> supplier) {
		this.supplier = supplier;
	}

	@Override
	public T getObject() {
		if (object == null) {
			object = supplier.get();
		}
		return object;
	}

}
