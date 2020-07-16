package org.jhaws.common.web.wicket.recaptcha.v3;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jhaws.common.web.wicket.recaptcha.ReCaptcha;
import org.jhaws.common.web.wicket.recaptcha.ReCaptchaPanel;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!! UNIEK PER PAGINA !!!!!!!!!!!!!!!!!!!!!!!!!!<br>
 * * <br>
 * in html:<br>
 * --------<br>
 * <br>
 * span wicket:id="recaptcha"<br>
 * <br>
 * [A] in code non-ajax:<br>
 * ---------------------<br>
 * <br>
 * ReCaptchaPanel recaptcha = new ReCaptchaV3Panel("recaptcha");<br>
 * <br>
 * in form.onsubmit<br>
 * if (recaptcha.isVisible()) {<br>
 * if (!recaptcha.verify()) {<br>
 * error(new
 * ResourceModel("recaptcha.failed").wrapOnAssignment(ContactPage.this).getObject());<br>
 * return;<br>
 * }<br>
 * }<br>
 * <br>
 * form.add(recaptcha);<br>
 * form.add(recaptcha.getReCaptcha().nonAjaxReCaptchaSubmit(new
 * SubmitLink("submitform", form)));<br>
 * <br>
 * [B] in code ajax:<br>
 * -----------------<br>
 * <br>
 * ReCaptchaPanel recaptcha = new ReCaptchaV3Panel("recaptcha");<br>
 * <br>
 * in form.onsubmit<br>
 * if (recaptcha.isVisible()) {<br>
 * if (!recaptcha.verify()) {<br>
 * error(new
 * ResourceModel("recaptcha.failed").wrapOnAssignment(ContactPage.this).getObject());<br>
 * return;<br>
 * }<br>
 * }<br>
 * <br>
 * form.add(recaptcha);<br>
 * form.add(new AjaxSubmitLink("submitform", form) {<br>
 * protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {<br>
 * attributes.getAjaxCallListeners().add(recaptcha.getReCaptcha().getAjaxSubmitAjaxCallListener());<br>
 * }<br>
 * });<br>
 *
 * @see http://apache-wicket.1842946.n4.nabble.com/Google-reCAPTCHA-V3-wicket-component-td4683360.html#a4683567
 * @see https://www.google.com/recaptcha/intro/v3.html
 */
@SuppressWarnings("serial")
public class ReCaptchaV3Panel extends ReCaptchaPanel {
	@SpringBean(name = "reCaptchaV3")
	protected ReCaptchaV3 reCaptchaV3;

	public ReCaptchaV3Panel(String id) {
		super(id);
		add(new WebMarkupContainer("recaptcha_js") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("src", ReCaptchaV3.API + ReCaptchaV3.RENDER + reCaptchaV3.getSitekey());
			}
		});
		add(new WebMarkupContainer("recaptchatag"));
	}

	@Override
	public boolean verify() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) getRequest().getContainerRequest();
		boolean isValidRecaptcha = reCaptchaV3.verify(httpServletRequest);
		return isValidRecaptcha;
	}

	public ReCaptchaV3 getReCaptchaV3() {
		return this.reCaptchaV3;
	}

	public void setReCaptchaV3(ReCaptchaV3 reCaptchaV3) {
		this.reCaptchaV3 = reCaptchaV3;
	}

	@Override
	public ReCaptcha getReCaptcha() {
		return this.reCaptchaV3;
	}
}
