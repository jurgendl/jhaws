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

@SuppressWarnings("serial")
public class EnhancedListView<T> extends ListView<T> {
    protected BooleanSupplier visiblePredicate;

    protected Consumer<ListItem<T>> itemizer;

    public EnhancedListView(String id, ListModel<T> model) {
        super(id, model);
    }

    public EnhancedListView(String id, List<T> list) {
        super(id, list);
    }

    public EnhancedListView(String id) {
        super(id);
    }

    public <I extends Consumer<ListItem<T>> & Serializable> EnhancedListView(String id, ListModel<T> model, I itemizer) {
        super(id, model);
        setItemizer(itemizer);
    }

    public <I extends Consumer<ListItem<T>> & Serializable> EnhancedListView(String id, List<T> list, I itemizer) {
        super(id, list);
        setItemizer(itemizer);
    }

    public <I extends Consumer<ListItem<T>> & Serializable> EnhancedListView(String id, I itemizer) {
        super(id);
        setItemizer(itemizer);
    }

    @Override
    protected void populateItem(ListItem<T> item) {
        itemizer.accept(item);
    }

    public Consumer<ListItem<T>> getItemizer() {
        return this.itemizer;
    }

    public <I extends Consumer<ListItem<T>> & Serializable> void setItemizer(I itemizer) {
        this.itemizer = itemizer;
    }

    public <I extends Consumer<ListItem<T>> & Serializable> EnhancedListView<T> itemizer(I itemizer0) {
        setItemizer(itemizer0);
        return this;
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (visiblePredicate != null) {
            setVisible(visiblePredicate.getAsBoolean());
        }
    }

    public BooleanSupplier getVisiblePredicate() {
        return this.visiblePredicate;
    }

    public <P extends BooleanSupplier & Serializable> void setVisiblePredicate(P visiblePredicate) {
        this.visiblePredicate = visiblePredicate;
    }

    public <M, P extends Function<M, Boolean> & Serializable> void setVisiblePredicate(IModel<M> model, P visiblePredicate) {
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

    public <P extends BooleanSupplier & Serializable> EnhancedListView<T> visiblePredicate(P _visiblePredicate) {
        setVisiblePredicate(_visiblePredicate);
        return this;
    }

    public <M, P extends Function<M, Boolean> & Serializable> EnhancedListView<T> visiblePredicate(IModel<M> model, P _visiblePredicate) {
        setVisiblePredicate(model, _visiblePredicate);
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
        setVisiblePredicate(stringModel);
        return this;
    }

    public EnhancedListView<T> visiblePredicateInvers(ListModel<?> listModel) {
        setVisiblePredicate(listModel);
        return this;
    }
}
