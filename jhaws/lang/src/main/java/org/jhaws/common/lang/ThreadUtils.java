package org.jhaws.common.lang;

/**
 * @see org.jhaws.common.pool.RunnableThread
 * @see org.jhaws.common.lang.RunUntil
 * @see org.jhaws.common.lang.RunIndefinitely
 * @see org.jhaws.common.pool.*
 */
public class ThreadUtils {
    static public void stopOnInterrupt() throws InterruptedRuntimeException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedRuntimeException();
        }
    }

    static public void sleepStopOnInterrupt(long duration) throws InterruptedRuntimeException {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
            throw new InterruptedRuntimeException(ex);
        }
    }
}
