package org.tools.hqlbuilder.webservice.wicket.forms.common;

import java.io.Serializable;

public interface FormSubmitInterceptor {
	public void onBeforeSubmit();

	public void onAfterSubmit(Serializable submitReturnValue);
}