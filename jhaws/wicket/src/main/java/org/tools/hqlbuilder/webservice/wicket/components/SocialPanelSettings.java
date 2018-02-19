package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

public class SocialPanelSettings implements Serializable {
    private static final long serialVersionUID = 1091881462422787495L;

    public static enum SocialForm {
        button, bar, pin;
    }

    private SocialForm form = SocialForm.button;

    private boolean fading = false;

    public boolean isFading() {
        return this.fading;
    }

    public SocialPanelSettings setFading(boolean fading) {
        this.fading = fading;
        return this;
    }

    public SocialForm getForm() {
        return this.form;
    }

    public SocialPanelSettings setForm(SocialForm form) {
        this.form = form;
        return this;
    }
}
