package org.tools.hqlbuilder.webservice.wicket.components;

import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;

public class LocalesPanel extends Panel {
    private static final long serialVersionUID = 645790077354220643L;

    protected StatelessForm<Locale> localeForm;

    protected DropDownChoice<Locale> changeLocale;

    public LocalesPanel(String id) {
        super(id);

        this.changeLocale = new LocalesDropDown("localeOptions", null, null, null) {
            private static final long serialVersionUID = -4402883308787625917L;

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(Locale newSelection) {
                getSession().setLocale(newSelection);
            }
        };
        this.changeLocale.setNullValid(false);
        localeForm = new StatelessForm<Locale>("localeForm") {
            private static final long serialVersionUID = -7717228617856599476L;

            @Override
            protected void onSubmit() {
                getSession().setLocale(changeLocale.getModelObject());
            }
        };
        localeForm.setMarkupId(localeForm.getId());
        add(localeForm.add(changeLocale));
    }
}
