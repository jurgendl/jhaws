package org.swingeasy;

/**
 * table.setDragEnabled(true);<br/>
 * table.setDropMode(DropMode.INSERT_ROWS);<br/>
 * table.setTransferHandler(new TableRowTransferHandler(table));<br/>
 * 
 * @see http://stackoverflow.com/questions/638807/how-do-i-drag-and-drop-a-row-in-a-jtable
 */
public interface Reorderable {
    public void reorder(int fromIndex, int toIndex);
}
