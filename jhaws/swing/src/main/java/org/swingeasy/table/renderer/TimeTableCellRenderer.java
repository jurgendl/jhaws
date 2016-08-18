package org.swingeasy.table.renderer;

import org.swingeasy.DateTimeType;

/**
 * @author Jurgen
 */
public class TimeTableCellRenderer extends DateTimeTableCellRenderer {
    private static final long serialVersionUID = -5300060082311348003L;

    public TimeTableCellRenderer() {
        super(DateTimeType.TIME);
    }
}
