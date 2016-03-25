package org.swingeasy.list.renderer;

import java.awt.Component;
import java.util.Locale;

import javax.swing.JList;

import org.swingeasy.UIUtils;

/**
 * @author Jurgen
 */
public class ByteArrayListCellRenderer extends EListCellRenderer<Object> {
    private static final long serialVersionUID = 393779263932701309L;

    public ByteArrayListCellRenderer() {
        super();
    }

    /**
     * @see org.swingeasy.list.renderer.EListCellRenderer#render(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    protected Component render(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.render(list, value, index, isSelected, cellHasFocus);
        String text;
        if (value instanceof byte[]) {
            byte[] ba = (byte[]) value;
            text = "0x" + UIUtils.bytesToHex(ba);
        } else if (value instanceof Byte[]) {
            Byte[] ba = (Byte[]) value;
            text = "0x" + UIUtils.bytesToHex(ba);
        } else {
            text = (value == null) ? "" : String.valueOf(value); //$NON-NLS-1$
        }
        this.setText(text);
        this.setToolTipText(text);
        return this;
    }

    /**
     *
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
    }
}