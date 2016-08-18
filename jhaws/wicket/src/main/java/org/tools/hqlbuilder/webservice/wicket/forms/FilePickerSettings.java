package org.tools.hqlbuilder.webservice.wicket.forms;

public class FilePickerSettings extends AbstractFormElementSettings<FilePickerSettings> {
    private static final long serialVersionUID = -9053664906141038088L;

    /** accept */
    protected String mimeType;

    /** data-input */
    protected Boolean showInput;

    /** data-icon */
    protected Boolean showIcon;

    protected Boolean multiple;

    protected boolean consistentLook = false;

    protected boolean clientSideTypeCheck = false;

    public String getMimeType() {
        return this.mimeType;
    }

    public FilePickerSettings setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Boolean getShowInput() {
        return this.showInput;
    }

    public Boolean getShowIcon() {
        return this.showIcon;
    }

    public FilePickerSettings setShowInput(Boolean showInput) {
        this.showInput = showInput;
        return this;
    }

    public FilePickerSettings setShowIcon(Boolean showIcon) {
        this.showIcon = showIcon;
        return this;
    }

    public Boolean getMultiple() {
        return this.multiple;
    }

    public FilePickerSettings setMultiple(Boolean multiple) {
        this.multiple = multiple;
        return this;
    }

    public boolean isConsistentLook() {
        return this.consistentLook;
    }

    public FilePickerSettings setConsistentLook(boolean consistentLook) {
        this.consistentLook = consistentLook;
        return this;
    }

    public boolean isClientSideTypeCheck() {
        return this.clientSideTypeCheck;
    }

    public FilePickerSettings setClientSideTypeCheck(boolean clientSideTypeCheck) {
        this.clientSideTypeCheck = clientSideTypeCheck;
        return this;
    }
}
