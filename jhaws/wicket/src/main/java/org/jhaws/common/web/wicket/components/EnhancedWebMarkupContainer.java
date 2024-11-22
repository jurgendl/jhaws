package org.jhaws.common.web.wicket.components;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.jhaws.common.lang.functions.SerializableBooleanSupplier;
import org.jhaws.common.lang.functions.SerializableFunction;
import org.jhaws.common.web.wicket.bootstrap.BootstrapFencedFeedbackPanel;

@SuppressWarnings("serial")
public class EnhancedWebMarkupContainer extends WebMarkupContainer {
	protected SerializableBooleanSupplier visiblePredicate;

	public EnhancedWebMarkupContainer(String id, IModel<?> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	public EnhancedWebMarkupContainer(String id) {
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
			setVisible(visiblePredicate.getAsBoolean());
		}
	}

	BootstrapFencedFeedbackPanel feedbackPanel = null;

	public BootstrapFencedFeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}

	public BootstrapFencedFeedbackPanel addFeedbackPanel(String id) {
		feedbackPanel = new BootstrapFencedFeedbackPanel(id, this);
		add(feedbackPanel);
		return feedbackPanel;
	}

	public SerializableBooleanSupplier getVisiblePredicate() {
		return this.visiblePredicate;
	}

	public void setVisiblePredicate(SerializableBooleanSupplier visiblePredicate) {
		this.visiblePredicate = visiblePredicate;
	}

	public <M> void setVisiblePredicate(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate) {
		setVisiblePredicate(() -> visiblePredicate.apply(model.getObject()));
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

	public EnhancedWebMarkupContainer visiblePredicate(SerializableBooleanSupplier _visiblePredicate) {
		setVisiblePredicate(_visiblePredicate);
		return this;
	}

	public <M> EnhancedWebMarkupContainer visiblePredicate(IModel<M> model,
			SerializableFunction<M, Boolean> _visiblePredicate) {
		setVisiblePredicate(model, _visiblePredicate);
		return this;
	}

	public EnhancedWebMarkupContainer visiblePredicate(IModel<String> stringModel) {
		setVisiblePredicate(stringModel);
		return this;
	}

	public EnhancedWebMarkupContainer visiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel);
		return this;
	}

	public EnhancedWebMarkupContainer visiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicateInvers(stringModel);
		return this;
	}

	public EnhancedWebMarkupContainer visiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicateInvers(listModel);
		return this;
	}

	public <M> void setVisiblePredicateInvers(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate) {
		setVisiblePredicate(() -> !visiblePredicate.apply(model.getObject()));
	}

	public <M> EnhancedWebMarkupContainer visiblePredicateInvers(IModel<M> model,
			SerializableFunction<M, Boolean> visiblePredicate0) {
		setVisiblePredicateInvers(model, visiblePredicate0);
		return this;
	}

	public void setVisibility(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> visible);
	}

	public EnhancedWebMarkupContainer visibility(IModel<Boolean> booleanModel) {
		setVisibility(booleanModel);
		return this;
	}

	public void setVisibilityInvers(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> !visible);
	}

	public EnhancedWebMarkupContainer visibilityInvers(IModel<Boolean> booleanModel) {
		setVisibilityInvers(booleanModel);
		return this;
	}
}
