package org.jhaws.common.web.wicket;

import org.apache.wicket.model.IModel;

public abstract class AbstractReadOnlyModel<T> implements IModel<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * @see IModel#getObject()
	 */
	@Override
	public abstract T getObject();

	/**
	 * This default implementation of setObject unconditionally throws an
	 * UnsupportedOperationException. Since the method is final, any subclass is
	 * effectively a read-only model.
	 * 
	 * @param object The object to set into the model
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final void setObject(final T object) {
		throw new UnsupportedOperationException("Model " + getClass() + " does not support setObject(Object)");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Model:classname=[");
		sb.append(getClass().getName()).append("]");
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void detach() {
	}
}