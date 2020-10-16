package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

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

    @Override
    protected void populateItem(ListItem<T> item) {
        itemizer.accept(item);
    }

    public Consumer<ListItem<T>> getItemizer() {
        return this.itemizer;
    }

    public void setItemizer(Consumer<ListItem<T>> itemizer) {
        this.itemizer = itemizer;
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
}
