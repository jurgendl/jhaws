package org.jhaws.common.lang.functions;

@FunctionalInterface
public interface ERunnable extends SRunnable {
    public static Runnable enhance(ERunnable runnable) {
        return runnable::run;
    }

    @Override
    default void run() {
        try {
            runEnhanced();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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