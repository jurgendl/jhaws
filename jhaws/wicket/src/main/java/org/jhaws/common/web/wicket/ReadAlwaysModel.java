package org.jhaws.common.web.wicket;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.jhaws.common.web.wicket.components.tree.ObjectProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * model not serialized, always fetched
 *
 * @param <T>
 */
public class ReadAlwaysModel<T> extends AbstractReadOnlyModel<T> {
	private static final long serialVersionUID = -3334087534401284277L;

	private static Logger logger = LoggerFactory.getLogger(ReadAlwaysModel.class);

	protected ObjectProvider<T> provider;

	public ReadAlwaysModel(ObjectProvider<T> provider) {
		this.provider = provider;
	}

	public ReadAlwaysModel(T object) {
		this.provider = () -> object;
	}

	/**
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public T getObject() {
		T t = this.provider.get();
		ReadAlwaysModel.logger.trace("ReadAlwaysModel#load: {} -> {}", this, t);
		return t;
	}
}
