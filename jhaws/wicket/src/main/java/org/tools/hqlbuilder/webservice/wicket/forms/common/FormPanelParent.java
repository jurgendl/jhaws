package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.FormConstants;
import org.tools.hqlbuilder.webservice.wicket.forms.FormPanel;

/**
 * @see http://jqueryui.com/button/
 * @see http://wicket.apache.org/guide/guide/forms2.html#forms2_1
 */
@SuppressWarnings("serial")
public class FormPanelParent<T extends Serializable> extends Panel implements FormConstants {
    protected RepeatingView rowRepeater;

    protected RepeatingView componentRepeater;

    protected FormSettings formSettings;

    protected FormActions<T> formActions;

    protected Form<T> form;

    protected int count = 0;

    public FormPanelParent(String id, FormActions<T> formActions) {
        this(id, formActions, null);
    }

    public FormPanelParent(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id);
        WebHelper.show(this);
        setFormActions(formActions != null ? formActions : new DefaultFormActions<T>() {
            private static final long serialVersionUID = -6135914559717102175L;

            @Override
            public Class<T> forObjectClass() {
                try {
                    return WebHelper.<T> getImplementation(FormPanelParent.this, FormPanel.class);
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("implement FormActions#forObjectClass or set generic type of FormActions<T>");
                }
            }

            @Override
            public String toString() {
                return "DefaultFormActions";
            }
        });
        setFormSettings(formSettings == null ? new FormSettings() : formSettings);
    }

    public void setFormActions(FormActions<T> formActions) {
        if (formActions == null) {
            throw new RuntimeException("FormActions required");
        }
        this.formActions = formActions;
    }

    public void setFormSettings(FormSettings formSettings) {
        if (formSettings == null) {
            throw new RuntimeException("FormSettings required");
        }
        this.formSettings = formSettings;
    }

    protected FormActions<T> getFormActions() {
        if (this.formActions == null) {
            throw new RuntimeException("FormActions required");
        }
        return this.formActions;
    }

    public T proxy() {
        return WebHelper.proxy(getFormActions().forObjectClass());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
    }
}
