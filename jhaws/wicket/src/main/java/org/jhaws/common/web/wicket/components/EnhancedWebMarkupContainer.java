package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class EnhancedWebMarkupContainer extends WebMarkupContainer {
    protected BooleanSupplier visiblePredicate;

    public EnhancedWebMarkupContainer(String id, IModel<?> model) {
        super(id, model);
    }

    public EnhancedWebMarkupContainer(String id) {
        super(id);
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
