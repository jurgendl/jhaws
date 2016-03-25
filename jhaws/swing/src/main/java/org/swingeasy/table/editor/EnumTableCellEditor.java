package org.swingeasy.table.editor;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import org.swingeasy.EComponentI;

/**
 * @author Jurgen
 */
public class EnumTableCellEditor<T extends Enum<T>> extends DefaultCellEditor implements EComponentI {
    private static <T extends Enum<T>> Vector<T> options(Class<T> enumType) {
        Vector<T> options = new Vector<T>();
        for (T option : EnumSet.allOf(enumType)) {
            options.add(option);
        }
        return options;
    }

    private static final long serialVersionUID = 5169127745067354714L;

    protected Class<T> enumType;

    public EnumTableCellEditor(Class<T> enumType) {
        super(new JComboBox<T>(EnumTableCellEditor.options(enumType)));
        this.enumType = enumType;
    }

    public Locale getLocale() {
        return this.getComponent().getLocale();
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
        this.getComponent().setLocale(l);
    }

}