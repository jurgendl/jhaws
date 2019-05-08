package org.tools.hqlbuilder.webservice.wicket.forms.bootstrap;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.form.select.IOptionRenderer;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.markup.html.form.select.SelectOptions;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;
import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractSelectSettings;
import org.tools.hqlbuilder.webservice.wicket.forms.common.FormSettings;
import org.tools.hqlbuilder.webservice.wicket.renderer.DefaultOptionRenderer;

@SuppressWarnings("serial")
public abstract class SelectPanel<T extends Serializable, C extends Select<T>, S extends AbstractSelectSettings<S>>
        extends DefaultFormRowPanel<T, C, S> {
    public static final String OPTIONS_CONTAINER_ID = "optionsContainer";

    public static final String OPTGROUP_ID = "optgroup";

    public static final String OPTION_ID = "option";

    protected IModel<? extends List<? extends T>>[] choices;

    protected IModel<String>[] groupLabels;

    protected IOptionRenderer<T> renderer;

    @SuppressWarnings("unchecked")
    public SelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>> choices) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = new IModel[] { choices };
        this.renderer = fallback(renderer);
    }

    public SelectPanel(IModel<?> model, T propertyPath, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(model, propertyPath, formSettings, componentSettings);
        this.choices = choices;
        this.renderer = fallback(renderer);
        this.groupLabels = groupLabels;
    }

    @SuppressWarnings("unchecked")
    public SelectPanel(T propertyPath, IModel<T> valueModel, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>> choices) {
        super(propertyPath, valueModel, formSettings, componentSettings);
        this.choices = new IModel[] { choices };
        this.renderer = fallback(renderer);
    }

    public SelectPanel(T propertyPath, IModel<T> valueModel, FormSettings formSettings, S componentSettings, IOptionRenderer<T> renderer,
            IModel<? extends List<? extends T>>[] choices, IModel<String>[] groupLabels) {
        super(propertyPath, valueModel, formSettings, componentSettings);
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

    @SuppressWarnings("unchecked")
    @Override
    protected C createComponent(IModel<T> model, Class<T> valueType) {
        Select<T> choice = createComponent(model, Arrays.stream(choices).mapToInt(lm -> lm.getObject().size()).sum() + (isNullValid() ? 1 : 0));

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

        return (C) choice;
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

    protected abstract boolean isNullValid();

    protected abstract Select<T> createComponent(IModel<T> model, int size);
}
