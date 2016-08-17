package org.jhaws.common.web.wicket;

import org.apache.wicket.model.LoadableDetachableModel;
import org.jhaws.common.web.wicket.components.tree.ObjectProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * model not serialized, once fetched
 *
 * @param <T>
 */
public class ReadOnceModel<T> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 5727586695739529518L;

	private static Logger logger = LoggerFactory.getLogger(ReadOnceModel.class);

	protected ObjectProvider<T> provider;

	public ReadOnceModel(ObjectProvider<T> provider) {
		this.provider = provider;
	}

	public ReadOnceModel(T object) {
		this.provider = () -> object;
	}

	/**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected T load() {
		T t = this.provider.get();
		ReadOnceModel.logger.trace("ReadOnceModel#load: {} -> {}", this, t);
		return t;
	}
}
