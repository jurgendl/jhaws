package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

@SuppressWarnings("serial")
public class EnhancedPanel extends Panel {
    protected BooleanSupplier visiblePredicate;

    public EnhancedPanel(String id, IModel<?> model) {
        super(id, model);
    }

    public EnhancedPanel(String id) {
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

    public <P extends BooleanSupplier & Serializable> EnhancedPanel visiblePredicate(P _visiblePredicate) {
        setVisiblePredicate(_visiblePredicate);
        return this;
    }

    public <M, P extends Function<M, Boolean> & Serializable> EnhancedPanel visiblePredicate(IModel<M> model, P _visiblePredicate) {
        setVisiblePredicate(model, _visiblePredicate);
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

    public <M, P extends Function<M, Boolean> & Serializable> void setVisiblePredicateInvers(IModel<M> model, P visiblePredicate) {
        setVisiblePredicate(() -> !visiblePredicate.apply(model.getObject()));
    }

    public <M, P extends Function<M, Boolean> & Serializable> EnhancedPanel visiblePredicateInvers(IModel<M> model, P visiblePredicate0) {
        setVisiblePredicateInvers(model, visiblePredicate0);
        return this;
    }
}
