package org.tools.hqlbuilder.webservice.wicket.forms;

import java.io.Serializable;
import java.util.Locale;

public class SerializableLocale implements Serializable {
    private static final long serialVersionUID = 4357999354845881793L;

    protected String id;

    protected transient Locale locale;

    public SerializableLocale() {
        super();
    }

    public SerializableLocale(Locale locale) {
        id = locale.toString();
    }

    public Locale getLocale() {
        if (locale == null && id != null) {
            for (Locale it : Locale.getAvailableLocales()) {
                if (it.toString().equals(id)) {
                    locale = it;
                    break;
                }
            }
        }
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.id = locale.toString();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}