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
        return backgroundRenderer;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isThreadSafe() {
        return threadSafe;
    }

    public EListConfig setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        lockCheck();
        this.backgroundRenderer = backgroundRenderer;
        return this;
    }

    public EListConfig setFilterable(boolean filterable) {
        lockCheck();
        this.filterable = filterable;
        return this;
    }

    public EListConfig setSortable(boolean sortable) {
        lockCheck();
        this.sortable = sortable;
        return this;
    }

    public EListConfig setThreadSafe(boolean threadSafe) {
        lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }
}
