package org.jhaws.common.web.wicket.forms.common;

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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.jhaws.common.web.wicket.WebHelper;
import org.jhaws.common.web.wicket.bootstrap.BootstrapFencedFeedbackPanel;
import org.jhaws.common.web.wicket.spin.Spin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
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
                    return WebHelper.<T>getImplementation(FormPanelParent.this, FormPanelParent.class);
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

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (!this.isEnabledInHierarchy()) {
            return;
        }
        if (formSettings.isDisableOnClick()) {
            String submitId = getFormActionsContainer().get(FORM_SUBMIT).getMarkupId();
            String javaScript = "$('#" + getForm().getMarkupId() + "').submit(function(){"//
                    + "$('#" + submitId + "').attr('disabled', true);" //
                    + "$('#" + submitId + "').addClass('disabled');"//
                    + "$('#" + submitId + "').children('.fa-check')"//
                    + ".removeClass('fa-check')"//
                    + ".addClass('fa-compass fa-spin')"//
                    + ";"//
                    + (getFormSettings().isSpinner() ? Spin.SHOW : "")//
                    + "return true;"//
                    + "});";
            response.render(OnDomReadyHeaderItem.forScript(javaScript));
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

    /**
     * defaults: form-group form-row mb-1
     */
    protected String getComponentRepeaterCssClass() {
        return "form-group form-row mb-1 mt-1";
    }

    /**
     * defaults: "container-fluid"
     */
    protected String getFormContainerCssClass() {
        return "container-fluid";
    }

    /**
     * defaults: "form-group form-row mb-1 mt-1"
     */
    protected String getActionsCssClass() {
        return "form-group form-row mb-1 mt-1";
    }

    /**
     * defaults: "form-group form-row mb-1 mt-1"
     */
    protected String getFeedbackCssClass() {
        return "form-group form-row mb-1 mt-1";
    }

    /**
     * defaults: "btn btn-primary btn-sm mr-1"
     */
    protected String getSubmitButtonCssClass() {
        return "btn btn-primary btn-sm mr-1";
    }

    /**
     * defaults: "btn btn-secondary btn-sm mr-1"
     */
    protected String getResetButtonCssClass() {
        return "btn btn-secondary btn-sm mr-1";
    }

    /**
     * defaults: "btn btn-secondary btn-sm mr-1 "
     */
    protected String getCancelButtonCssClass() {
        return "btn btn-secondary btn-sm mr-1";
    }

    protected RepeatingView getComponentRepeater() {
        // only create a new row when needed
        if (this.componentRepeater == null) {
            WebMarkupContainer rowContainer = new WebMarkupContainer(getRowRepeater().newChildId());
            rowContainer.add(AttributeModifier.replace("class", getComponentRepeaterCssClass()));
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

    public static class InnerFormModel<X extends Serializable> extends LoadableDetachableModel<X> {
        protected FormActions<X> formActions;

        public InnerFormModel(FormActions<X> formActions) {
            this.formActions = formActions;
        }

        @Override
        protected X load() {
            return formActions.loadObject();
        }

        @Override
        public String toString() {
            return "Form:IModel<X>";
        }
    }

    public Form<T> getForm() {
        if (this.form == null) {
            this.getFormActions(); // check if exists asap
            IModel<T> formModel = new InnerFormModel<>(getFormActions());
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
                this.form.add(AttributeModifier.replace("autocomplete", "off"));
            } else if (Boolean.TRUE.equals(this.formSettings.getAutocomplete())) {
                this.form.add(AttributeModifier.replace("autocomplete", "on"));
            }

            if (this.getFormSettings().isInheritId()) {
                this.form.setMarkupId(this.form.getId());
            }

            WebHelper.show(this.form);
            WebMarkupContainer formContainer = new WebMarkupContainer(FormConstants.FORM_CONTAINER);
            formContainer.add(AttributeModifier.replace("class", getFormContainerCssClass()));
            formContainer.add(form);
            this.add(formContainer);

            WebMarkupContainer formHeader = new WebMarkupContainer(FormConstants.FORM_HEADER) {
                @Override
                public boolean isVisible() {
                    for (int i = 0; i < this.size(); i++) {
                        if (!this.stream().skip(i).findFirst().get().isVisible()) {
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
            WebHelper.hide(formBody); // TODO
            this.form.add(formBody);

            WebMarkupContainer formFieldSet = new WebMarkupContainer(FormConstants.FORM_FIELDSET);
            WebHelper.hide(formFieldSet); // TODO
            formBody.add(formFieldSet);
            String fieldSetLegend = this.getFormSettings().getFieldSetLegend();
            Label formFieldSetLegend = new Label(FormConstants.FORM_FIELDSET_LEGEND, fieldSetLegend == null ? Model.of("") : new ResourceModel(fieldSetLegend));
            WebHelper.hide(formFieldSetLegend); // TODO
            formFieldSet.add(formFieldSetLegend.setVisible(fieldSetLegend != null));
            formFieldSet.add(this.getRowRepeater());

            // ResourceModel submitModel = new
            // ResourceModel(FormConstants.SUBMIT_LABEL);
            // ResourceModel resetModel = new
            // ResourceModel(FormConstants.RESET_LABEL);
            // ResourceModel cancelModel = new
            // ResourceModel(FormConstants.CANCEL_LABEL);

            WebMarkupContainer submit;
            if (this.getFormSettings().isAjax()) {
                submit = createAjaxSubmit();
                // submit.setDefaultModel(submitModel);
            } else {
                submit = createSubmit();
            }
            submit.setVisible(this.formSettings.isShowSubmit());
            submit.add(createSubmitLabel(SUBMIT_LABEL));
            submit.add(AttributeModifier.replace("class", getSubmitButtonCssClass()));

            Button reset = new Button(FormConstants.FORM_RESET/* , resetModel */);
            reset.add(AttributeModifier.replace("class", getResetButtonCssClass()));
            reset.add(createResetLabel(RESET_LABEL));
            reset.setVisible(this.formSettings.isShowReset());

            // https://cwiki.apache.org/confluence/display/WICKET/Multiple+submit+buttons
            WebMarkupContainer cancel;
            if (this.getFormSettings().isAjax()) {
                cancel = new AjaxSubmitLink(FormConstants.FORM_CANCEL, this.form) {
                    @Override
                    protected void onAfterSubmit(AjaxRequestTarget target) {
                        getFormActions().afterCancel(target, form, getFormModel());
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
            cancel.add(AttributeModifier.replace("class", getCancelButtonCssClass()));
            cancel.setVisible(this.getFormSettings().isCancelable());
            cancel.add(createCancelLabel(CANCEL_LABEL));

            if (this.getFormSettings().isInheritId()) {
                submit.setMarkupId(this.getId() + "." + FormConstants.FORM_SUBMIT);
                reset.setMarkupId(this.getId() + "." + FormConstants.FORM_RESET);
                cancel.setMarkupId(this.getId() + "." + FormConstants.FORM_CANCEL);
            }

            WebMarkupContainer formActionsContainer = new WebMarkupContainer(FormConstants.FORM_ACTIONS);
            formActionsContainer.add(AttributeModifier.replace("class", getActionsCssClass()));
            this.form.add(formActionsContainer);
            formActionsContainer.add(submit);
            formActionsContainer.add(reset);
            formActionsContainer.add(cancel);
            formActionsContainer.setVisible(this.formSettings.isShowReset() || this.formSettings.isShowSubmit() || this.getFormSettings().isCancelable());

            formActionsContainer.add(new RepeatingView(FormConstants.FORM_ACTIONS_ADDTIONAL).setRenderBodyOnly(true).setVisible(false));

            WebMarkupContainer formFooter = new WebMarkupContainer(FormConstants.FORM_FOOTER) {
                @Override
                public boolean isVisible() {
                    for (int i = 0; i < this.size(); i++) {
                        if (!this.stream().skip(i).findFirst().get().isVisible()) {
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
            WebHelper.hide(formFooter); // TODO
            this.form.add(formFooter);

            switch (getFormSettings().getShowMessages()) {
                case bottom:
                    formHeader.add(new WebMarkupContainer("allMessagesTop").setVisible(false));
                    formFooter.add(newFeedbackPanel("allMessagesBottom").add(AttributeModifier.replace("class", getFeedbackCssClass())));
                    break;
                case top:
                    formHeader.add(newFeedbackPanel("allMessagesTop").add(AttributeModifier.replace("class", getFeedbackCssClass())));
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

    protected Component createSubmitLabel(String id) {
        return new Label(id, new ResourceModel(id));
    }

    protected Component createResetLabel(String id) {
        return new Label(id, new ResourceModel(id));
    }

    protected Component createCancelLabel(String id) {
        return new Label(id, new ResourceModel(id));
    }

    protected WebMarkupContainer createSubmit() {
        return new Button(FormConstants.FORM_SUBMIT/* , submitModel */) {
            @Override
            public String toString() {
                return "SubmitLink:" + FormConstants.FORM_SUBMIT;
            }
        };
    }

    protected WebMarkupContainer createAjaxSubmit() {
        return new AjaxSubmitLink(FormConstants.FORM_SUBMIT, this.form) {
            @Override
            protected void onAfterSubmit(AjaxRequestTarget target) {
                getFormActions().afterSubmit(target, form, getFormModel());
                if (getFormSettings().isSpinner()) {
                    target.appendJavaScript(Spin.HIDE);
                }
            }

            @Override
            public String toString() {
                return "AjaxSubmitLink:" + FormConstants.FORM_SUBMIT;
            }
        };
    }

    protected org.apache.wicket.markup.html.panel.FeedbackPanel newFeedbackPanel(String id) {
        org.apache.wicket.markup.html.panel.FeedbackPanel feedbackPanel = new BootstrapFencedFeedbackPanel(id, this);
        return feedbackPanel;
    }

    protected <PropertyType//
            , ModelType//
            , ComponentType extends FormComponent<ModelType>//
            , ElementSettings extends AbstractFormElementSettings<ElementSettings>//
            , RowPanel extends FormRowPanelParent<PropertyType, ModelType, ComponentType, ElementSettings>> //
    RowPanel addRow(RowPanel rowpanel) {
        getForm();

        WebMarkupContainer elementContainer = WebHelper.hide(new WebMarkupContainer(this.getComponentRepeater().newChildId()));
        getComponentRepeater().add(elementContainer);

        // rowpanel is already created
        elementContainer.add(rowpanel);

        // components are created in "rowpanel.addComponents"
        rowpanel.addComponents(rowpanel.getComponentSettings());

        // some post creation stuff
        rowpanel.afterAddComponents();

        if (rowpanel.takesUpSpace()) {
            this.count++;
            if (this.formSettings.getColumns() != null && this.count == this.formSettings.getColumns()) {
                newRow();
            }
        }

        return rowpanel;
    }

    public void newRow() {
        this.count = 0; // reset count
        // so that a new one is created when needed
        this.componentRepeater = null;
    }

    public void feedbackError(Serializable message) {
        getForm().error(message);
    }

    public void feedbackInfo(Serializable message) {
        getForm().info(message);
    }

    public void feedbackFatal(Serializable message) {
        getForm().fatal(message);
    }

    public void feedbackWarn(Serializable message) {
        getForm().warn(message);
    }

    public void feedbackSuccess(Serializable message) {
        getForm().success(message);
    }
}
