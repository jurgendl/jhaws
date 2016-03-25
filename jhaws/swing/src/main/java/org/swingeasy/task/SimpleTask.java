package org.swingeasy.task;

/**
 * @author Jurgen
 */
public abstract class SimpleTask extends Task<Void> {
    /**
     * 
     * @see be.ugent.oasis.swing.ui.gen2.Task#doInBackground()
     */
    @Override
    final protected Void doInBackground() throws Exception {
        this.doInBackgroundNrv();
        return null;
    }

    abstract protected void doInBackgroundNrv() throws Exception;

    abstract protected void done(Throwable cause);

    /**
     * 
     * @see be.ugent.oasis.swing.ui.gen2.Task#done(java.lang.Object, java.lang.Throwable)
     */
    @Override
    final protected void done(Void returnValue, Throwable cause) {
        this.done(cause);
    }
}
