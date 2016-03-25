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

        final JTextField jtf = JTextField.class.cast(this.getComponent());
        jtf.setBorder(null);
        jtf.removeActionListener(this.delegate);
        this.delegate = new EditorDelegate() {
            private static final long serialVersionUID = 6553117639786915624L;

            @Override
            public Object getCellEditorValue() {
                if (jtf.getText().equals("")) { //$NON-NLS-1$
                    return null;
                }
                try {
                    return DateTimeTableCellEditor.this.formatter.parseObject(jtf.getText());
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
                jtf.setText((value != null) ? DateTimeTableCellEditor.this.formatter.format(value) : ""); //$NON-NLS-1$
            }
        };
        jtf.addActionListener(this.delegate);

        this.type = type;
        this.newFormatter();
    }

    public Locale getLocale() {
        return this.getComponent().getLocale();
    }

    protected void newFormatter() {
        switch (this.type) {
            case DATE:
                this.formatter = DateFormat.getDateInstance(DateFormat.SHORT, this.getLocale());
                break;
            case DATE_TIME:
                this.formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, this.getLocale());
                break;
            case TIME:
                this.formatter = DateFormat.getTimeInstance(DateFormat.SHORT, this.getLocale());
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
        this.getComponent().setLocale(l);
        this.newFormatter();
    }

}