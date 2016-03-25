package org.swingeasy.table.renderer;

import java.util.Locale;

import org.swingeasy.UIUtils;

/**
 * @author Jurgen
 */
public class ByteArrayTableCellRenderer extends ETableCellRenderer<Object> {
    private static final long serialVersionUID = 393779263932701309L;

    public ByteArrayTableCellRenderer() {
        super();
    }

    /**
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
    }

    /**
     * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    @Override
    protected void setValue(Object value) {
        String text;
        if (value instanceof byte[]) {
            byte[] ba = (byte[]) value;
            text = "0x" + UIUtils.bytesToHex(ba);
        } else if (value instanceof Byte[]) {
            Byte[] ba = (Byte[]) value;
            text = "0x" + UIUtils.bytesToHex(ba);
        } else {
            text = (value == null) ? "" : String.valueOf(value);
        }
        this.setText(text);
        this.setToolTipText(text);
    }
}