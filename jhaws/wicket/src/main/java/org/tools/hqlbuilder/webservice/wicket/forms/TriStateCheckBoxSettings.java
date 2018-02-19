package org.tools.hqlbuilder.webservice.wicket.forms;

import org.tools.hqlbuilder.webservice.wicket.forms.common.AbstractFormElementSettings;

public class TriStateCheckBoxSettings extends AbstractFormElementSettings<TriStateCheckBoxSettings> {
    private static final long serialVersionUID = 8680530201586036946L;

    public TriStateCheckBoxSettings() {
        super();
    }

    public TriStateCheckBoxSettings(boolean required) {
        super(required);
    }

    public TriStateCheckBoxSettings(TriStateCheckBoxSettings other) {
        super(other);
    }
}
