package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETextFieldConfig extends EComponentConfig<ETextFieldConfig> {
    protected int columns = 0;

    protected boolean enabled = true;

    protected boolean selectAllOnFocus = false;

    protected String text = null;

    public ETextFieldConfig() {
        super();
    }

    public ETextFieldConfig(boolean enabled) {
        this.enabled = enabled;
    }

    public ETextFieldConfig(boolean enabled, int columns) {
        this.enabled = enabled;
        this.columns = columns;
    }

    public ETextFieldConfig(boolean enabled, String text) {
        this.enabled = enabled;
        this.text = text;
    }

    public ETextFieldConfig(int columns) {
        this.columns = columns;
    }

    public ETextFieldConfig(String text) {
        this.text = text;
    }

    public int getColumns() {
        return this.columns;
    }

    public String getText() {
        return this.text;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isSelectAllOnFocus() {
        return this.selectAllOnFocus;
    }

    public ETextFieldConfig setColumns(int columns) {
        this.lockCheck();
        this.columns = columns;
        return this;
    }

    public ETextFieldConfig setEnabled(boolean enabled) {
        this.lockCheck();
        this.enabled = enabled;
        return this;
    }

    public ETextFieldConfig setSelectAllOnFocus(boolean selectAllOnFocus) {
        this.lockCheck();
        this.selectAllOnFocus = selectAllOnFocus;
        return this;
    }

    public ETextFieldConfig setText(String text) {
        this.lockCheck();
        this.text = text;
        return this;
    }
}
