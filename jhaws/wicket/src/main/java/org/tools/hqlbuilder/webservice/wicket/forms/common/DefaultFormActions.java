package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.WebHelper;

public abstract class DefaultFormActions<T extends Serializable> implements FormActions<T> {
    private static final long serialVersionUID = 555158530492799693L;

    private static final Logger logger = LoggerFactory.getLogger(DefaultFormActions.class);

    protected IModel<T> formModel;

    public DefaultFormActions() {
        super();
    }

    public DefaultFormActions(T modelObject) {
        this(Model.of(modelObject));
    }

    public DefaultFormActions(IModel<T> model) {
        this.formModel = model;
    }

    public void ajaxRefreshForm(AjaxRequestTarget target, Form<T> form) {
        if (target != null) {
            target.add(form);
        }
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#afterSubmit(org.apache.wicket.ajax.AjaxRequestTarget,
     *      org.apache.wicket.markup.html.form.Form, org.apache.wicket.model.IModel)
     */
    @Override
    public void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        ajaxRefreshForm(target, form);
    }

    /**
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#afterCancel(org.apache.wicket.ajax.AjaxRequestTarget,
     *      org.apache.wicket.markup.html.form.Form, org.apache.wicket.model.IModel)
     */
    @Override
    public void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model) {
        ajaxRefreshForm(target, form);
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#submitModel(org.apache.wicket.model.IModel)
     */
    @Override
    public T submitModel(IModel<T> model) {
        try {
            return submitObject(model.getObject());
        } catch (UnsupportedOperationException ex) {
            logger.error("{}", ex);
            return null;
        }
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#submitObject(java.lang.Object)
     */
    @Override
    public T submitObject(T object) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#loadModel()
     */
    @Override
    public IModel<T> loadModel() {
        if (formModel == null) {
            try {
                formModel = WebHelper.model(loadObject());
            } catch (UnsupportedOperationException ex) {
                formModel = Model.<T> of();
                logger.error("{}", ex);
            }
        }
        return formModel;
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#loadObject()
     */
    @Override
    public T loadObject() {
        if (formModel != null) {
            return formModel.getObject();
        }
        try {
            return WebHelper.create(forObjectClass());
        } catch (org.springframework.beans.BeanInstantiationException ex) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     *
     * @see org.tools.hqlbuilder.webservice.wicket.forms.common.FormActions#forObjectClass()
     */
    @Override
    public Class<T> forObjectClass() {
        return WebHelper.<T> getImplementation(this, DefaultFormActions.class);
    }
}