package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.function.Supplier;

import org.apache.wicket.model.LoadableDetachableModel;

@SuppressWarnings("serial")
public class SupplyingModel<T> extends LoadableDetachableModel<T> {
	private Supplier<T> supplier;

	public <S extends Supplier<T> & Serializable> SupplyingModel(S supplier) {
		this.supplier = supplier;
	}

	@Override
	protected T load() {
		return supplier.get();
	}

	@Override
	public void detach() {
		super.detach();
		setObject(null);
	}
}
