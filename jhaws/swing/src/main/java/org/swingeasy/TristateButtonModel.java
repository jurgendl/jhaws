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
        setState(state);
    }

    private void displayState() {
        super.setSelected(state != TristateState.DESELECTED);
        super.setArmed(state == TristateState.INDETERMINATE);
        super.setPressed(state == TristateState.INDETERMINATE);

    }

    public TristateState getState() {
        return state;
    }

    public boolean isIndeterminate() {
        return state == TristateState.INDETERMINATE;
    }

    void iterateState() {
        setState(state.next());
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
        displayState();
    }

    public void setIndeterminate() {
        setState(TristateState.INDETERMINATE);
    }

    @Override
    public void setPressed(boolean b) {
        //
    }

    @Override
    public void setSelected(boolean selected) {
        setState(selected ? TristateState.SELECTED : TristateState.DESELECTED);
    }

    private void setState(TristateState state) {
        // Set internal state
        this.state = state;
        displayState();
        if (state == TristateState.INDETERMINATE && isEnabled()) {
            // force the events to fire

            // Send ChangeEvent
            fireStateChanged();

            // Send ItemEvent
            int indeterminate = 3;
            fireItemStateChanged(new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, indeterminate));
        }
    }
}
