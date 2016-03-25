package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETableConfig extends EComponentConfig<ETableConfig> {
    protected boolean threadSafe = true;

    protected boolean sortable;

    protected boolean filterable;

    protected boolean editable;

    protected boolean reorderable;

    protected boolean resizable;

    protected boolean vertical;

    protected boolean draggable;

    protected EComponentRenderer backgroundRenderer;

    public ETableConfig() {
        super();
        this.threadSafe = true;
    }

    public ETableConfig(boolean all) {
        this(all, all, all, all, all, all, all, false);
        this.threadSafe = true;
        this.vertical = false;
    }

    public ETableConfig(boolean threadSafe, boolean sortable, boolean filterable, boolean editable, boolean reorderable, boolean resizable,
            boolean vertical, boolean draggable) {
        super();
        this.threadSafe = threadSafe;
        this.sortable = sortable;
        this.filterable = filterable;
        this.editable = editable;
        this.reorderable = reorderable;
        this.resizable = resizable;
        this.vertical = vertical;
        this.draggable = draggable;
    }

    public EComponentRenderer getBackgroundRenderer() {
        return this.backgroundRenderer;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public boolean isFilterable() {
        return this.filterable;
    }

    public boolean isReorderable() {
        return this.reorderable;
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public boolean isVertical() {
        return this.vertical;
    }

    public ETableConfig setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        this.lockCheck();
        this.backgroundRenderer = backgroundRenderer;
        return this;
    }

    public ETableConfig setDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public ETableConfig setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public ETableConfig setFilterable(boolean filterable) {
        this.lockCheck();
        this.filterable = filterable;
        return this;
    }

    public ETableConfig setReorderable(boolean reorderable) {
        this.lockCheck();
        this.reorderable = reorderable;
        return this;
    }

    public ETableConfig setResizable(boolean resizable) {
        this.lockCheck();
        this.resizable = resizable;
        return this;
    }

    public ETableConfig setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
        return this;
    }

    public ETableConfig setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }

    public ETableConfig setVertical(boolean vertical) {
        this.lockCheck();
        this.vertical = vertical;
        return this;
    }
}
