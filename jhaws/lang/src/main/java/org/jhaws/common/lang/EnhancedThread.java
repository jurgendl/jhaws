package org.jhaws.common.lang;

import java.util.function.Consumer;

import org.jhaws.common.lang.functions.ERunnable;
import org.jhaws.common.lang.functions.ESupplier;

public class EnhancedThread extends Thread {
    public static Runnable convert(ESupplier<Long> runnable, Consumer<Exception> exceptionHandler) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    long delay;
                    do {
                        delay = runnable.get();
                        if (delay > 0) {
                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException ex) {
                                //
                            }
                        }
                    } while (delay >= 0);
                } catch (Exception ex) {
                    if (exceptionHandler != null) {
                        exceptionHandler.accept(ex);
                    }
                }
            }
        };
    }

    public static ESupplier<Long> convert(ERunnable runnable, Long delay) {
        return () -> {
            runnable.run();
            return delay;
        };
    }

    public EnhancedThread(boolean deamon, ESupplier<Long> runnable, Consumer<Exception> exceptionHandler) {
        super(convert(runnable, exceptionHandler));
        setDaemon(deamon);
    }

    public EnhancedThread(boolean deamon, ESupplier<Long> runnable, Consumer<Exception> exceptionHandler, String id) {
        super(convert(runnable, exceptionHandler), id);
        setDaemon(deamon);
    }

    public EnhancedThread(boolean deamon, ERunnable runnable, Long delay, Consumer<Exception> exceptionHandler) {
        super(convert(convert(runnable, delay), exceptionHandler));
        setDaemon(deamon);
    }

    public EnhancedThread(boolean deamon, ERunnable runnable, Long delay, Consumer<Exception> exceptionHandler, String id) {
        super(convert(convert(runnable, delay), exceptionHandler), id);
        setDaemon(deamon);
    }
}
