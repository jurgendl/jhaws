package org.jhaws.common.web.wicket.forms.bootstrap;

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
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.forms.common.BootstrapSelectSettings;
import org.jhaws.common.web.wicket.forms.common.FormSettings;
import org.jhaws.common.web.wicket.renderer.DefaultOptionRenderer;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class BootstrapSelectPanel<T extends Serializable> extends DefaultFormRowPanel<T, Select<T>, BootstrapSelectSettings> {
    public static final String OPTIONS_CONTAINER_ID = "optionsContainer";

    public static final String OPTGROUP_ID = "optgroup";

    public static final String OPTION_ID = "option";

    protected IModel<? extends List<? extends T>>[] choices;

    protected IModel<String>[] groupLabels;

    protected IOptionRenderer<T> renderer;

    protected IOptionRenderer<T> contentRenderer;

    @SuppressWarnings("unchecked")
    public BootstrapSelectPanel(IModel<?> model, LambdaPath<?, T> propertyPath, FormSettings formSettings, BootstrapSelectSettings componentSettings, IOptionRenderer<T> renderer, IOptionRenderer<T> contentRenderer, IModel<? extends List<? extends T>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = new IModel[]{choices};
        this.renderer = fallback(renderer);
        this.contentRenderer = contentRenderer;
    }

    public BootstrapSelectPanel(IModel<?> model, LambdaPath<?, T> propertyPath, FormSettings formSettings, BootstrapSelectSettings componentSettings, IOptionRenderer<T> renderer, IOptionRenderer<T> contentRenderer, IModel<? extends List<? extends T>>[] choices,
                                IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = fallback(renderer);
        this.groupLabels = groupLabels;
        this.contentRenderer = contentRenderer;
    }

    protected IOptionRenderer<T> fallback(IOptionRenderer<T> r) {
        if (r == null) {
            r = new DefaultOptionRenderer<>();
        }
        return r;
    }

    @Override
    protected void onFormComponentTag(ComponentTag tag) {
        super.onFormComponentTag(tag);

        if (getComponentSettings().getSize() != null) {
            WebHelper.tag(tag, "data-size", getComponentSettings().getSize());
        }

        WebHelper.untag(tag, "multiple");
    }

    @Override
    protected void setupPlaceholder(ComponentTag tag) {
        if (StringUtils.isNotBlank(getComponentSettings().getPlaceholder())) {
            WebHelper.tag(tag, TITLE, getComponentSettings().getPlaceholder());
        }
    }

    @Override
    protected void setupReadOnly(ComponentTag tag) {
        if ((this.componentSettings != null) && this.componentSettings.isReadOnly()) {
            WebHelper.tag(tag, DISABLED, DISABLED);
        }
    }

    @Override
    protected Select<T> createComponent(IModel<T> model, Class<T> valueType) {
        Select<T> choice = new Select<T>(VALUE, model) {
            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                onFormComponentTag(tag);
                WebHelper.untag(tag, "multiple");
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
            SelectOptions<T> options = createSelectOptions(OPTIONS_CONTAINER_ID, this.choices[i]

            );
            optgroupWebcontainer.add(options);
        }

        return choice;
    }

    protected SelectOptions<T> createSelectOptions(String id, //
                                                   IModel<? extends List<? extends T>> choicesModel) {
        return new SelectOptions<T>(id, choicesModel, renderer) {
            @Override
            protected SelectOption<T> newOption(String id1, String text, IModel<T> optModel) {
                final String textF = StringUtils.isBlank(text) ? "..." : text;
                String data = null;
                if (contentRenderer != null) {
                    data = contentRenderer.getDisplayValue(optModel.getObject());
                }
                return createSelectOption(text, optModel, textF, data);
            }
        };
    }

    protected SelectOption<T> createSelectOption(final String text, final IModel<T> optModel, final String textF, final String data) {
        SelectOption<T> selectOption = new SelectOption<T>(OPTION_ID, optModel) {
            @Override
            public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag) {
                replaceComponentTagBody(markupStream, openTag, textF);
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.setType(TagType.OPEN);
                if (StringUtils.isNotBlank(data)) {
                    tag.put("data-content", data);
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
        // response.render(CssHeaderItem.forReference(BootstrapSelect.CSS));
        // response.render(JavaScriptHeaderItem.forReference(BootstrapSelect.JS));
    }
}
