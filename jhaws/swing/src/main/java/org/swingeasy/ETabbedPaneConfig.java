package org.swingeasy;

public class ETabbedPaneConfig extends EComponentConfig<ETabbedPaneConfig> {
    protected boolean closable;

    protected boolean minimizable;

    protected Rotation rotation = Rotation.DEFAULT;

    public ETabbedPaneConfig() {
        this(false, false);
    }

    public ETabbedPaneConfig(boolean closable, boolean minimizable) {
        this(Rotation.DEFAULT, closable, minimizable);
    }

    public ETabbedPaneConfig(Rotation rotation, boolean closable, boolean minimizable) {
        super();
        this.rotation = rotation;
        this.closable = closable;
        this.minimizable = minimizable;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public boolean isClosable() {
        return this.closable;
    }

    public boolean isMinimizable() {
        return this.minimizable;
    }

    public ETabbedPaneConfig setClosable(boolean closable) {
        this.lockCheck();
        this.closable = closable;
        return this;
    }

    public ETabbedPaneConfig setMinimizable(boolean minimizable) {
        this.lockCheck();
        this.minimizable = minimizable;
        return this;
    }

    public ETabbedPaneConfig setRotation(Rotation rotation) {
        this.lockCheck();
        this.rotation = rotation;
        return this;
    }
}
