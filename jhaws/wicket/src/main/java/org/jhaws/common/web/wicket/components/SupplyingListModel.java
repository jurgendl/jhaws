package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

import org.apache.wicket.model.IModel;

/**
 * {@link IModel} van {@link List}, opgevuld met een {@link Serializable}
 * {@link Supplier} (ps {@link Supplier} is niet {@link Serializable} by default
 * en geeft normaal problemen in Wicket maar deze class dus niet), je hoeft dus
 * ook niet telkens een subclass te maken<br>
 * <br>
 * bv. IModel<List<String>> listModel = new ListModel<>(() -> new
 * ArrayList<>());
 */
@SuppressWarnings("serial")
public class SupplyingListModel<T> extends org.apache.wicket.model.util.ListModel<T> {
	private Supplier<List<T>> supplier;

	public <S extends Supplier<List<T>> & Serializable> SupplyingListModel(S supplier) {
		this.supplier = supplier;
	}

	@Override
	public List<T> getObject() {
		return supplier.get();
	}

	@Override
	public void detach() {
		super.detach();
		setObject(null);
	}
}
