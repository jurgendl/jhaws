package org.jhaws.common.web.wicket.components;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.jhaws.common.lang.functions.SerializableBooleanSupplier;
import org.jhaws.common.lang.functions.SerializableFunction;

@SuppressWarnings("serial")
public class EnhancedLabel extends Label {
	protected SerializableBooleanSupplier visiblePredicate;

	public EnhancedLabel(String id, IModel<?> model) {
		super(id, model);
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
	}

	public EnhancedLabel(String id, Serializable label) {
		super(id, label);
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
	}

	public EnhancedLabel(String id) {
		super(id);
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
	}

	public EnhancedLabel(String id, IModel<?> model, SerializableBooleanSupplier visiblePredicate) {
		super(id, model);
		setVisiblePredicate(visiblePredicate);
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
	}

	public EnhancedLabel(String id, Serializable label, SerializableBooleanSupplier visiblePredicate) {
		super(id, label);
		setVisiblePredicate(visiblePredicate);
		setEscapeModelStrings(false);
		setOutputMarkupId(true);
	}

	public EnhancedLabel(String id, SerializableBooleanSupplier visiblePredicate) {
		super(id);
		setVisiblePredicate(visiblePredicate);
		setEscapeModelStrings(false);
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
			setVisible(visiblePredicate.getAsBoolean());
		}
	}

	/**
	 * Visibility gedrag zo instellen dat element onzichtbaar is bij lege string
	 */
	public void setHideOnEmpty() {
		setVisiblePredicate(() -> StringUtils.isNotBlank(getDefaultModelObjectAsString()));
	}

	/**
	 * Hulpje bij constructor voor builder style instantiate <br/>
	 * EnhancedLabel titelLabel = new EnhancedLabel("titel",
	 * titelModel).hideOnEmpty();
	 */
	public EnhancedLabel hideOnEmpty() {
		setHideOnEmpty();
		return this;
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
		setVisiblePredicate(stringModel, org.apache.commons.lang3.StringUtils::isNotBlank);
	}

	public void setVisiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel, list -> list != null && !list.isEmpty());
	}

	public void setVisiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicate(stringModel, org.apache.commons.lang3.StringUtils::isBlank);
	}

	public void setVisiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicate(listModel, list -> list == null || list.isEmpty());
	}

	public EnhancedLabel visiblePredicate(SerializableBooleanSupplier visiblePredicate0) {
		setVisiblePredicate(visiblePredicate0);
		return this;
	}

	public <T> EnhancedLabel visiblePredicate(IModel<T> model, SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicate(model, visiblePredicate0);
		return this;
	}

	public EnhancedLabel visiblePredicate(IModel<String> stringModel) {
		setVisiblePredicate(stringModel);
		return this;
	}

	public EnhancedLabel visiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel);
		return this;
	}

	public EnhancedLabel visiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicateInvers(stringModel);
		return this;
	}

	public EnhancedLabel visiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicateInvers(listModel);
		return this;
	}

	public <T> void setVisiblePredicateInvers(IModel<T> model, SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicate(() -> !visiblePredicate0.apply(model.getObject()));
	}

	public <T> EnhancedLabel visiblePredicateInvers(IModel<T> model,
			SerializableFunction<T, Boolean> visiblePredicate0) {
		setVisiblePredicateInvers(model, visiblePredicate0);
		return this;
	}

	public void setVisibility(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> visible);
	}

	public EnhancedLabel visibility(IModel<Boolean> booleanModel) {
		setVisibility(booleanModel);
		return this;
	}

	public void setVisibilityInvers(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> !visible);
	}

	public EnhancedLabel visibilityInvers(IModel<Boolean> booleanModel) {
		setVisibilityInvers(booleanModel);
		return this;
	}
}
