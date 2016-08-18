package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.tools.hqlbuilder.webservice.jquery.ui.jqueryuithemes.JQueryUIThemes;
import org.tools.hqlbuilder.webservice.jquery.ui.primeui.PrimeUI;
import org.tools.hqlbuilder.webservice.wicket.WicketSession;

@SuppressWarnings("serial")
public class ThemesPanel extends Panel {
    protected StatelessForm<Object> themeForm;

    protected DropDownChoice<String> changeTheme;

    public ThemesPanel(String id) {
        super(id);

        Model<String> themeModel = new Model<String>();
        String sessionTheme = WicketSession.get().getJQueryUITheme();
        themeModel.setObject(sessionTheme);

        this.changeTheme = new DropDownChoice<String>("themeOptions", themeModel, JQueryUIThemes.getThemes(), new IChoiceRenderer<String>() {
            @Override
            public Object getDisplayValue(String object) {
                return object;
            }

            @Override
            public String getIdValue(String object, int index) {
                return object;
            }
        }) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(String newSelection) {
                WicketSession.get().setJQueryUITheme(newSelection);
            }
        };
        this.changeTheme.setNullValid(false);
        themeForm = new StatelessForm<Object>("themeForm") {
            @Override
            protected void onSubmit() {
                WicketSession.get().setJQueryUITheme(changeTheme.getModelObject());
            }
        };
        themeForm.setMarkupId(themeForm.getId());
        // changeTheme.add(new CssClassNameAppender(PrimeUI.puidropdown));
        add(themeForm.add(changeTheme));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        response.render(JavaScriptHeaderItem.forReference(PrimeUI.PRIME_UI_FACTORY_JS));
    }
}
