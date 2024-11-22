package org.jhaws.common.web.wicket.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.jhaws.common.lang.functions.SerializableBooleanSupplier;
import org.jhaws.common.lang.functions.SerializableFunction;

@SuppressWarnings("serial")
public class EnhancedPanel extends WebMarkupContainer {
	protected SerializableBooleanSupplier visiblePredicate;

	public EnhancedPanel(String id, IModel<?> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	public EnhancedPanel(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (getOutputMarkupId() && getRenderBodyOnly()) {
			setOutputMarkupId(false);
		}
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		if (visiblePredicate != null) {
			boolean visible = visiblePredicate.getAsBoolean();
			setVisible(visible);
		}
	}

	public SerializableBooleanSupplier getVisiblePredicate() {
		return this.visiblePredicate;
	}

	public void setVisiblePredicate(SerializableBooleanSupplier visiblePredicate) {
		this.visiblePredicate = visiblePredicate;
	}

	public <T> void setVisiblePredicate(IModel<T> model, SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicate(() -> visiblePredicate0.apply(model.getObject()));
	}

	public void setVisiblePredicate(IModel<String> stringModel) {
		setVisiblePredicate(stringModel, StringUtils::isNotBlank);
	}

	public void setVisiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel, list -> list != null && !list.isEmpty());
	}

	public void setVisiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicate(stringModel, StringUtils::isBlank);
	}

	public void setVisiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicate(listModel, list -> list == null || list.isEmpty());
	}

	public EnhancedPanel visiblePredicate(SerializableBooleanSupplier visiblePredicate0) {
		setVisiblePredicate(visiblePredicate0);
		return this;
	}

	public <T> EnhancedPanel visiblePredicate(IModel<T> model, SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicate(model, visiblePredicate0);
		return this;
	}

	public EnhancedPanel visiblePredicate(IModel<String> stringModel) {
		setVisiblePredicate(stringModel);
		return this;
	}

	public EnhancedPanel visiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel);
		return this;
	}

	public EnhancedPanel visiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicateInvers(stringModel);
		return this;
	}

	public EnhancedPanel visiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicateInvers(listModel);
		return this;
	}

	public <T> void setVisiblePredicateInvers(IModel<T> model, SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicate(() -> !visiblePredicate0.apply(model.getObject()));
	}

	public <T> EnhancedPanel visiblePredicateInvers(IModel<T> model,
			SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicateInvers(model, visiblePredicate0);
		return this;
	}

	public void setVisibility(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> visible);
	}

	public EnhancedPanel visibility(IModel<Boolean> booleanModel) {
		setVisibility(booleanModel);
		return this;
	}

	public void setVisibilityInvers(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> !visible);
	}

	public EnhancedPanel visibilityInvers(IModel<Boolean> booleanModel) {
		setVisibilityInvers(booleanModel);
		return this;
	}
}
