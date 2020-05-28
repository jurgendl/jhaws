package org.jhaws.common.web.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.forms.common.MultiSelectSettings;
import org.jhaws.common.web.wicket.renderer.DefaultOptionRenderer;

@SuppressWarnings("serial")
public class MultiSelectPanel<T extends Serializable> extends DefaultFormRowPanel<List<T>, Select<List<T>>, MultiSelectSettings> {
    public static final String OPTIONS_CONTAINER_ID = "optionsContainer";

    public static final String OPTGROUP_ID = "optgroup";

    public static final String OPTION_ID = "option";

    protected IModel<? extends List<? extends T>>[] choices;

    protected IModel<String>[] groupLabels;

    protected IOptionRenderer<T> renderer;

    @SuppressWarnings("unchecked")
    public MultiSelectPanel(IModel<?> model, List<T> propertyPath, FormSettings formSettings, MultiSelectSettings componentSettings,
            IOptionRenderer<T> renderer, IModel<? extends List<? extends T>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = new IModel[] { choices };
        this.renderer = fallback(renderer);
    }

    public MultiSelectPanel(IModel<?> model, List<T> propertyPath, FormSettings formSettings, MultiSelectSettings componentSettings,
            IOptionRenderer<T> renderer, IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = fallback(renderer);
        this.groupLabels = groupLabels;
    }

    protected IOptionRenderer<T> fallback(IOptionRenderer<T> r) {
        if (r == null) {
            r = new DefaultOptionRenderer<>();
        }
        return r;
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        //
    }

    @Override
    protected Select<List<T>> createComponent(IModel<List<T>> model, Class<List<T>> valueType) {
        Select<List<T>> choice = new Select<List<T>>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                WebHelper.tag(tag, "multiple", "yes");
            }
        };

        RepeatingView optgroupRepeater = new RepeatingView(OPTGROUP_ID);
        WebHelper.show(optgroupRepeater);
        choice.add(optgroupRepeater);

        if (isNullValid()) {
            WebMarkupContainer optgroupWebcontainer = new WebMarkupContainer(optgroupRepeater.newChildId());
            optgroupWebcontainer.setRenderBodyOnly(true);
            optgroupRepeater.add(optgroupWebcontainer);
            SelectOptions<T> options = createSelectOptions(OPTIONS_CONTAINER_ID, new ListModel<>(Collections.singletonList((T) null)));
            optgroupWebcontainer.add(options);
        }

        for (int i = 0; i < choices.length; i++) {
            final int I = i;
            WebMarkupContainer optgroupWebcontainer = new WebMarkupContainer(optgroupRepeater.newChildId()) {
                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    if (groupLabels != null) {
                        tag.put(LABEL, groupLabels[I].getObject());
                    }
                }
            };
            if (groupLabels != null) {
                WebHelper.show(optgroupWebcontainer);
            } else {
                optgroupWebcontainer.setRenderBodyOnly(true);
            }
            optgroupRepeater.add(optgroupWebcontainer);
            SelectOptions<T> options = createSelectOptions(OPTIONS_CONTAINER_ID, this.choices[i]);
            optgroupWebcontainer.add(options);
        }

        return choice;
    }

    protected SelectOptions<T> createSelectOptions(String id, IModel<? extends List<? extends T>> choicesModel) {
        SelectOptions<T> options = new SelectOptions<T>(id, choicesModel, renderer) {
            @Override
            protected SelectOption<T> newOption(final String text, final IModel<? extends T> optModel) {
                final String textF = StringUtils.isBlank(text) ? "..." : text;
                SelectOption<T> selectOption = createSelectOption(text, optModel, textF);
                return selectOption;
            }
        };
        return options;
    }

    protected SelectOption<T> createSelectOption(final String text, final IModel<? extends T> optModel, final String textF) {
        SelectOption<T> selectOption = new SelectOption<T>(OPTION_ID, optModel) {
            @Override
            public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
                replaceComponentTagBody(markupStream, openTag, textF);
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.setType(TagType.OPEN);
                if (StringUtils.isNotBlank(text)) {
                    tag.put(TITLE, textF);
                }
            }

            @Override
            public String getValue() {
                if (getComponentSettings().isInheritValue()) {
                    return String.valueOf(getModelObject());
                }
                return super.getValue();
            }
        };
        return selectOption;
    }

    protected boolean isNullValid() {
        return false;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!isEnabledInHierarchy()) {
            return;
        }
        // response.render(CssHeaderItem.forReference(MultiSelect.CSS));
        // response.render(JavaScriptHeaderItem.forReference(MultiSelect.JS));
        // response.render(MultiSelect.JS_FACTORY);
    }
}
