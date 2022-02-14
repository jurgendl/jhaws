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
        threadSafe = true;
    }

    public ETableConfig(boolean all) {
        this(all, all, all, all, all, all, all, false);
        threadSafe = true;
        vertical = false;
    }

    public ETableConfig(boolean threadSafe, boolean sortable, boolean filterable, boolean editable, boolean reorderable, boolean resizable, boolean vertical, boolean draggable) {
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
        return backgroundRenderer;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isFilterable() {
        return filterable;
    }

    public boolean isReorderable() {
        return reorderable;
    }

    public boolean isResizable() {
        return resizable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isThreadSafe() {
        return threadSafe;
    }

    public boolean isVertical() {
        return vertical;
    }

    public ETableConfig setBackgroundRenderer(EComponentRenderer backgroundRenderer) {
        lockCheck();
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
        lockCheck();
        this.filterable = filterable;
        return this;
    }

    public ETableConfig setReorderable(boolean reorderable) {
        lockCheck();
        this.reorderable = reorderable;
        return this;
    }

    public ETableConfig setResizable(boolean resizable) {
        lockCheck();
        this.resizable = resizable;
        return this;
    }

    public ETableConfig setSortable(boolean sortable) {
        lockCheck();
        this.sortable = sortable;
        return this;
    }

    public ETableConfig setThreadSafe(boolean threadSafe) {
        lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }

    public ETableConfig setVertical(boolean vertical) {
        lockCheck();
        this.vertical = vertical;
        return this;
    }
}
