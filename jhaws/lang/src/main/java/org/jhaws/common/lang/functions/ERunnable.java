package org.jhaws.common.lang.functions;

import org.jhaws.common.lang.InterruptedRuntimeException;
import org.jhaws.common.lang.RuntimeWrappedException;

@FunctionalInterface
public interface ERunnable extends SRunnable {
    public static Runnable enhance(ERunnable runnable) {
        return runnable::run;
    }

    @Override
    default void run() {
        try {
            runEnhanced();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (InterruptedException ex) {
            throw new InterruptedRuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeWrappedException(ex);
        }
    }

    void runEnhanced() throws Exception;

    default ERunnable and(ERunnable run) {
        ERunnable tmp = () -> {
            this.run();
            run.run();
        };
        return tmp;
    }
}