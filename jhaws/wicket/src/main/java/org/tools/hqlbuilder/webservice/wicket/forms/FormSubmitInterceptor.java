package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;

public interface FormSubmitInterceptor {
    public void onBeforeSubmit();

    public void onAfterSubmit(Serializable submitReturnValue);
}