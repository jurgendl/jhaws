package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.jhaws.common.lang.functions.SerializableBooleanSupplier;
import org.jhaws.common.lang.functions.SerializableConsumer;
import org.jhaws.common.lang.functions.SerializableFunction;

@SuppressWarnings("serial")
public class EnhancedListView<T> extends ListView<T> {
	protected SerializableBooleanSupplier visiblePredicate;

	protected SerializableConsumer<ListItem<T>> itemizer;

	public EnhancedListView(String id) {
		super(id);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, List<T> list) {
		super(id, list);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, IModel<? extends List<T>> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, ListModel<T> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, List<T> list, SerializableConsumer<ListItem<T>> itemizer) {
		super(id, list);
		setItemizer(itemizer);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, ListModel<T> model, SerializableConsumer<ListItem<T>> itemizer) {
		super(id, model);
		setItemizer(itemizer);
		setOutputMarkupId(true);
	}

	public EnhancedListView(String id, IModel<? extends List<T>> model, SerializableConsumer<ListItem<T>> itemizer) {
		super(id, model);
		setItemizer(itemizer);
		setOutputMarkupId(true);
	}

	@Override
	protected void populateItem(ListItem<T> item) {
		if (itemizer == null) throw new IllegalArgumentException("itemizer not set");
		item.setOutputMarkupId(true);
		itemizer.accept(item);
	}

	public SerializableConsumer<ListItem<T>> getItemizer() {
		return this.itemizer;
	}

	public void setItemizer(SerializableConsumer<ListItem<T>> itemizer) {
		this.itemizer = itemizer;
	}

	public EnhancedListView<T> itemizer(SerializableConsumer<ListItem<T>> _itemizer) {
		setItemizer(_itemizer);
		return this;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		if (visiblePredicate != null) {
			setVisible(visiblePredicate.getAsBoolean());
		}
	}

	public SerializableBooleanSupplier getVisiblePredicate() {
		return this.visiblePredicate;
	}

	public void setVisiblePredicate(SerializableBooleanSupplier visiblePredicate) {
		this.visiblePredicate = visiblePredicate;
	}

	public <M> void setVisiblePredicate(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate0) {
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

	public EnhancedListView<T> visiblePredicate(SerializableBooleanSupplier visiblePredicate0) {
		setVisiblePredicate(visiblePredicate0);
		return this;
	}

	public <M> EnhancedListView<T> visiblePredicate(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate0) {
		setVisiblePredicate(model, visiblePredicate0);
		return this;
	}

	public EnhancedListView<T> visiblePredicate(IModel<String> stringModel) {
		setVisiblePredicate(stringModel);
		return this;
	}

	public EnhancedListView<T> visiblePredicate(ListModel<?> listModel) {
		setVisiblePredicate(listModel);
		return this;
	}

	public EnhancedListView<T> visiblePredicateInvers(IModel<String> stringModel) {
		setVisiblePredicateInvers(stringModel);
		return this;
	}

	public EnhancedListView<T> visiblePredicateInvers(ListModel<?> listModel) {
		setVisiblePredicateInvers(listModel);
		return this;
	}

	public <M> void setVisiblePredicateInvers(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate0) {
		setVisiblePredicate(() -> !visiblePredicate0.apply(model.getObject()));
	}

	public <M> EnhancedListView<T> visiblePredicateInvers(IModel<M> model, SerializableFunction<M, Boolean> visiblePredicate0) {
		setVisiblePredicateInvers(model, visiblePredicate0);
		return this;
	}

	public void setVisibility(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> visible);
	}

	public EnhancedListView<T> visibility(IModel<Boolean> booleanModel) {
		setVisibility(booleanModel);
		return this;
	}

	public void setVisibilityInvers(IModel<Boolean> booleanModel) {
		setVisiblePredicate(booleanModel, visible -> !visible);
	}

	public EnhancedListView<T> visibilityInvers(IModel<Boolean> booleanModel) {
		setVisibilityInvers(booleanModel);
		return this;
	}
}
