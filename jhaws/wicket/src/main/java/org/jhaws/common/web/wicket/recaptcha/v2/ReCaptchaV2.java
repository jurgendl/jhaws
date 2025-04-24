package org.jhaws.common.web.wicket.recaptcha.v2;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.IModel;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.web.wicket.WicketRoot;
import org.jhaws.common.web.wicket.recaptcha.ReCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @see https://stackoverflow.com/questions/9908556/can-i-use-re-captcha-with-wicket
 * @see https://developers.google.com/recaptcha
 * @see https://developers.google.com/recaptcha/docs/versions
 */
@Component
@SuppressWarnings("serial")
public class ReCaptchaV2 implements ReCaptcha {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReCaptchaV2.class);

    public static final String API = "https://www.google.com/recaptcha/api.js";

    public static final String LANG = "?hl=";

    public static final String VERIFY = "https://www.google.com/recaptcha/api/siteverify";

    /** zet system NoRecaptchaValidation=true om te disabelen, bv bij tests */
    public static final String NO_RECAPTCHA_VALIDATION = "NoRecaptchaValidation";

    public static final String RECAPTCHA_CALLBACK = "reCaptchaCallback";

    /** ingesteld by default */
    @Value("${recaptcha.v2.verifyUrl:" + VERIFY + "}")
    protected String verifyUrl = VERIFY;

    /** checkbox moet aangevinkt worden <> auto (invisible) */
    @Value("${recaptcha.v2.visible:true}")
    protected Boolean visible = Boolean.TRUE;

    /** in te stellen maar crasht niet wanneer niet ingesteld */
    @Value("${recaptcha.v2.visible.sitekey:-}")
    protected String visible_sitekey;

    /** in te stellen maar crasht niet wanneer niet ingesteld */
    @Value("${recaptcha.v2.visible.secret:-}")
    protected String visible_secret;

    /** in te stellen maar crasht niet wanneer niet ingesteld */
    @Value("${recaptcha.v2.invisible.sitekey:-}")
    protected String invisible_sitekey;

    /** in te stellen maar crasht niet wanneer niet ingesteld */
    @Value("${recaptcha.v2.invisible.secret:-}")
    protected String invisible_secret;

    /** by default is versie 2 actief */
    @Value("${recaptcha.version:v2}")
    protected String version = "v2";

    @Override
    public String getSitekey() {
        return visible ? visible_sitekey : invisible_sitekey;
    }

    /**
     * extra nodig op niet ajax submit: we registreren een callback function bij onlick die de originele submit doet 'window.reCaptchaCallback'
     * (ReCaptchaV2Panel.RECAPTCHA_CALLBACK), we breken de onclick submit af en doen de captcha check "grecaptcha.execute" die bij success de nieuwe callback uitvoert en dus submit
     * zoals normaal
     */
    @Override
    public SubmitLink adjustNonAjaxReCaptchaSubmit(String id, IModel<?> model, Form<?> form) {
        // WICKET-6/7
        // SubmitLink submitLink = new SubmitLink(id, model, form);
        // AttributeModifier am = new AttributeModifier("onclick", "") {
        // @Override
        // protected String newValue(String currentValue, String
        // replacementValue) {
        // if (getVisible()) {
        // return currentValue;
        // }
        // return ";window." + RECAPTCHA_CALLBACK + "=function(){" +
        // currentValue.toString() +
        // "};event.preventDefault();grecaptcha.execute();";
        // }
        // };
        // submitLink.add(am);

        // WICKET-9
        SubmitLink submitLink = new SubmitLink(id, model, form) {
            @Override
            protected CharSequence getTriggerJavaScript() {
                CharSequence triggerJavaScript = super.getTriggerJavaScript();
                if (triggerJavaScript != null) {
                    return ";window." + RECAPTCHA_CALLBACK + "=function(){" + triggerJavaScript + "};event.preventDefault();grecaptcha.execute();";
                }
                return triggerJavaScript;
            }
        };

        return submitLink;
    }

    @Override
    public AjaxCallListener getAjaxSubmitAjaxCallListener() {
        if (getVisible()) {
            return null;
        }
        return new AjaxCallListener() {
            @Override
            public CharSequence getPrecondition(org.apache.wicket.Component component) {
                String script = new FilePath(WicketRoot.class, "components/recaptcha/v2/captchav2-wicketajax.js").readAll();
                script = script.replace("$SITEKEY$", getSitekey());
                script = script.replace("$TOKENID$", ReCaptcha.G_RECAPTCHA_RESPONSE);
                script = script.replace('\t', ' ');
                return script;
            }
        };
    }

    @Override
    public boolean validateCaptcha(String recaptchaUserResponse, String remoteip) {
        try {
            if (recaptchaUserResponse == null) {
                return false;
            }
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
            map.add("secret", visible ? visible_secret : invisible_secret);
            map.add("response", recaptchaUserResponse);
            if (remoteip != null) {
                map.add("remoteip", remoteip);
            }
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> res = rt.exchange(verifyUrl, HttpMethod.POST, httpEntity, String.class);
            if (res == null || res.getBody() == null) {
                return false;
            }
            ReCaptchaV2Response response = OBJECT_MAPPER.readerFor(ReCaptchaV2Response.class).readValue(res.getBody());
            LOGGER.info("ReCaptcha Response '{}'", response);
            return response != null && response.getSuccess() != null && response.getSuccess();
        } catch (Exception e) {
            LOGGER.error("Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verify(Form<?> form) {
        HttpServletRequest servletRequest = (HttpServletRequest) form.getWebRequest().getContainerRequest();
        return verify(servletRequest);
    }

    @Override
    public boolean verify(HttpServletRequest httpServletRequest) {
        if ("true".equals(System.getProperty(NO_RECAPTCHA_VALIDATION))) {
            return true;
        }
        if (httpServletRequest == null) {
            return false;
        }
        String remoteAddr = httpServletRequest.getRemoteAddr();
        String response = httpServletRequest.getParameter(G_RECAPTCHA_RESPONSE);
        if (StringUtils.isBlank(response)) {
            return false;
        }
        LOGGER.info("token: {}", response);
        return validateCaptcha(response, remoteAddr);
    }

    @Override
    public String getVerifyUrl() {
        return this.verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getVisible_sitekey() {
        return this.visible_sitekey;
    }

    public void setVisible_sitekey(String visible_sitekey) {
        this.visible_sitekey = visible_sitekey;
    }

    public String getVisible_secret() {
        return this.visible_secret;
    }

    public void setVisible_secret(String visible_secret) {
        this.visible_secret = visible_secret;
    }

    public String getInvisible_sitekey() {
        return this.invisible_sitekey;
    }

    public void setInvisible_sitekey(String invisible_sitekey) {
        this.invisible_sitekey = invisible_sitekey;
    }

    public String getInvisible_secret() {
        return this.invisible_secret;
    }

    public void setInvisible_secret(String invisible_secret) {
        this.invisible_secret = invisible_secret;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}