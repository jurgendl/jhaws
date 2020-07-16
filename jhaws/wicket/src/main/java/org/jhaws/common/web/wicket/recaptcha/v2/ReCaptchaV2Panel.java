package org.jhaws.common.web.wicket.recaptcha.v2;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jhaws.common.web.wicket.recaptcha.ReCaptcha;
import org.jhaws.common.web.wicket.recaptcha.ReCaptchaPanel;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!! UNIEK PER PAGINA !!!!!!!!!!!!!!!!!!!!!!!!!!<br>
 * <br>
 * config:<br>
 * -------<br>
 * <br>
 * ReCaptchaV2Panel.ReCaptchaV2 bean<br>
 * zet sitekey en secret dmv Spring (OPGELET ReCaptchaV2Panel.visible=true/false
 * hebben andere sitekey/secret)<br>
 * url zou al ingesteld moeten zijn<br>
 * visible: standaard true (toon recaptcha checkbox)<br>
 * <br>
 * in html:<br>
 * --------<br>
 * span wicket:id="recaptcha"<br>
 * <br>
 * [A] in code non-ajax:<br>
 * ---------------------<br>
 * <br>
 * ReCaptchaPanel recaptcha = new ReCaptchaV2Panel("recaptcha");<br>
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
 * ---------------------<br>
 * <br>
 * ReCaptchaPanel recaptcha = new ReCaptchaV2Panel("recaptcha");<br>
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
 * protected void updateAjaxAttributes(final AjaxRequestAttributes attributes)
 * {<br>
 * attributes.getAjaxCallListeners().add(recaptcha.getReCaptcha().getAjaxSubmitAjaxCallListener());<br>
 * }<br>
 * });<br>
 * <br>
 * 
 * @see https://stackoverflow.com/questions/9908556/can-i-use-re-captcha-with-wicket
 * @see https://developers.google.com/recaptcha
 * @see https://developers.google.com/recaptcha/docs/versions
 */
@SuppressWarnings("serial")
public class ReCaptchaV2Panel extends ReCaptchaPanel {
	@SpringBean(name = "reCaptchaV2")
	protected ReCaptchaV2 reCaptchaV2;

	public ReCaptchaV2Panel(String id) {
		super(id);
		add(new WebMarkupContainer("recaptcha_js") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("src",
						ReCaptchaV2.API + ReCaptchaV2.LANG + WebSession.get().getLocale().getLanguage().toLowerCase());
			}
		});
		add(new WebMarkupContainer("recaptchatag") {
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.getAttributes().put("data-sitekey", reCaptchaV2.getVisible() ? reCaptchaV2.getVisible_sitekey()
						: reCaptchaV2.getInvisible_sitekey());
				if (!Boolean.FALSE.equals(reCaptchaV2.getVisible())) {
					//
				} else {
					tag.getAttributes().put("data-size", "invisible"); // zet
																		// invisible
																		// ( =
																		// geen
																		// checkbox
																		// )
					tag.getAttributes().put("data-callback", ReCaptchaV2.RECAPTCHA_CALLBACK); // welke
																								// functie
																								// moet
																								// uitgevoerd
																								// worden
				}
			}
		});
	}

	@Override
	public boolean verify() {
		HttpServletRequest httpServletRequest = (HttpServletRequest) getRequest().getContainerRequest();
		boolean isValidRecaptcha = reCaptchaV2.verify(httpServletRequest);
		return isValidRecaptcha;
	}

	public ReCaptchaV2 getReCaptchaV2() {
		return this.reCaptchaV2;
	}

	public void setReCaptchaV2(ReCaptchaV2 reCaptchaV2) {
		this.reCaptchaV2 = reCaptchaV2;
	}

	@Override
	public ReCaptcha getReCaptcha() {
		return this.reCaptchaV2;
	}
}
