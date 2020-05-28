package org.jhaws.common.web.wicket.forms.bootstrap;

import org.apache.wicket.markup.html.panel.Panel;
import org.jhaws.common.web.wicket.forms.common.FormConstants;

@SuppressWarnings("serial")
public class EmptyFormPanel extends Panel implements FormConstants {
    public EmptyFormPanel() {
        super(FORM_ELEMENT);
    }
}
