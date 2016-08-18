package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

public interface FormActions<T extends Serializable> extends Serializable {
    /**
     * after default form cancelling when ajax=true
     *
     * @param target exists when ajax is enabled on form and on the default submit
     * @param form this form
     * @param model this foem model
     */
    public abstract void afterCancel(AjaxRequestTarget target, Form<T> form, IModel<T> model);

    /**
     * after default form submitting when ajax=true
     *
     * @param target exists when ajax is enabled on form and on the default submit
     * @param form this form
     * @param model this foem model
     */
    public abstract void afterSubmit(AjaxRequestTarget target, Form<T> form, IModel<T> model);

    public abstract Class<T> forObjectClass();

    public abstract IModel<T> loadModel();

    public abstract T loadObject() throws UnsupportedOperationException;

	public abstract T submitModel(IModel<T> model);

	public abstract T submitObject(T object) throws UnsupportedOperationException;
}