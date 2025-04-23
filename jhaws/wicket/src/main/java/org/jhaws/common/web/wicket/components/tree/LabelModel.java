package org.jhaws.common.web.wicket.components.tree;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.WebHelper;

public class LabelModel implements IModel<String> {

    private static final long serialVersionUID = -2309269739547743819L;

    protected final Component component;

    protected final String key;

    public LabelModel(Component component, LambdaPath<?, ?> key) {
        this.component = component;
        this.key = WebHelper.name(key);
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
