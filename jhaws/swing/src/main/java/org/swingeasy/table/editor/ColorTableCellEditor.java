package org.swingeasy.table.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.swingeasy.EComponentI;
import org.swingeasy.Messages;

/**
 * @author Jurgen
 */
public class ColorTableCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener, EComponentI {

    private static final long serialVersionUID = 819809458892249679L;

    protected Locale locale;

    protected Color currentColor;

    protected JButton button;

    protected JColorChooser colorChooser;

    protected JDialog dialog;

    protected static final String EDIT = "edit"; //$NON-NLS-1$

    public ColorTableCellEditor() {
        // Set up the editor (from the table's point of view),
        // which is a button.
        // This button brings up the color chooser dialog,
        // which is the editor from the user's point of view.
        this.button = new JButton();
        this.button.setActionCommand(ColorTableCellEditor.EDIT);
        this.button.addActionListener(this);

        this.button.setBorder(null);
        this.button.setBorderPainted(false);
        this.button.setOpaque(false);
        this.button.setContentAreaFilled(true);
        this.button.setFocusable(false);
    }

    /**
     * Handles events from the editor button and from the dialog's OK button.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (ColorTableCellEditor.EDIT.equals(e.getActionCommand())) {
            // The user has clicked the cell, so
            // bring up the dialog.

            this.button.setBackground(this.currentColor);
            this.button.setForeground(this.currentColor);

            this.getColorChooser().setColor(this.currentColor);
            this.getDialog().setVisible(true);

            // Make the renderer reappear.
            this.fireEditingStopped();
        } else { // User pressed dialog's "OK" button.
            this.currentColor = this.getColorChooser().getColor();
        }
    }

    /**
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return this.currentColor;
    }

    public JColorChooser getColorChooser() {
        if (this.colorChooser == null) {
            this.colorChooser = new JColorChooser();

        }
        this.colorChooser.setLocale(this.locale);
        return this.colorChooser;
    }

    public JDialog getDialog() {
        if (this.dialog == null) {
            this.dialog = JColorChooser.createDialog(this.button,// parent
                    Messages.getString((Locale) null, "ColorTableCellEditor.pickAColor"),// title
                    true, // modal
                    this.getColorChooser(), this, // OK button handler
                    null); // no CANCEL button handler
        }
        this.dialog.setLocale(this.locale);
        return this.dialog;
    }

    /**
     * 
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.currentColor = (Color) value;
        return this.button;
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
        this.locale = l;
        this.dialog = null;
        this.colorChooser = null;
    }
}
