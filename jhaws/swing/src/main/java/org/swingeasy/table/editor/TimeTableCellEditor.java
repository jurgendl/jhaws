package org.swingeasy.table.editor;

import org.swingeasy.DateTimeType;

/**
 * @author Jurgen
 */
public class TimeTableCellEditor extends DateTimeTableCellEditor {
    private static final long serialVersionUID = 7036312170850978563L;

    public TimeTableCellEditor() {
        super(DateTimeType.TIME);
    }
}
