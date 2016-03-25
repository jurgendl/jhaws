package org.swingeasy;

import java.awt.Point;
import java.util.Collection;
import java.util.List;

/**
 * @author Jurgen
 */
public interface ETreeTableI<T> extends EComponentI {
    public abstract void addRecord(final ETreeTableRecord<T> record);

    public abstract void addRecords(final Collection<ETreeTableRecord<T>> records);

    public void clear();

    public Object getColumnValueAtVisualColumn(int i);

    public abstract List<String> getHeadernames();

    public abstract ETreeTableRecord<T> getRecordAtVisualRow(int i);

    public abstract List<ETreeTableRecord<T>> getRecords();

    public abstract Object getSelectedCell();

    public abstract List<Object> getSelectedCells();

    /**
     * gets selected record (first one if multiple ones are selected
     */
    public abstract ETreeTableRecord<T> getSelectedRecord();

    /**
     * gets selected record (first one if multiple ones are selected
     */
    public abstract List<ETreeTableRecord<T>> getSelectedRecords();

    public abstract void packColumn(int vColIndex);

    public abstract void packColumn(int vColIndex, int margin);

    public abstract void removeAllRecords();

    public abstract void removeRecord(final ETableRecord<T> record);

    public abstract void removeRecordAtVisualRow(final int i);

    public abstract void scrollToVisibleRecord(final ETableRecord<T> record);

    public abstract void selectCell(Point p);
}
