package org.swingeasy;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * @author Jurgen
 */
public class EventHelper {
    public static boolean isModification(KeyEvent e) {
        return ((e.getKeyCode() == KeyEvent.VK_ALT) || (e.getKeyCode() == KeyEvent.VK_CONTROL) || (e.getKeyCode() == KeyEvent.VK_SHIFT));
    }

    public static boolean isModified(KeyEvent e) {
        return EventHelper.keyEvent(e, EventModifier.CTRL_DOWN) || EventHelper.keyEvent(e, EventModifier.ALT_DOWN)
                || EventHelper.keyEvent(e, EventModifier.SHIFT_DOWN);
    }

    public static boolean isMouse1x1(MouseEvent e) {
        return (e.getClickCount() == 1) && (e.getButton() == MouseEvent.BUTTON1);
    }

    public static boolean isMouse1x2(MouseEvent e) {
        return (e.getClickCount() == 1) && (e.getButton() == MouseEvent.BUTTON2);
    }

    public static boolean isMouse1x3(MouseEvent e) {
        return (e.getClickCount() == 1) && (e.getButton() == MouseEvent.BUTTON3);
    }

    public static boolean isMouse2x1(MouseEvent e) {
        return (e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1);
    }

    public static boolean isMouse2x2(MouseEvent e) {
        return (e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON2);
    }

    public static boolean isMouse2x3(MouseEvent e) {
        return (e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON3);
    }

    public static boolean isNavigate(KeyEvent e) {
        return ((e.getKeyCode() == KeyEvent.VK_DOWN) || (e.getKeyCode() == KeyEvent.VK_UP) || (e.getKeyCode() == KeyEvent.VK_LEFT)
                || (e.getKeyCode() == KeyEvent.VK_RIGHT) || (e.getKeyCode() == KeyEvent.VK_HOME) || (e.getKeyCode() == KeyEvent.VK_END));
    }

    public static boolean keyEvent(KeyEvent event, char character) {
        return event.getKeyChar() == character;
    }

    public static boolean keyEvent(KeyEvent event, EventModifier onmask) {
        return EventHelper.keyEvent(event, onmask, EventModifier.NONE_DOWN);
    }

    public static boolean keyEvent(KeyEvent event, EventModifier onmask, char character) {
        return EventHelper.keyEvent(event, onmask) && EventHelper.keyEvent(event, character);
    }

    public static boolean keyEvent(KeyEvent event, EventModifier onmask, EventModifier offmask) {
        int on = onmask.real;
        int off = offmask.real;
        return ((event.getModifiersEx() & (on | off)) == on);
    }

    public static boolean keyEvent(KeyEvent event, EventModifier onmask, EventModifier offmask, char character) {
        return EventHelper.keyEvent(event, onmask, offmask) && EventHelper.keyEvent(event, character);
    }
}
