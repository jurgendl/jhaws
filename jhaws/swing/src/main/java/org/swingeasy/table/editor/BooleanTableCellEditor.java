package org.swingeasy.table.editor;

import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

import org.swingeasy.EComponentI;

/**
 * @author Jurgen
 */
public class BooleanTableCellEditor extends DefaultCellEditor implements EComponentI {
    
    private static final long serialVersionUID = -1148800983303008114L;

    public BooleanTableCellEditor() {
        super(new JCheckBox());
        JCheckBox checkBox = (JCheckBox) this.getComponent();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * 
     * @see org.swingeasy.EComponentI#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean b) {
        //
    }

    /**
     * 
     * @see org.swingeasy.EComponentI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        //
    }
}