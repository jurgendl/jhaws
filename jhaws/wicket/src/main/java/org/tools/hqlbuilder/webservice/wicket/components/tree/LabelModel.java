package org.tools.hqlbuilder.webservice.wicket.components.tree;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public class LabelModel implements IModel<String> {

	private static final long serialVersionUID = -2309269739547743819L;

	protected final Component component;

	protected final String key;

	public LabelModel(Component component, Object key) {
		this.component = component;
		String k;
		try {
			k = WebHelper.name(key);
		} catch (Exception ex) {
			k = (String) key;
		}
		this.key = k;
	}

	@Override
	public void detach() {
		//
	}

	public Component getComponent() {
		return component;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String getObject() {
		return component.getString(key);
	}

	@Override
	public void setObject(String object) {
		//
	}
}
