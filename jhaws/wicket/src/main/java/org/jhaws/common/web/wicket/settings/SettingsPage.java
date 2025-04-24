package org.jhaws.common.web.wicket.settings;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.WicketApplication;
import org.jhaws.common.web.wicket.bootstrap.DefaultWebPage;
import org.jhaws.common.web.wicket.forms.bootstrap.FormPanel;
import org.jhaws.common.web.wicket.forms.common.CheckBoxSettings;
import org.jhaws.common.web.wicket.forms.common.DefaultFormActions;
import org.jhaws.common.web.wicket.forms.common.DropDownSettings;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.TextFieldSettings;
import org.jhaws.common.web.wicket.renderer.StringOptionRenderer;
import org.jhaws.common.web.wicket.settings.WicketAppSettings.Theme;
import org.jhaws.common.web.wicket.spin.Spin;

import java.util.Arrays;
import java.util.stream.Collectors;

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
    }

    ;

    public SettingsPage(PageParameters parameters) {
        super(parameters);
    }

    public SettingsPage() {
        super(new PageParameters());
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        Label.class.cast(getPage().get("page.title")).setDefaultModelObject("Application Settings");
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
        form.addDropDown(LambdaPath.p(WicketAppSettings::getTheme), new DropDownSettings(), new StringOptionRenderer(), new ListModel<>(Arrays.stream(Theme.values()).map(Theme::id).collect(Collectors.toList())));
        form.addDropDown(LambdaPath.p(WicketAppSettings::getSpinner), new DropDownSettings(), new StringOptionRenderer(), new ListModel<>(Spin.types));
        form.addToggle(LambdaPath.p(WicketAppSettings::getCheckAdsEnabled), new CheckBoxSettings());
        form.addTextField(LambdaPath.p(WicketAppSettings::getCacheDuration), new TextFieldSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getCheckCookiesEnabled), new CheckBoxSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getCheckJavaScriptEnabled), new CheckBoxSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getDiskStore), new CheckBoxSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getGatherBrowserInfo), new CheckBoxSettings());
        form.addTextField(LambdaPath.p(WicketAppSettings::getGoogleSigninClientId), new TextFieldSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getJavascriptAtBottom), new CheckBoxSettings());
        form.addTextField(LambdaPath.p(WicketAppSettings::getShortcutIcon), new TextFieldSettings());
        form.addToggle(LambdaPath.p(WicketAppSettings::getDemo), new CheckBoxSettings());
        this.add(form);
    }
}
