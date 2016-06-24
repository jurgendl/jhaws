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
		button = new JButton();
		button.setActionCommand(ColorTableCellEditor.EDIT);
		button.addActionListener(this);

		button.setBorder(null);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(true);
		button.setFocusable(false);
	}

	/**
	 * Handles events from the editor button and from the dialog's OK button.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ColorTableCellEditor.EDIT.equals(e.getActionCommand())) {
			// The user has clicked the cell, so
			// bring up the dialog.

			button.setBackground(currentColor);
			button.setForeground(currentColor);

			getColorChooser().setColor(currentColor);
			getDialog().setVisible(true);

			// Make the renderer reappear.
			fireEditingStopped();
		} else { // User pressed dialog's "OK" button.
			currentColor = getColorChooser().getColor();
		}
	}

	/**
	 * 
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		return currentColor;
	}

	public JColorChooser getColorChooser() {
		if (colorChooser == null) {
			colorChooser = new JColorChooser();

		}
		colorChooser.setLocale(locale);
		return colorChooser;
	}

	public JDialog getDialog() {
		if (dialog == null) {
			dialog = JColorChooser.createDialog(button, // parent
					Messages.getString((Locale) null, "ColorTableCellEditor.pickAColor"), // title
					true, // modal
					getColorChooser(), this, // OK button handler
					null); // no CANCEL button handler
		}
		dialog.setLocale(locale);
		return dialog;
	}

	/**
	 * 
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentColor = (Color) value;
		return button;
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
		locale = l;
		dialog = null;
		colorChooser = null;
	}
}
