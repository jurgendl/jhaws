package org.jhaws.common.web.wicket.components;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

@SuppressWarnings("serial")
public class SupplyingListModel<T> extends org.apache.wicket.model.util.ListModel<T> {
	protected SerializableSupplier<List<T>> supplier;

	public SupplyingListModel(SerializableSupplier<List<T>> supplier) {
		setSupplier(supplier);
	}

	public SupplyingListModel(IModel<List<T>> listModel) {
		setSupplier(() -> listModel.getObject());
	}

	public <M> SupplyingListModel(IModel<M> parentModel, SerializableFunction<M, List<T>> supplier) {
		setSupplier(() -> supplier.apply(parentModel.getObject()));
	}

	protected void setSupplier(SerializableSupplier<List<T>> supplier) {
		this.supplier = supplier;
	}

	@Override
	public List<T> getObject() {
		return supplier.get();
	}
}
