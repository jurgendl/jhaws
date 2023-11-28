package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.danekja.java.util.function.serializable.SerializableSupplier;

@SuppressWarnings("serial")
public class ReadOnceListModel<T extends Serializable> extends org.apache.wicket.model.util.ListModel<T> {
	protected SerializableSupplier<List<T>> supplier;

	private List<T> object;

	public ReadOnceListModel(SerializableSupplier<List<T>> supplier) {
		setSupplier(supplier);
	}

	public <M> ReadOnceListModel(IModel<M> parentModel, SerializableFunction<M, List<T>> supplier) {
		setSupplier(() -> supplier.apply(parentModel.getObject()));
	}

	public ReadOnceListModel(SupplyingListModel<T> model) {
		setSupplier(() -> model.getObject());
	}

	protected void setSupplier(SerializableSupplier<List<T>> supplier) {
		this.supplier = supplier;
	}

	@Override
	public List<T> getObject() {
		if (object == null) {
			object = createSerializableVersionOf(supplier.get());
		}
		return object;
	}

}
