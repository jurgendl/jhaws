package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;

import org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormPanelParent;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;

@SuppressWarnings("serial")
public class FormPanel<T extends Serializable> extends FormPanelParent<T> {
    public FormPanel(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id, formActions, formSettings);
    }
}
