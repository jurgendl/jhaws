package org.tools.hqlbuilder.webservice.wicket.forms;

import org.tools.hqlbuilder.webservice.jquery.ui.ckeditor.CKEditor.CKEType;

/**
 * @see http://docs.ckeditor.com/#!/api/CKEDITOR.config
 */
public class CKEditorTextAreaSettings extends AbstractFormElementSettings<CKEditorTextAreaSettings> {
    private static final long serialVersionUID = 2746452642715136812L;

    protected CKEType type;

    protected String language;

    @Override
    protected boolean skipForExport(String propertyName) {
        return super.skipForExport(propertyName) || "type".equals(propertyName);
    }

    public CKEType getType() {
        return this.type;
    }

    public CKEditorTextAreaSettings setType(CKEType type) {
        this.type = type;
        return this;
    }

    public String getLanguage() {
        return this.language;
    }

    public CKEditorTextAreaSettings setLanguage(String language) {
        this.language = language;
        return this;
    }
}
