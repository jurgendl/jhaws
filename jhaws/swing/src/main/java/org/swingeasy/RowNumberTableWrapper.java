package org.swingeasy;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

/**
 * Use a JTable as a renderer for row numbers of a given main table. This table must be added to the row header of the scrollpane that contains the
 * main table.
 * 
 * @see http://tips4java.wordpress.com/2008/11/18/row-number-table/
 */
public class RowNumberTableWrapper extends JScrollPane {
    private static final long serialVersionUID = 178175335896020429L;

    public RowNumberTableWrapper(JTable wrapped) {
        super(wrapped);
        RowNumberTable rowTable = new RowNumberTable(wrapped);
        this.setRowHeaderView(rowTable);
        this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowTable.getTableHeader());
    }
}
