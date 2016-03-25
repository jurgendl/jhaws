package org.swingeasy;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jurgen
 */
public class DelegatingTransferable implements Transferable {
    List<Transferable> sources = new ArrayList<Transferable>();

    public DelegatingTransferable(Transferable... sources) {
        this.sources.addAll(Arrays.asList(sources));
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        for (Transferable t : this.sources) {
            if (t.isDataFlavorSupported(flavor)) {
                return t.getTransferData(flavor);
            }
        }
        return null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        List<DataFlavor> l = new ArrayList<DataFlavor>();
        for (Transferable t : this.sources) {
            for (DataFlavor f : t.getTransferDataFlavors()) {
                l.add(f);
            }
        }
        return l.toArray(new DataFlavor[0]);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (Transferable t : this.sources) {
            if (t.isDataFlavorSupported(flavor)) {
                return true;
            }
        }
        return false;
    }
}