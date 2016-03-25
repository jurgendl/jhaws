package org.swingeasy.task;

/**
 * @author Jurgen
 */
public abstract class SimpleEventThreadTask extends EventThreadTask<Void> {
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

    abstract protected void doOnEventThread() throws Exception;

    /**
     * 
     * @see be.ugent.oasis.swing.ui.gen2.EventThreadTask#doOnEventThread(java.lang.Object)
     */
    @Override
    final protected void doOnEventThread(Void returnValue) throws Exception {
        this.doOnEventThread();
    }

    /**
     * 
     * @see be.ugent.oasis.swing.ui.gen2.EventThreadTask#handleException(java.lang.Throwable)
     */
    @Override
    abstract protected void handleException(Throwable cause);
}
