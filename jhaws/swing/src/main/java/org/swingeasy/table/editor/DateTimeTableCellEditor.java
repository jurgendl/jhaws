package org.swingeasy.table.editor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.swingeasy.DateTimeType;
import org.swingeasy.EComponentI;

// javax.swing.text.DateFormatter
/**
 * @author Jurgen
 */
public class DateTimeTableCellEditor extends DefaultCellEditor implements EComponentI {
    private static final long serialVersionUID = 5169127745067354714L;

    protected DateFormat formatter;

    protected DateTimeType type;

    public DateTimeTableCellEditor() {
        this(DateTimeType.DATE_TIME);
    }

    public DateTimeTableCellEditor(DateTimeType type) {
        super(new JTextField());

        final JTextField jtf = JTextField.class.cast(getComponent());
        jtf.setBorder(null);
        jtf.removeActionListener(delegate);
        delegate = new EditorDelegate() {
            private static final long serialVersionUID = 6553117639786915624L;

            @Override
            public Object getCellEditorValue() {
                if (jtf.getText().equals("")) { //$NON-NLS-1$
                    return null;
                }
                try {
                    return formatter.parseObject(jtf.getText());
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }

            /**
             * 
             * @see javax.swing.DefaultCellEditor.EditorDelegate#setValue(java.lang.Object)
             */
            @Override
            public void setValue(Object value) {
                jtf.setText(value != null ? formatter.format(value) : ""); //$NON-NLS-1$
            }
        };
        jtf.addActionListener(delegate);

        this.type = type;
        newFormatter();
    }

    public Locale getLocale() {
        return getComponent().getLocale();
    }

    protected void newFormatter() {
        switch (type) {
            case DATE:
                formatter = DateFormat.getDateInstance(DateFormat.SHORT, getLocale());
                break;
            case DATE_TIME:
                formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, getLocale());
                break;
            case TIME:
                formatter = DateFormat.getTimeInstance(DateFormat.SHORT, getLocale());
                break;
        }
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
        getComponent().setLocale(l);
        newFormatter();
    }

}