package org.jhaws.common.web.wicket.recaptcha;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface ReCaptcha extends Serializable {
	public static final String G_RECAPTCHA_RESPONSE = "g-recaptcha-response";

	static final ObjectMapper OBJECT_MAPPER = new ReCaptchaObjectMapper();

	SubmitLink adjustNonAjaxReCaptchaSubmit(SubmitLink submitLink);

	AttributeModifier getNonAjaxSubmitAttributeModifier();

	AjaxCallListener getAjaxSubmitAjaxCallListener();

	boolean verify(Form<?> form);

	boolean verify(HttpServletRequest httpServletRequest);

	String getSitekey();

	boolean validateCaptcha(String recaptchaUserResponse, String remoteip);

	String getVerifyUrl();
}
