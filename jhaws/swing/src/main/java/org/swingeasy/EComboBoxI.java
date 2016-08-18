package org.swingeasy;

import java.util.Collection;
import java.util.List;

/**
 * @author Jurgen
 */
public interface EComboBoxI<T> extends EComponentI {
    public abstract void activateAutoCompletion();

    public abstract void activateScrolling();

    public abstract void addRecord(EComboBoxRecord<T> eComboBoxRecord);

    public abstract void addRecords(Collection<EComboBoxRecord<T>> eComboBoxRecords);

    public abstract void deactivateScrolling();

    public abstract List<EComboBoxRecord<T>> getRecords();

    public abstract EComboBoxRecord<T> getSelectedRecord();

    public abstract void removeAllRecords();

    public abstract void removeRecord(final EComboBoxRecord<T> record);

    public abstract void setSelectedRecord(EComboBoxRecord<T> record);
}
