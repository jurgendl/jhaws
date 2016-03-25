package org.swingeasy.task;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 */
public abstract class EventThreadTask<T> extends Task<T> {
    public static void runNowOnEventQueue(Runnable taak) {
        if (EventQueue.isDispatchThread()) {
            taak.run();
        } else {
            try {
                EventQueue.invokeAndWait(taak);
            } catch (InterruptedException ex) {
                //
            } catch (InvocationTargetException ex) {
                if (ex.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) ex.getCause();
                }
                throw new RuntimeException(ex.getCause());
            }
        }
    }

    /**
     * 
     * @see be.ugent.oasis.swing.ui.gen2.Task#done(java.lang.Object, java.lang.Throwable)
     */
    @Override
    final protected void done(final T returnValue, Throwable cause) {
        if (cause != null) {
            this.handleException(cause);
            return;
        }

        if (SwingUtilities.isEventDispatchThread()) {
            try {
                this.doOnEventThread(returnValue);
            } catch (Exception ex) {
                this.handleException(ex);
            }
        } else {
            try {
                EventThreadTask.runNowOnEventQueue(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EventThreadTask.this.doOnEventThread(returnValue);
                        } catch (Exception ex) {
                            EventThreadTask.this.handleException(ex);
                        }
                    }
                });
            } catch (Exception ex) {
                this.handleException(ex);
            }
        }
    }

    abstract protected void doOnEventThread(T returnValue) throws Exception;

    abstract protected void handleException(Throwable cause);
}