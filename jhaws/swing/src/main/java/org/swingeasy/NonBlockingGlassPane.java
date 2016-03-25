package org.swingeasy;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * GlassPane tutorial "A well-behaved GlassPane" http://weblogs.java.net/blog/alexfromsun/
 * <p/>
 * This is the final version of the GlassPane it is transparent for MouseEvents, and respects underneath component's cursors by default, it is also
 * friedly for other users, if someone adds a mouseListener to this GlassPane or set a new cursor it will respect them
 * 
 * @author Alexander Potochkin
 */
public class NonBlockingGlassPane extends JPanel implements AWTEventListener {
    
    private static final long serialVersionUID = -2073312139654377382L;

    private final Window frame;

    public NonBlockingGlassPane(Window frame) {
        super(null);
        this.frame = frame;
        this.setOpaque(false);
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * If someone adds a mouseListener to the GlassPane or set a new cursor we expect that he knows what he is doing and return the super.contains(x,
     * y) otherwise we return false to respect the cursors for the underneath components
     */
    @Override
    public boolean contains(int x, int y) {
        if ((this.getMouseListeners().length == 0) && (this.getMouseMotionListeners().length == 0) && (this.getMouseWheelListeners().length == 0)
                && (this.getCursor() == Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))) {
            return false;
        }
        return super.contains(x, y);
    }

    /**
     * 
     * @see java.awt.event.AWTEventListener#eventDispatched(java.awt.AWTEvent)
     */
    @Override
    public void eventDispatched(AWTEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) event;
            if (me.getComponent() == null) {
                return;
            }
            if (!SwingUtilities.isDescendingFrom(me.getComponent(), this.frame)) {
                return;
            }
        }
    }
}
