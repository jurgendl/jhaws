package org.swingeasy;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 * Handles drag & drop row reordering
 *
 * @see http://stackoverflow.com/questions/638807/how-do-i-drag-and-drop-a-row-in-a-jtable
 */
public class TableRowTransferHandler extends TransferHandler {
    private static final long serialVersionUID = -7424691675591737713L;

    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Integer.class, DataFlavor.javaJVMLocalObjectMimeType, "Integer Row Index"); //$NON-NLS-1$

    private JTable table = null;

    public TableRowTransferHandler(JTable table) {
        this.table = table;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        return b;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        assert c == table;
        return new DataHandler(Integer.valueOf(table.getSelectedRow()), localObjectFlavor.getMimeType());
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act) {
        if (act == TransferHandler.MOVE) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        JTable target = (JTable) info.getComponent();
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int index = dl.getRow();
        int max = table.getModel().getRowCount();
        if (index < 0 || index > max) {
            index = max;
        }
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        try {
            Integer rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
            if (rowFrom != -1 && rowFrom != index) {
                if (table instanceof Reorderable) {
                    ((Reorderable) table).reorder(rowFrom, index);
                }
                if (table.getModel() instanceof Reorderable) {
                    ((Reorderable) table.getModel()).reorder(rowFrom, index);
                }
                if (index > rowFrom) {
                    index--;
                }
                target.getSelectionModel().addSelectionInterval(index, index);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
