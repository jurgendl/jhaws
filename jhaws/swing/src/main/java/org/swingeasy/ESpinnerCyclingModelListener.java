package org.swingeasy;

/**
 * @see http://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html
 * 
 * @author Jurgen
 */
public interface ESpinnerCyclingModelListener {
    public abstract void overflow();

    public abstract void rollback();
}