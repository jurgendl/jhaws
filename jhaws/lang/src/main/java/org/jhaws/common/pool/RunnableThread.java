package org.jhaws.common.pool;

import org.jhaws.common.lang.InterruptedRuntimeException;
import org.jhaws.common.lang.functions.ERunnable;

public class RunnableThread implements Runnable {
    private final ERunnable delegate;

    private Thread thread;

    // private boolean interrupted;

    public RunnableThread(ERunnable run) {
        delegate = run;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            // interrupted = true;
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            // while (!interrupted) {
            delegate.run();
            Thread.sleep(1);
            // if (Thread.currentThread().isInterrupted()) {
            // break;
            // }
            // }
        } catch (InterruptedException ex) {
            // interrupted = true;
        } catch (InterruptedRuntimeException ex) {
            // interrupted = true;
        }
    }
}
