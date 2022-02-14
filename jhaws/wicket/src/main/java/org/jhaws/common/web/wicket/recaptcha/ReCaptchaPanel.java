package org.jhaws.common.web.wicket.recaptcha;

import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public abstract class ReCaptchaPanel extends Panel {
    public ReCaptchaPanel(String id) {
        super(id);
    }

    /**
     * toe te voegen in form submit
     */
    abstract public boolean verify();

    abstract public ReCaptcha getReCaptcha();
}
