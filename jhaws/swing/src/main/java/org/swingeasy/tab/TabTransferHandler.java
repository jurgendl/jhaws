package org.swingeasy.tab;

/**
 * released under 'CC0 1.0 Universal' http://creativecommons.org/publicdomain/zero/1.0/legalcode
 */

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.image.BufferedImage;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 */
@SuppressWarnings("unchecked")
public class TabTransferHandler<T extends org.swingeasy.tab.DnDTabbedPane> extends TransferHandler {
    
    private static final long serialVersionUID = 7994723426669020551L;

    private final DataFlavor localObjectFlavor;

    private T source = null;

    private static GhostGlassPane glassPane;

    public TabTransferHandler(Class<T> type) {
        // System.out.println("TabTransferHandler");
        this.localObjectFlavor = new ActivationDataFlavor(type, DataFlavor.javaJVMLocalObjectMimeType, type.getName());
    }

    @Override
    public boolean canImport(TransferSupport support) {
        // System.out.println("canImport");
        boolean drop = support.isDrop();
        boolean dataFlavorSupported = support.isDataFlavorSupported(this.localObjectFlavor);
        if (!drop || !dataFlavorSupported) {
            // System.out.println("canImport:" + drop + " " + dataFlavorSupported);
            return false;
        }
        support.setDropAction(TransferHandler.MOVE);
        DropLocation tdl = support.getDropLocation();
        Point pt = tdl.getDropPoint();
        T target = (T) support.getComponent();
        target.autoScrollTest(pt);
        DnDTabbedPane.DropLocation dl = target.dropLocationForPoint(pt);
        int idx = dl.getIndex();
        boolean isDropable = false;

        // DnDTabbedPane source = TabTransferHandler.source;
        // if(!isWebStart()) {
        // try{
        // source = (DnDTabbedPane)support.getTransferable().getTransferData(localObjectFlavor);
        // }catch(Exception ex) {
        // ex.printStackTrace();
        // }
        // }
        if (target == this.source) {
            // System.out.println("target==source");
            isDropable = target.getTabAreaBounds().contains(pt) && (idx >= 0) && (idx != target.dragTabIndex) && (idx != (target.dragTabIndex + 1));
        } else {
            // System.out.format("target!=source%n  target: %s%n  source: %s", target.getName(), source.getName());
            if ((this.source != null) && (target != this.source.getComponentAt(this.source.dragTabIndex))) {
                isDropable = target.getTabAreaBounds().contains(pt) && (idx >= 0);
            }
        }
        TabTransferHandler.glassPane.setVisible(false);
        target.getRootPane().setGlassPane(TabTransferHandler.glassPane);
        // XXX: [http://bugs.sun.com/view_bug.do?bug_id=6700748 Cursor flickering during D&D when using CellRendererPane with validation]
        TabTransferHandler.glassPane.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        TabTransferHandler.glassPane.setVisible(true);
        target.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        // Component c = target.getRootPane().getGlassPane();
        // c.setCursor(isDropable?DragSource.DefaultMoveDrop:DragSource.DefaultMoveNoDrop);
        if (isDropable) {
            support.setShowDropLocation(true);
            dl.setDropable(true);
            target.setDropLocation(dl, null, true);
            return true;
        }
        support.setShowDropLocation(false);
        dl.setDropable(false);
        target.setDropLocation(dl, null, false);
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        // System.out.println("createTransferable");
        if (c instanceof DnDTabbedPane) {
            this.source = (T) c;
        }
        return new DataHandler(c, this.localObjectFlavor.getMimeType());
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        // System.out.println("exportDone");
        T src = (T) c;
        src.setDropLocation(null, null, false);
        src.repaint();
        TabTransferHandler.glassPane.setVisible(false);
        src.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        // glassPane = null;
        // source = null;
    }

    @Override
    public int getSourceActions(JComponent c) {
        // System.out.println("getSourceActions");
        T src = (T) c;
        if (TabTransferHandler.glassPane == null) {
            c.getRootPane().setGlassPane(TabTransferHandler.glassPane = new GhostGlassPane(src));
        }
        if (src.dragTabIndex < 0) {
            return TransferHandler.NONE;
        }
        TabTransferHandler.glassPane.setImage(this.makeDragTabImage(src));
        // setDragImage(makeDragTabImage(src)); //java 1.7.0-ea-b84
        c.getRootPane().getGlassPane().setVisible(true);
        return TransferHandler.MOVE;
    }

    @Override
    public boolean importData(TransferSupport support) {
        // System.out.println("importData");
        if (!this.canImport(support)) {
            return false;
        }

        T target = (T) support.getComponent();
        DnDTabbedPane.DropLocation dl = target.getDropLocation();
        try {
            T _source = (T) support.getTransferable().getTransferData(this.localObjectFlavor);
            int index = dl.getIndex(); // boolean insert = dl.isInsert();
            if (target == _source) {
                _source.convertTab(_source.dragTabIndex, index); // getTargetTabIndex(e.getLocation()));
            } else {
                _source.exportTab(_source.dragTabIndex, target, index);
            }
            return true;
        } catch (UnsupportedFlavorException ufe) {
            ufe.printStackTrace();
        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    // private static boolean isWebStart() {
    // try{
    // javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
    // return true;
    // }catch(Exception ex) {
    // return false;
    // }
    // }
    private BufferedImage makeDragTabImage(DnDTabbedPane tabbedPane) {
        Rectangle rect = tabbedPane.getBoundsAt(tabbedPane.dragTabIndex);
        BufferedImage image = new BufferedImage(tabbedPane.getWidth(), tabbedPane.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        tabbedPane.paint(g);
        g.dispose();
        if (rect.x < 0) {
            rect.translate(-rect.x, 0);
        }
        if (rect.y < 0) {
            rect.translate(0, -rect.y);
        }
        if ((rect.x + rect.width) > image.getWidth()) {
            rect.width = image.getWidth() - rect.x;
        }
        if ((rect.y + rect.height) > image.getHeight()) {
            rect.height = image.getHeight() - rect.y;
        }
        return image.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }
}
