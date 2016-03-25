package org.swingeasy;

/**
 * only autoResizePopup kan be changed after creation of the {@link EComboBox}
 * 
 * @author Jurgen
 */
public class EComboBoxConfig extends EComponentConfig<EComboBoxConfig> {
    protected boolean autoComplete = true;

    protected boolean threadSafe = true;

    protected boolean scrolling = true;

    protected boolean sortable = true;

    protected boolean autoResizePopup = true;

    public EComboBoxConfig() {
        super();
    }

    public boolean isAutoComplete() {
        return this.autoComplete;
    }

    public boolean isAutoResizePopup() {
        return this.autoResizePopup;
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public EComboBoxConfig setAutoComplete(boolean autoComplete) {
        this.lockCheck();
        this.autoComplete = autoComplete;
        return this;
    }

    public EComboBoxConfig setAutoResizePopup(boolean autoResizePopup) {
        this.lockCheck();
        this.autoResizePopup = autoResizePopup;
        return this;
    }

    public EComboBoxConfig setScrolling(boolean scrolling) {
        this.lockCheck();
        this.scrolling = scrolling;
        return this;
    }

    public EComboBoxConfig setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
        return this;
    }

    public EComboBoxConfig setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }
}
