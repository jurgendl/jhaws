package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

/**
 * @see http://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html
 * 
 * @author Jurgen
 */
public class ESpinnerCyclingModel<T> extends SpinnerListModel {
    private static final long serialVersionUID = -6273825685200303306L;

    protected T firstValue, lastValue;

    protected SpinnerModel linkedModel = null;

    protected List<ESpinnerCyclingModelListener> listeners = new ArrayList<ESpinnerCyclingModelListener>();

    public ESpinnerCyclingModel(List<T> values) {
        super(values);
        this.firstValue = values.get(0);
        this.lastValue = values.get(values.size() - 1);
    }

    public void addCyclingSpinnerListModelListener(ESpinnerCyclingModelListener l) {
        this.listeners.add(l);
    }

    public void clearCyclingSpinnerListModelListener() {
        this.listeners.clear();
    }

    @Override
    public Object getNextValue() {
        @SuppressWarnings("unchecked")
        T value = (T) super.getNextValue();
        if (value == null) {
            value = this.firstValue;
            if (this.linkedModel != null) {
                this.linkedModel.setValue(this.linkedModel.getNextValue());
            }
            for (ESpinnerCyclingModelListener l : this.listeners) {
                l.overflow();
            }
        }
        return value;
    }

    @Override
    public Object getPreviousValue() {
        @SuppressWarnings("unchecked")
        T value = (T) super.getPreviousValue();
        if (value == null) {
            value = this.lastValue;
            if (this.linkedModel != null) {
                this.linkedModel.setValue(this.linkedModel.getPreviousValue());
            }
            for (ESpinnerCyclingModelListener l : this.listeners) {
                l.rollback();
            }
        }
        return value;
    }

    public void removeCyclingSpinnerListModelListener(ESpinnerCyclingModelListener l) {
        this.listeners.remove(l);
    }

    public void setLinkedModel(SpinnerModel linkedModel) {
        this.linkedModel = linkedModel;
    }
}
