package org.swingeasy.tab;

/**
 * released under 'CC0 1.0 Universal' http://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 */
public class DnDTabbedPane extends JTabbedPane {
    public static final class DropLocation extends TransferHandler.DropLocation {
        private final int index;

        private boolean dropable = true;

        private DropLocation(Point p, int index) {
            super(p);
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }

        public boolean isDropable() {
            return this.dropable;
        }

        // public String toString() {
        // return getClass().getName()
        // + "[dropPoint=" + getDropPoint() + ","
        // + "index=" + index + ","
        // + "insert=" + isInsert + "]";
        // }
        public void setDropable(boolean flag) {
            this.dropable = flag;
        }
    }

    private class Handler extends MouseAdapter implements PropertyChangeListener { // , BeforeDrag
        private Point startPt;

        int gestureMotionThreshold = DragSource.getDragThreshold();

        // private final Integer gestureMotionThreshold = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("DnD.gestureMotionThreshold");
        @Override
        public void mouseDragged(MouseEvent e) {
            Point tabPt = e.getPoint(); // e.getDragOrigin();
            if ((this.startPt != null)
                    && (Math.sqrt(Math.pow(tabPt.x - this.startPt.x, 2) + Math.pow(tabPt.y - this.startPt.y, 2)) > this.gestureMotionThreshold)) {
                DnDTabbedPane src = (DnDTabbedPane) e.getSource();
                TransferHandler th = src.getTransferHandler();
                DnDTabbedPane.this.dragTabIndex = src.indexAtLocation(tabPt.x, tabPt.y);
                th.exportAsDrag(src, e, TransferHandler.MOVE);
                DnDTabbedPane.this.lineRect.setRect(0, 0, 0, 0);
                src.getRootPane().getGlassPane().setVisible(true);
                src.setDropLocation(new DropLocation(tabPt, -1), null, true);
                this.startPt = null;
            }
        }

        // @Override public void mouseClicked(MouseEvent e) {}
        // @Override public void mouseEntered(MouseEvent e) {}
        // @Override public void mouseExited(MouseEvent e) {}
        // @Override public void mouseMoved(MouseEvent e) {}
        // @Override public void mouseReleased(MouseEvent e) {}
        // MouseListener
        @Override
        public void mousePressed(MouseEvent e) {
            DnDTabbedPane src = (DnDTabbedPane) e.getSource();
            if (src.getTabCount() <= 0) {
                this.startPt = null;
                return;
            }
            Point tabPt = e.getPoint(); // e.getDragOrigin();
            int idx = src.indexAtLocation(tabPt.x, tabPt.y);
            // disabled tab, null component problem.
            // pointed out by daryl. NullPointerException: i.e. addTab("Tab",null)
            this.startPt = ((idx < 0) || !src.isEnabledAt(idx) || (src.getComponentAt(idx) == null)) ? null : tabPt;
        }

        // PropertyChangeListener
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if ("dropLocation".equals(propertyName)) {
                // System.out.println("propertyChange: dropLocation");
                this.repaintDropLocation(DnDTabbedPane.this.getDropLocation());
            }
        }

        private void repaintDropLocation( DropLocation loc) {
            Component c = DnDTabbedPane.this.getRootPane().getGlassPane();
            if (c instanceof GhostGlassPane) {
                GhostGlassPane glassPane = (GhostGlassPane) c;
                glassPane.setTargetTabbedPane(DnDTabbedPane.this);
                glassPane.repaint();
            }
        }
    }

    private static final long serialVersionUID = -2562830669661632426L;

    private static final int LINEWIDTH = 3;

    private final Rectangle lineRect = new Rectangle();

    public int dragTabIndex = -1;

    public static Rectangle rBackward = new Rectangle();

    public static Rectangle rForward = new Rectangle();

    private static int rwh = 20;

    private static int buttonsize = 30; // XXX 30 is magic number of scroll button size

    private DropMode dropMode = DropMode.INSERT;

    private transient DropLocation dropLocation;

    public DnDTabbedPane() {
        super();
        Handler h = new Handler();
        this.addMouseListener(h);
        this.addMouseMotionListener(h);
        this.addPropertyChangeListener(h);
    }

    public void autoScrollTest(Point pt) {
        Rectangle r = this.getTabAreaBounds();
        @SuppressWarnings("hiding")
        int tabPlacement = this.getTabPlacement();
        if ((tabPlacement == SwingConstants.TOP) || (tabPlacement == SwingConstants.BOTTOM)) {
            DnDTabbedPane.rBackward.setBounds(r.x, r.y, DnDTabbedPane.rwh, r.height);
            DnDTabbedPane.rForward.setBounds((r.x + r.width) - DnDTabbedPane.rwh - DnDTabbedPane.buttonsize, r.y, DnDTabbedPane.rwh
                    + DnDTabbedPane.buttonsize, r.height);
        } else if ((tabPlacement == SwingConstants.LEFT) || (tabPlacement == SwingConstants.RIGHT)) {
            DnDTabbedPane.rBackward.setBounds(r.x, r.y, r.width, DnDTabbedPane.rwh);
            DnDTabbedPane.rForward.setBounds(r.x, (r.y + r.height) - DnDTabbedPane.rwh - DnDTabbedPane.buttonsize, r.width, DnDTabbedPane.rwh
                    + DnDTabbedPane.buttonsize);
        }
        if (DnDTabbedPane.rBackward.contains(pt)) {
            this.clickArrowButton("scrollTabsBackwardAction");
        } else if (DnDTabbedPane.rForward.contains(pt)) {
            this.clickArrowButton("scrollTabsForwardAction");
        }
    }

    private void clickArrowButton(String actionKey) {
        ActionMap map = this.getActionMap();
        if (map != null) {
            Action action = map.get(actionKey);
            if ((action != null) && action.isEnabled()) {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
            }
        }
    }

    public void convertTab(int prev, int next) {
        // System.out.println("convertTab");
        if ((next < 0) || (prev == next)) {
            return;
        }
        Component cmp = this.getComponentAt(prev);
        Component tab = this.getTabComponentAt(prev);
        String str = this.getTitleAt(prev);
        Icon icon = this.getIconAt(prev);
        String tip = this.getToolTipTextAt(prev);
        boolean flg = this.isEnabledAt(prev);
        int tgtindex = prev > next ? next : next - 1;
        this.remove(prev);
        this.insertTab(str, icon, cmp, tip, tgtindex);
        this.setEnabledAt(tgtindex, flg);
        // When you drag'n'drop a disabled tab, it finishes enabled and selected.
        // pointed out by dlorde
        if (flg) {
            this.setSelectedIndex(tgtindex);
        }
        // I have a component in all tabs (jlabel with an X to close the tab) and when i move a tab the component disappear.
        // pointed out by Daniel Dario Morales Salas
        this.setTabComponentAt(tgtindex, tab);
    }

    // warning can be ignored
    public DropLocation dropLocationForPoint(final Point p) {
        
        boolean isTB = (this.getTabPlacement() == SwingConstants.TOP) || (this.getTabPlacement() == SwingConstants.BOTTOM);
        switch (this.dropMode) {
            case INSERT:
                for (int i = 0; i < this.getTabCount(); i++) {
                    if (this.getBoundsAt(i).contains(p)) {
                        return new DropLocation(p, i);
                    }
                }
                if (this.getTabAreaBounds().contains(p)) {
                    return new DropLocation(p, this.getTabCount());
                }
                break;
            case USE_SELECTION:
            case ON:
            case ON_OR_INSERT:
            default:
                assert false : "Unexpected drop mode";
        }
        return new DropLocation(p, -1);
    }

    public void exportTab(int dragIndex, JTabbedPane target, int targetIndex) {
        // System.out.println("exportTab");
        if (targetIndex < 0) {
            return;
        }

        Component cmp = this.getComponentAt(dragIndex);
        Container parent = target;
        while (parent != null) {
            if (cmp == parent) {
                return; // target==child: JTabbedPane in JTabbedPane
            }
            parent = parent.getParent();
        }

        Component tab = this.getTabComponentAt(dragIndex);
        String str = this.getTitleAt(dragIndex);
        Icon icon = this.getIconAt(dragIndex);
        String tip = this.getToolTipTextAt(dragIndex);
        boolean flg = this.isEnabledAt(dragIndex);
        this.remove(dragIndex);
        target.insertTab(str, icon, cmp, tip, targetIndex);
        target.setEnabledAt(targetIndex, flg);
        // //ButtonTabComponent
        // if(tab instanceof ButtonTabComponent) tab = new ButtonTabComponent(target);
        // target.setTabComponentAt(targetIndex, tab);
        target.setSelectedIndex(targetIndex);
        if ((tab != null) && (tab instanceof JComponent)) {
            ((JComponent) tab).scrollRectToVisible(tab.getBounds());
        }
    }

    public Rectangle getDropLineRect() {
        DropLocation loc = this.getDropLocation();
        if ((loc == null) || !loc.isDropable()) {
            return null;
        }

        int index = loc.getIndex();
        if (index < 0) {
            this.lineRect.setRect(0, 0, 0, 0);
            return null;
        }
        boolean isZero = index == 0;
        try {
            Rectangle r = this.getBoundsAt(isZero ? 0 : index - 1);
            if ((this.getTabPlacement() == SwingConstants.TOP) || (this.getTabPlacement() == SwingConstants.BOTTOM)) {
                this.lineRect.setRect((r.x - (DnDTabbedPane.LINEWIDTH / 2)) + (r.width * (isZero ? 0 : 1)), r.y, DnDTabbedPane.LINEWIDTH, r.height);
            } else {
                this.lineRect.setRect(r.x, (r.y - (DnDTabbedPane.LINEWIDTH / 2)) + (r.height * (isZero ? 0 : 1)), r.width, DnDTabbedPane.LINEWIDTH);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            this.lineRect.setRect(0, 0, 0, 0);
        } catch (IndexOutOfBoundsException ex) {
            this.lineRect.setRect(0, 0, 0, 0);
        }
        return this.lineRect;
    }

    public final DropLocation getDropLocation() {
        return this.dropLocation;
    }

    public Rectangle getTabAreaBounds() {
        Rectangle tabbedRect = this.getBounds();
        int xx = tabbedRect.x;
        int yy = tabbedRect.y;
        Component c = this.getSelectedComponent();
        if (c == null) {
            return tabbedRect;
        }
        Rectangle compRect = this.getSelectedComponent().getBounds();
        @SuppressWarnings("hiding")
        int tabPlacement = this.getTabPlacement();
        if (tabPlacement == SwingConstants.TOP) {
            tabbedRect.height = tabbedRect.height - compRect.height;
        } else if (tabPlacement == SwingConstants.BOTTOM) {
            tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
            tabbedRect.height = tabbedRect.height - compRect.height;
        } else if (tabPlacement == SwingConstants.LEFT) {
            tabbedRect.width = tabbedRect.width - compRect.width;
        } else if (tabPlacement == SwingConstants.RIGHT) {
            tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
            tabbedRect.width = tabbedRect.width - compRect.width;
        }
        tabbedRect.translate(-xx, -yy);
        // tabbedRect.grow(2, 2);
        return tabbedRect;
    }

    // warning can be ignored
    
    public Object setDropLocation(final TransferHandler.DropLocation location, final Object state, final boolean forDrop) {
        DropLocation old = this.dropLocation;
        if ((location == null) || !forDrop) {
            this.dropLocation = new DropLocation(new Point(), -1);
        } else if (location instanceof DropLocation) {
            this.dropLocation = (DropLocation) location;
        }
        this.firePropertyChange("dropLocation", old, this.dropLocation);
        return null;
    }
}
