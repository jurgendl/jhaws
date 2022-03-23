package org.jhaws.common.web.wicket.components;

import java.io.Serializable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;

@SuppressWarnings("serial")
public class EnhancedLabel extends Label {
    protected BooleanSupplier visiblePredicate;

    protected BooleanSupplier enabledPredicate;

    public EnhancedLabel(String id, IModel<?> model) {
        super(id, model);
    }

    public EnhancedLabel(String id, Serializable label) {
        super(id, label);
    }

    public EnhancedLabel(String id) {
        super(id);
    }

    public <P extends BooleanSupplier & Serializable> EnhancedLabel(String id, IModel<?> model, P visiblePredicate) {
        super(id, model);
        setVisiblePredicate(visiblePredicate);
    }

    public <P extends BooleanSupplier & Serializable> EnhancedLabel(String id, Serializable label, P visiblePredicate) {
        super(id, label);
        setVisiblePredicate(visiblePredicate);
    }

    public <P extends BooleanSupplier & Serializable> EnhancedLabel(String id, P visiblePredicate) {
        super(id);
        setVisiblePredicate(visiblePredicate);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        if (visiblePredicate != null) {
            setVisible(visiblePredicate.getAsBoolean());
        }
    }

    /**
     * Visibility gedrag zo instellen dat ie onzichtbaar is bij lege string
     */
    public void setHideOnEmpty() {
        setVisiblePredicate((BooleanSupplier & Serializable) () -> StringUtils.isNotBlank(getDefaultModelObjectAsString()));
    }

    /**
     * Hulpje bij contructor voor builder style instantiatie <br/>
     * KomodoLabel titelLabel = new KomodoLabel("titel", titelModel).hideOnEmpty();
     *
     * @return
     */
    public EnhancedLabel hideOnEmpty() {
        setHideOnEmpty();
        return this;
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

    public <P extends BooleanSupplier & Serializable> EnhancedLabel visiblePredicate(P _visiblePredicate) {
        setVisiblePredicate(_visiblePredicate);
        return this;
    }

    public <M, P extends Function<M, Boolean> & Serializable> EnhancedLabel visiblePredicate(IModel<M> model, P _visiblePredicate) {
        setVisiblePredicate(model, _visiblePredicate);
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
        setVisiblePredicate(stringModel);
        return this;
    }

    public EnhancedLabel visiblePredicateInvers(ListModel<?> listModel) {
        setVisiblePredicate(listModel);
        return this;
    }

    public BooleanSupplier getEnabledPredicate() {
        return this.enabledPredicate;
    }

    public <P extends BooleanSupplier & Serializable> void setEnabledPredicate(P _enabledPredicate) {
        this.enabledPredicate = _enabledPredicate;
    }

    public <M, P extends Function<M, Boolean> & Serializable> void setEnabledPredicate(IModel<M> model, P enabledPredicate) {
        setEnabledPredicate(() -> enabledPredicate.apply(model.getObject()));
    }

    public void setEnabledPredicate(IModel<String> stringModel) {
        setEnabledPredicate(stringModel, StringUtils::isNotBlank);
    }

    public void setEnabledPredicate(ListModel<?> listModel) {
        setEnabledPredicate(listModel, list -> list != null && !list.isEmpty());
    }

    public void setEnabledPredicateInvers(IModel<String> stringModel) {
        setEnabledPredicate(stringModel, StringUtils::isBlank);
    }

    public void setEnabledPredicateInvers(ListModel<?> listModel) {
        setEnabledPredicate(listModel, list -> list == null || list.isEmpty());
    }

    public <P extends BooleanSupplier & Serializable> EnhancedLabel enabledPredicate(P _enabledPredicate) {
        setEnabledPredicate(_enabledPredicate);
        return this;
    }

    public <M, P extends Function<M, Boolean> & Serializable> EnhancedLabel enabledPredicate(IModel<M> model, P _enabledPredicate) {
        setEnabledPredicate(model, _enabledPredicate);
        return this;
    }

    public EnhancedLabel enabledPredicate(IModel<String> stringModel) {
        setEnabledPredicate(stringModel);
        return this;
    }

    public EnhancedLabel enabledPredicate(ListModel<?> listModel) {
        setEnabledPredicate(listModel);
        return this;
    }

    public EnhancedLabel enabledPredicateInvers(IModel<String> stringModel) {
        setEnabledPredicate(stringModel);
        return this;
    }

    public EnhancedLabel enabledPredicateInvers(ListModel<?> listModel) {
        setEnabledPredicate(listModel);
        return this;
    }
}
