package org.swingeasy;

/**
 * see {@link EventHelper}
 * 
 * @author Jurgen
 */
public enum EventModifier {
    NONE_DOWN(0), //
    CTRL_DOWN(java.awt.event.InputEvent.CTRL_DOWN_MASK), //
    ALT_DOWN(java.awt.event.InputEvent.ALT_DOWN_MASK), //
    SHIFT_DOWN(java.awt.event.InputEvent.SHIFT_DOWN_MASK), //
    CTR_ALT_DOWN(java.awt.event.InputEvent.CTRL_DOWN_MASK | java.awt.event.InputEvent.ALT_DOWN_MASK), //
    CTR_SHIFT_DOWN(java.awt.event.InputEvent.CTRL_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK), //
    ALT_SHIFT_DOWN(java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK), //
    ALL_DOWN(java.awt.event.InputEvent.CTRL_DOWN_MASK | java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.SHIFT_DOWN_MASK);//
    int real;

    private EventModifier(int real) {
        this.real = real;
    }
}