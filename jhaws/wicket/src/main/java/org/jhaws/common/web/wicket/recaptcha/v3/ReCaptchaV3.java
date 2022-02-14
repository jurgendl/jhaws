package org.jhaws.common.web.wicket.recaptcha.v3;

import static org.apache.http.client.fluent.Form.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.WicketRoot;
import org.jhaws.common.web.wicket.recaptcha.ReCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// <dependency>
// <groupId>org.apache.httpcomponents</groupId>
// <artifactId>fluent-hc</artifactId>
// <version>4.5.12</version>
// </dependency>

/**
 * @see http://apache-wicket.1842946.n4.nabble.com/Google-reCAPTCHA-V3-wicket-component-td4683360.html#a4683567
 * @see https://www.google.com/recaptcha/intro/v3.html
 */
@Component
@SuppressWarnings("serial")
public class ReCaptchaV3 implements ReCaptcha {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReCaptchaV3.class);

    public static final String API = "https://www.google.com/recaptcha/api.js";

    public static final String RENDER = "?render=";

    public static final String VERIFY = "https://www.google.com/recaptcha/api/siteverify";

    /** zet system NoRecaptchaValidation=true om te disabelen, bv bij tests */
    public static final String NO_RECAPTCHA_VALIDATION = "NoRecaptchaValidation";

    public static final String RECAPTCHA_CALLBACK = "reCaptchaCallback";

    public static final String TOKEN_PARAM = "token";

    public static final String TOKEN_JS = "tokenCaptcha";

    /** ingesteld by default */
    @Value("${recaptcha.v3.verifyUrl:" + VERIFY + "}")
    protected String verifyUrl = VERIFY;

    /**
     * onveiligst 0.0-1.0 veiligst, default 0.4, mogelijks is dit zetten op 0.8 mogelijk
     */
    @Value("${recaptcha.v3.score:0.4}")
    protected double captchaScore = 0.4;

    /** in te stellen maar crasht niet wanneer niet ingesteld */
    @Value("${recaptcha.v3.secret:-}")
    protected String secret;

    @Value("${recaptcha.v3.sitekey:-}")
    protected String sitekey;

    /** by default is versie 2 actief */
    @Value("${recaptcha.version:v2}")
    protected String version = "v2";

    public double getCaptchaScore() {
        return this.captchaScore;
    }

    public void setCaptchaScore(double captchaScore) {
        this.captchaScore = captchaScore;
    }

    @Override
    public String getVerifyUrl() {
        return this.verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    @Override
    public String getSitekey() {
        return this.sitekey;
    }

    public void setSitekey(String sitekey) {
        this.sitekey = sitekey;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean verify(Form<?> form) {
        HttpServletRequest servletRequest = (HttpServletRequest) form.getWebRequest().getContainerRequest();
        return verify(servletRequest);
    }

    @Override
    public boolean validateCaptcha(String recaptchaUserResponse, String remoteip) {
        try {
            if (recaptchaUserResponse == null) {
                return false;
            }
            Request post = Request.Post(verifyUrl.toString());
            Content content = post.bodyForm(form()//
                    .add("secret", secret)//
                    .add("response", recaptchaUserResponse)//
                    .add("remoteip", remoteip)//
                    .build())//
                    .execute()//
                    .returnContent();
            ReCaptchaV3Response response = OBJECT_MAPPER.readerFor(ReCaptchaV3Response.class).readValue(content.asStream());
            LOGGER.info("ReCaptcha Response '{}'", response);
            return response != null && response.getSuccess() != null && response.getSuccess() && (response.getScore() > captchaScore);
        } catch (Exception ex) {
            LOGGER.error("", ex);
            return false;
        }
    }

    @Override
    public boolean verify(HttpServletRequest servletRequest) {
        if ("true".equals(System.getProperty(NO_RECAPTCHA_VALIDATION))) {
            return true;
        }
        if (servletRequest == null) {
            return false;
        }
        String remoteAddr = servletRequest.getRemoteAddr();
        IRequestParameters requestParameters = RequestCycle.get().getRequest().getRequestParameters();
        String response = requestParameters.getParameterValue(TOKEN_PARAM).toString();
        if (StringUtils.isBlank(response)) {
            response = requestParameters.getParameterValue(G_RECAPTCHA_RESPONSE).toString();
        }
        if (StringUtils.isBlank(response)) {
            return false;
        }
        LOGGER.info("token: {}", response);
        return validateCaptcha(response, remoteAddr);
    }

    @Override
    public SubmitLink adjustNonAjaxReCaptchaSubmit(String id, IModel<?> model, Form<?> form) {
        // WICKET-6/7
        // SubmitLink submitLink = new SubmitLink(id, model, form);
        // AttributeModifier am = new AttributeModifier("onclick", "") {
        // @Override
        // protected String newValue(String currentValue, String
        // replacementValue) {
        // String js = ";event.preventDefault();"//
        // + "var " + ReCaptchaV3.TOKEN_JS + ";"//
        // + "grecaptcha.ready(function(){"//
        // + "grecaptcha.execute('" + sitekey + "',{action: 'submit'})"//
        // + ".then(function(token){"//
        // + ReCaptchaV3.TOKEN_JS + "=token;"//
        // + "$('#" + ReCaptcha.G_RECAPTCHA_RESPONSE + "').val(token);"//
        // + currentValue + ";"//
        // + "})})";
        // return js;
        // }
        // };
        // submitLink.add(am);

        // WICKET-9
        SubmitLink submitLink = new SubmitLink(id, model, form) {
            @Override
            protected CharSequence getTriggerJavaScript() {
                CharSequence triggerJavaScript = super.getTriggerJavaScript();
                if (triggerJavaScript != null) {
                    triggerJavaScript = ";event.preventDefault();"//
                            + "var " + ReCaptchaV3.TOKEN_JS + ";"//
                            + "grecaptcha.ready(function(){"//
                            + "grecaptcha.execute('" + sitekey + "',{action: 'submit'})"//
                            + ".then(function(token){"//
                            + ReCaptchaV3.TOKEN_JS + "=token;"//
                            + "$('#" + ReCaptcha.G_RECAPTCHA_RESPONSE + "').val(token);"//
                            + triggerJavaScript + ";"//
                            + "})})";
                }
                return triggerJavaScript;
            }
        };

        // submitLink.add(AttributeModifier.append("class", " g-recaptcha"));
        // submitLink.add(new AttributeModifier("data-sitekey",
        // reCaptchaV3.getSitekey()));
        // submitLink.add(new AttributeModifier("data-action", "submit"));
        // submitLink.add(new AttributeModifier("data-callback",
        // RECAPTCHA_CALLBACK)); // welke functie moet uitgevoerd worden

        return submitLink;
    }

    @Override
    public AjaxCallListener getAjaxSubmitAjaxCallListener() {
        return new AjaxCallListener() {
            @Override
            public CharSequence getPrecondition(org.apache.wicket.Component component) {
                String script = new FilePath(WicketRoot.class, "components/recaptcha/v3/captchav3-wicketajax.js").readAll();
                script = script.replace("$SITEKEY$", getSitekey());
                script = script.replace("$TOKENID$", ReCaptcha.G_RECAPTCHA_RESPONSE);
                script = script.replace('\t', ' ');
                return script;
            }
        };
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
