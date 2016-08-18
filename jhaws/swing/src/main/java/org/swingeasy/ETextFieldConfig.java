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
        return columns;
    }

    public String getText() {
        return text;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSelectAllOnFocus() {
        return selectAllOnFocus;
    }

    public ETextFieldConfig setColumns(int columns) {
        lockCheck();
        this.columns = columns;
        return this;
    }

    public ETextFieldConfig setEnabled(boolean enabled) {
        lockCheck();
        this.enabled = enabled;
        return this;
    }

    public ETextFieldConfig setSelectAllOnFocus(boolean selectAllOnFocus) {
        lockCheck();
        this.selectAllOnFocus = selectAllOnFocus;
        return this;
    }

    public ETextFieldConfig setText(String text) {
        lockCheck();
        this.text = text;
        return this;
    }
}
