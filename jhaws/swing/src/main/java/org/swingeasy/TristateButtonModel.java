package org.swingeasy;

import java.awt.event.ItemEvent;

import javax.swing.JToggleButton.ToggleButtonModel;

public class TristateButtonModel extends ToggleButtonModel {
    private static final long serialVersionUID = -312455813197883670L;

    private TristateState state = TristateState.DESELECTED;

    public TristateButtonModel() {
        this(TristateState.DESELECTED);
    }

    public TristateButtonModel(TristateState state) {
        this.setState(state);
    }

    private void displayState() {
        super.setSelected(this.state != TristateState.DESELECTED);
        super.setArmed(this.state == TristateState.INDETERMINATE);
        super.setPressed(this.state == TristateState.INDETERMINATE);

    }

    public TristateState getState() {
        return this.state;
    }

    public boolean isIndeterminate() {
        return this.state == TristateState.INDETERMINATE;
    }

    void iterateState() {
        this.setState(this.state.next());
    }

    // Empty overrides of superclass methods
    @Override
    public void setArmed(boolean b) {
        //
    }

    // Overrides of superclass methods
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        // Restore state display
        this.displayState();
    }

    public void setIndeterminate() {
        this.setState(TristateState.INDETERMINATE);
    }

    @Override
    public void setPressed(boolean b) {
        //
    }

    @Override
    public void setSelected(boolean selected) {
        this.setState(selected ? TristateState.SELECTED : TristateState.DESELECTED);
    }

    private void setState(TristateState state) {
        // Set internal state
        this.state = state;
        this.displayState();
        if ((state == TristateState.INDETERMINATE) && this.isEnabled()) {
            // force the events to fire

            // Send ChangeEvent
            this.fireStateChanged();

            // Send ItemEvent
            int indeterminate = 3;
            this.fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, indeterminate));
        }
    }
}
