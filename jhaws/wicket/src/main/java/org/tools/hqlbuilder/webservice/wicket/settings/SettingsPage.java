package org.tools.hqlbuilder.webservice.wicket.settings;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tools.hqlbuilder.webservice.jquery.ui.spin.Spin;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;
import org.tools.hqlbuilder.webservice.wicket.bootstrap.DefaultWebPage;
import org.tools.hqlbuilder.webservice.wicket.forms.bootstrap.FormPanel;
import org.tools.hqlbuilder.webservice.wicket.forms.common.CheckBoxSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DefaultFormActions;
import org.tools.hqlbuilder.webservice.wicket.forms.common.DropDownSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.TextFieldSettings;
import org.tools.hqlbuilder.webservice.wicket.renderer.StringOptionRenderer;

@SuppressWarnings("serial")
public class SettingsPage extends DefaultWebPage {
    public static class WebSettingsModel implements IModel<WicketAppSettings> {
        @Override
        public void detach() {
            //
        }

        @Override
        public WicketAppSettings getObject() {
            return WicketApplication.get().getSettings();
        }

        @Override
        public void setObject(WicketAppSettings settings) {
            WicketApplication.get().setSettings(settings);
        }
    };

    public SettingsPage(PageParameters parameters) {
        super(parameters);
    }

    public SettingsPage() {
        super(new PageParameters());
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        Label.class.cast(getPage().get("page.title")).setDefaultModelObject("Web: Settings");
    }

    @Override
    protected void addComponents(PageParameters parameters, MarkupContainer html) {
        IModel<WicketAppSettings> model = new WebSettingsModel();
        FormPanel<WicketAppSettings> form = new FormPanel<>("webSettings", new DefaultFormActions<WicketAppSettings>(model) {
            @Override
            public WicketAppSettings submitObject(WicketAppSettings settings) {
                super.submitObject(settings);
                WicketApplication.get().setSettings(settings);
                return settings;
            }
        }, new FormSettings());
        WicketAppSettings p = form.proxy();
        form.addDropDown(p.getSpinner(), new DropDownSettings(), new StringOptionRenderer(), new ListModel<>(Spin.types));
        form.addToggle(p.getCheckAdsEnabled(), new CheckBoxSettings());
        form.addTextField(p.getCacheDuration(), new TextFieldSettings());
        form.addToggle(p.getCheckCookiesEnabled(), new CheckBoxSettings());
        form.addToggle(p.getCheckJavaScriptEnabled(), new CheckBoxSettings());
        form.addToggle(p.getDiskStore(), new CheckBoxSettings());
        form.addToggle(p.getGatherBrowserInfo(), new CheckBoxSettings());
        form.addTextField(p.getGoogleSigninClientId(), new TextFieldSettings());
        form.addToggle(p.getJavascriptAtBottom(), new CheckBoxSettings());
        form.addTextField(p.getShortcutIcon(), new TextFieldSettings());
        this.add(form);
    }
}
