package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

/**
 * @see http://jqueryui.com/button/
 * @see http://wicket.apache.org/guide/guide/forms2.html#forms2_1
 */
@SuppressWarnings("serial")
public abstract class FormPanelParent<T extends Serializable> extends Panel implements FormConstants {
    protected static final Logger logger = LoggerFactory.getLogger(FormPanelParent.class);

    protected RepeatingView rowRepeater;

    protected RepeatingView componentRepeater;

    protected FormSettings formSettings;

    protected FormActions<T> formActions;

    protected Form<T> form;

    protected int count = 0;

    protected boolean bootstrap;

    public FormPanelParent(String id, FormActions<T> formActions) {
        this(id, formActions, null);
    }

    public FormPanelParent(String id, FormActions<T> formActions, FormSettings formSettings) {
        super(id);
        WebHelper.show(this);
        setFormActions(formActions != null ? formActions : new DefaultFormActions<T>() {
            @Override
            public Class<T> forObjectClass() {
                try {
                    return WebHelper.<T> getImplementation(FormPanelParent.this, FormPanelParent.class);
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
        if (formSettings.isDisableOnClick()) {
            String submitId = getFormActionsContainer().get(FORM_SUBMIT).getMarkupId();
            response.render(OnDomReadyHeaderItem.forScript("$('#" + getForm().getMarkupId() + "').submit(function () { $('#" + submitId
                    + "').attr('disabled', true); $('#" + submitId + "').addClass('disabled'); return true; });"));
        }
    }

    public IModel<T> getFormModel() {
        return this.getForm().getModel();
    }

    public FormSettings getFormSettings() {
        if (this.formSettings == null) {
            throw new RuntimeException("FormSettings required");
        }
        return this.formSettings;
    }

    /**
     * single lazy creation
     */
    protected RepeatingView getRowRepeater() {
        if (rowRepeater == null) {
            RepeatingView repeater = new RepeatingView(FormConstants.FORM_ROW_REPEATER);
            rowRepeater = WebHelper.show(repeater);
        }
        return this.rowRepeater;
    }

    protected String getComponentRepeaterCssClass() {
        return null;
    }

    protected RepeatingView getComponentRepeater() {
        // only create a new row when needed
        if (this.componentRepeater == null) {
            WebMarkupContainer rowContainer = new WebMarkupContainer(getRowRepeater().newChildId());
            String cssClass = getComponentRepeaterCssClass();
            if (StringUtils.isNotBlank(cssClass)) rowContainer.add(new CssClassNameAppender(cssClass));
            getRowRepeater().add(rowContainer);

            RepeatingView repeater = new RepeatingView(FormConstants.FORM_ELEMENT_REPEATER);
            this.componentRepeater = WebHelper.hide(repeater);
            rowContainer.add(this.componentRepeater);
        }
        return this.componentRepeater;
    }

    public void nextRow() {
        this.getForm();
        while (this.count != 0) {
            WebMarkupContainer elementContainer = new WebMarkupContainer(this.getComponentRepeater().newChildId());
            WebHelper.hide(elementContainer);
            this.getComponentRepeater().add(elementContainer);
            elementContainer.add(WebHelper.hide(newEmptyPanel()));
            this.count++;
            if (this.count == this.formSettings.getColumns()) {
                this.count = 0; // reset count
                // so that a new one is created when needed
                this.componentRepeater = null;
            }
        }
    }

    protected abstract MarkupContainer newEmptyPanel();

    @SuppressWarnings("rawtypes")
    protected void onAfterSubmit(final Serializable submitReturnValue) {
        this.getRowRepeater().visitChildren(FormRowPanelParent.class, new IVisitor<FormRowPanelParent, Void>() {
            @Override
            public void component(FormRowPanelParent object, IVisit<Void> visit) {
                if (object instanceof FormSubmitInterceptor) {
                    FormSubmitInterceptor.class.cast(object).onAfterSubmit(submitReturnValue);
                }
            }

            @Override
            public String toString() {
                return "onAfterSubmit:IVisitor<FormRowPanel, Void>";
            }
        });
    }

    @SuppressWarnings("rawtypes")
    protected void onBeforeSubmit() {
        this.getRowRepeater().visitChildren(FormRowPanelParent.class, new IVisitor<FormRowPanelParent, Void>() {
            @Override
            public void component(FormRowPanelParent object, IVisit<Void> visit) {
                if (object instanceof FormSubmitInterceptor) {
                    FormSubmitInterceptor.class.cast(object).onBeforeSubmit();
                }
            }

            @Override
            public String toString() {
                return "onBeforeSubmit:IVisitor<FormRowPanel, Void>";
            }
        });
    }

    /**
     * behind normal form, default invisible, default render body only
     */
    public RepeatingView getFormAdditionalContainer() {
        return (RepeatingView) this.getForm().get(FormConstants.FORM_ADDITIONAL).setVisible(true);
    }

    /**
     * behind normal form buttons, default invisible, default render body only
     */
    public RepeatingView getFormActionsAdditionalContainer() {
        return (RepeatingView) this.getForm().get(FormConstants.FORM_ACTIONS).get(FormConstants.FORM_ACTIONS_ADDTIONAL).setVisible(true);
    }

    /**
     * button container
     */
    public WebMarkupContainer getFormActionsContainer() {
        return (WebMarkupContainer) this.getForm().get(FormConstants.FORM_ACTIONS);
    }

    public Form<T> getForm() {
        if (this.form == null) {
            this.getFormActions(); // check if exists asap
            IModel<T> formModel = new LoadableDetachableModel<T>() {
                @Override
                protected T load() {
                    return getFormActions().loadObject();
                }

                @Override
                public String toString() {
                    return "Form:IModel<T>";
                }
            };
            if (this.getFormSettings().isStateless()) {
                this.form = new StatelessForm<T>(FormConstants.FORM, formModel) {
                    @Override
                    protected String getMethod() {
                        return formSettings.getMethod() != null ? formSettings.getMethod().toString() : super.getMethod();
                    }

                    @Override
                    protected void onSubmit() {
                        onBeforeSubmit();
                        Serializable submitReturnValue = getFormActions().submitModel(getFormModel());
                        onAfterSubmit(submitReturnValue);
                    }

                    @Override
                    public String toString() {
                        return "StatelessForm";
                    }
                };
            } else {
                this.form = new Form<T>(FormConstants.FORM, formModel) {
                    @Override
                    protected String getMethod() {
                        return formSettings.getMethod() != null ? formSettings.getMethod().toString() : super.getMethod();
                    }

                    @Override
                    protected void onSubmit() {
                        onBeforeSubmit();
                        Serializable submitReturnValue = getFormActions().submitModel(getFormModel());
                        onAfterSubmit(submitReturnValue);
                    }

                    @Override
                    public String toString() {
                        return "Form";
                    }
                };
            }

            if (Boolean.FALSE.equals(this.formSettings.getAutocomplete())) {
                this.form.add(new AttributeModifier("autocomplete", "off"));
            } else if (Boolean.TRUE.equals(this.formSettings.getAutocomplete())) {
                this.form.add(new AttributeModifier("autocomplete", "on"));
            }

            if (this.getFormSettings().isInheritId()) {
                this.form.setMarkupId(this.form.getId());
            }

            WebHelper.show(this.form);
            WebMarkupContainer formContainer = new WebMarkupContainer(FormConstants.FORM_CONTAINER);
            if (org.jhaws.common.lang.StringUtils.isNotBlank(formSettings.getFormContainerClass())) {
                formContainer.add(new AttributeModifier("class", formSettings.getFormContainerClass()));
            }
            formContainer.add(form);
            this.add(formContainer);

            WebMarkupContainer formHeader = new WebMarkupContainer(FormConstants.FORM_HEADER) {
                @Override
                public boolean isVisible() {
                    for (int i = 0; i < this.size(); i++) {
                        if (!this.get(i).isVisible()) {
                            return false;
                        }
                    }
                    return super.isVisible();
                }

                @Override
                public String toString() {
                    return "WebMarkupContainer:" + FormConstants.FORM_HEADER;
                }
            };
            this.form.add(formHeader);

            WebMarkupContainer formBody = new WebMarkupContainer(FormConstants.FORM_BODY);
            if (bootstrap) WebHelper.hide(formBody);
            this.form.add(formBody);

            WebMarkupContainer formFieldSet = new WebMarkupContainer(FormConstants.FORM_FIELDSET);
            if (bootstrap) WebHelper.hide(formFieldSet);
            formBody.add(formFieldSet);
            String fieldSetLegend = this.getFormSettings().getFieldSetLegend();
            Label formFieldSetLegend = new Label(FormConstants.FORM_FIELDSET_LEGEND,
                    fieldSetLegend == null ? Model.of("") : new ResourceModel(fieldSetLegend));
            if (bootstrap) WebHelper.hide(formFieldSetLegend);
            formFieldSet.add(formFieldSetLegend.setVisible(fieldSetLegend != null));
            formFieldSet.add(this.getRowRepeater());

            // ResourceModel submitModel = new ResourceModel(FormConstants.SUBMIT_LABEL);
            // ResourceModel resetModel = new ResourceModel(FormConstants.RESET_LABEL);
            // ResourceModel cancelModel = new ResourceModel(FormConstants.CANCEL_LABEL);

            Component submit;
            if (this.getFormSettings().isAjax()) {
                submit = new AjaxSubmitLink(FormConstants.FORM_SUBMIT, this.form) {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                        getFormActions().afterSubmit(target, (Form<T>) f, getFormModel());
                    }

                    @Override
                    public String toString() {
                        return "AjaxSubmitLink:" + FormConstants.FORM_SUBMIT;
                    }
                };
                // submit.setDefaultModel(submitModel);
            } else {
                submit = new Button(FormConstants.FORM_SUBMIT/* , submitModel */);
            }
            submit.setVisible(this.formSettings.isShowSubmit());
            if (getButtonCssClass() != null) submit.add(new CssClassNameAppender(getButtonCssClass()));

            Button reset = new Button(FormConstants.FORM_RESET/* , resetModel */);
            if (getButtonCssClass() != null) reset.add(new CssClassNameAppender(getButtonCssClass()));
            reset.setVisible(this.formSettings.isShowReset());

            // https://cwiki.apache.org/confluence/display/WICKET/Multiple+submit+buttons
            Component cancel;
            if (this.getFormSettings().isAjax()) {
                cancel = new AjaxSubmitLink(FormConstants.FORM_CANCEL, this.form) {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void onAfterSubmit(AjaxRequestTarget target, Form<?> f) {
                        getFormActions().afterCancel(target, (Form<T>) f, getFormModel());
                    }

                    @Override
                    public String toString() {
                        return "AjaxSubmitLink:" + FormConstants.FORM_CANCEL;
                    }
                };
                // cancel.setDefaultModel(cancelModel);
                ((AjaxSubmitLink) cancel).setDefaultFormProcessing(false);
            } else {
                cancel = new Button(FormConstants.FORM_CANCEL/* , cancelModel */);
                ((Button) cancel).setDefaultFormProcessing(false);
            }
            if (getButtonCssClass() != null) cancel.add(new CssClassNameAppender(getButtonCssClass()));
            cancel.setVisible(this.getFormSettings().isCancelable());

            if (this.getFormSettings().isInheritId()) {
                submit.setMarkupId(this.getId() + "." + FormConstants.FORM_SUBMIT);
                reset.setMarkupId(this.getId() + "." + FormConstants.FORM_RESET);
                cancel.setMarkupId(this.getId() + "." + FormConstants.FORM_CANCEL);
            }

            WebMarkupContainer formActionsContainer = new WebMarkupContainer(FormConstants.FORM_ACTIONS);
            this.form.add(formActionsContainer);
            formActionsContainer.add(submit);
            formActionsContainer.add(reset);
            formActionsContainer.add(cancel);
            formActionsContainer
                    .setVisible(this.formSettings.isShowReset() || this.formSettings.isShowSubmit() || this.getFormSettings().isCancelable());

            formActionsContainer.add(new RepeatingView(FormConstants.FORM_ACTIONS_ADDTIONAL).setRenderBodyOnly(true).setVisible(false));

            WebMarkupContainer formFooter = new WebMarkupContainer(FormConstants.FORM_FOOTER) {
                @Override
                public boolean isVisible() {
                    for (int i = 0; i < this.size(); i++) {
                        if (!this.get(i).isVisible()) {
                            return false;
                        }
                    }
                    return super.isVisible();
                }

                @Override
                public String toString() {
                    return "WebMarkupContainer:" + FormConstants.FORM_FOOTER;
                }
            };
            if (bootstrap) WebHelper.hide(formFooter);
            this.form.add(formFooter);

            switch (getFormSettings().getShowMessages()) {
                case bottom:
                    formHeader.add(new WebMarkupContainer("allMessagesTop").setVisible(false));
                    formFooter.add(new FeedbackPanel("allMessagesBottom").setEscapeModelStrings(false));
                    break;
                case top:
                    formHeader.add(new FeedbackPanel("allMessagesTop").setEscapeModelStrings(false));
                    formFooter.add(new WebMarkupContainer("allMessagesBottom").setVisible(false));
                    break;
                default:
                    formHeader.add(new WebMarkupContainer("allMessagesTop").setVisible(false));
                    formFooter.add(new WebMarkupContainer("allMessagesBottom").setVisible(false));
                    break;
            }
            this.form.add(new RepeatingView(FormConstants.FORM_ADDITIONAL).setRenderBodyOnly(true).setVisible(false));
        }
        return this.form;
    }

    protected String getButtonCssClass() {
        return null;
    }

    protected <PropertyType, ModelType, ComponentType extends FormComponent<ModelType>, ElementSettings extends AbstractFormElementSettings<ElementSettings>, RowPanel extends FormRowPanelParent<PropertyType, ModelType, ComponentType, ElementSettings>> RowPanel addRow(
            RowPanel rowpanel) {
        getForm();

        WebMarkupContainer elementContainer = WebHelper.hide(new WebMarkupContainer(this.getComponentRepeater().newChildId()));
        getComponentRepeater().add(elementContainer);

        // rowpanel is already created
        elementContainer.add(rowpanel);

        // components are created in "rowpanel.addComponents"
        rowpanel.addComponents();

        // some post creation stuff
        rowpanel.afterAddComponents();

        if (rowpanel.takesUpSpace()) {
            this.count++;
            if (this.count == this.formSettings.getColumns()) {
                this.count = 0; // reset count
                // so that a new one is created when needed
                this.componentRepeater = null;
            }
        }

        return rowpanel;
    }
}
