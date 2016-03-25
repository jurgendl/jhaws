package org.swingeasy;

/**
 * @author Jurgen
 */
public class EListConfig extends EComponentConfig<EListConfig> {
    protected boolean threadSafe = true;

    protected boolean sortable = true;

    protected boolean filterable = false;

    protected EComponentRenderer backgroundRenderer;

    public EListConfig() {
        super();
    }

    public EComponentRenderer getBackgroundRenderer() {
        return this.backgroundRenderer;
    }

    public boolean isFilterable() {
        return this.filterable;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public EListConfig setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        this.lockCheck();
        this.backgroundRenderer = backgroundRenderer;
        return this;
    }

    public EListConfig setFilterable(boolean filterable) {
        this.lockCheck();
        this.filterable = filterable;
        return this;
    }

    public EListConfig setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
        return this;
    }

    public EListConfig setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }
}
