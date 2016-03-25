package org.swingeasy;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Use a JTable as a renderer for row numbers of a given main table. This table must be added to the row header of the scrollpane that contains the
 * main table.
 * 
 * @see http://tips4java.wordpress.com/2008/11/18/row-number-table/
 */
public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener {
    private static final long serialVersionUID = 1854035830771958284L;

    protected JTable main;

    public RowNumberTable(JTable table) {
        this(table, 4);
    }

    protected RowNumberTable(JTable table, double w) {
        this.main = table;
        this.main.addPropertyChangeListener(this);

        this.setFocusable(false);
        this.setAutoCreateColumnsFromModel(false);
        this.setModel(this.main.getModel());
        this.setSelectionModel(this.main.getSelectionModel());

        TableColumn column = new TableColumn();
        column.setHeaderValue(" "); //$NON-NLS-1$
        this.addColumn(column);

        TableCellRenderer renderer = this.getTableHeader().getDefaultRenderer();
        if (renderer instanceof JLabel) {
            JLabel.class.cast(renderer).setHorizontalAlignment(SwingConstants.RIGHT);
        }
        column.setCellRenderer(renderer);

        this.getColumnModel().getColumn(0).setPreferredWidth((int) w);
        this.setPreferredScrollableViewportSize(this.getPreferredSize());
    }

    public RowNumberTable(JTable table, int chars) {
        this(table, new JTextField(chars).getPreferredSize().getWidth());
    }

    /**
     * 
     * @see javax.swing.JTable#addNotify()
     */
    @Override
    public void addNotify() {
        super.addNotify();

        Component c = this.getParent();

        // Keep scrolling of the row table in sync with the main table.

        if (c instanceof JViewport) {
            JViewport viewport = (JViewport) c;
            viewport.addChangeListener(this);
        }
    }

    /**
     * Delegate method to main table
     */
    @Override
    public int getRowCount() {
        return this.main.getRowCount();
    }

    /**
     * 
     * @see javax.swing.JTable#getRowHeight(int)
     */
    @Override
    public int getRowHeight(int row) {
        return this.main.getRowHeight(row);
    }

    /**
     * This table does not use any data from the main TableModel, so just return a value based on the row parameter.
     */
    @Override
    public Object getValueAt(int row, int column) {
        return Integer.toString(row + 1);
    }

    /**
     * Don't edit data in the main TableModel by mistake
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Implement the PropertyChangeListener
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // Keep the row table in sync with the main table

        if ("selectionModel".equals(e.getPropertyName())) { //$NON-NLS-1$
            this.setSelectionModel(this.main.getSelectionModel());
        }

        if ("model".equals(e.getPropertyName())) { //$NON-NLS-1$
            this.setModel(this.main.getModel());
        }
    }

    /**
     ** Implement the ChangeListener
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        // Keep the scrolling of the row table in sync with main table

        JViewport viewport = (JViewport) e.getSource();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
}
