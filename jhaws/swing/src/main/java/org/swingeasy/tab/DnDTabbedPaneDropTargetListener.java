package org.swingeasy.tab;

/**
 * released under 'CC0 1.0 Universal' http://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

import java.awt.Component;
import java.awt.Cursor;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 */
public class DnDTabbedPaneDropTargetListener extends DropTargetAdapter {
    private void clearDropLocationPaint(Component c) {
        // System.out.println("------------------- " + c.getName());
        if (c instanceof DnDTabbedPane) {
            DnDTabbedPane t = (DnDTabbedPane) c;
            t.setDropLocation(null, null, false);
            t.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // System.out.println("DropTargetListener#dragEnter");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        // System.out.println("DropTargetListener#dragExit");
        Component c = dte.getDropTargetContext().getComponent();
        this.clearDropLocationPaint(c);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // System.out.println("dragOver");
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        // System.out.println("DropTargetListener#drop");
        Component c = dtde.getDropTargetContext().getComponent();
        this.clearDropLocationPaint(c);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // System.out.println("dropActionChanged");
    }
}
