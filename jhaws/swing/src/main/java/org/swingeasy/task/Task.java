package org.swingeasy.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jurgen
 */
public abstract class Task<T> {
    public static enum TaskState {
        STARTED, DONE, WAITING;
    }

    static final private AtomicInteger threadNumber = new AtomicInteger(1);

    private static ExecutorService threadPool;

    /**
     * geeft threadpool terug of creeert met defaults
     */
    private static ExecutorService getThreadPool() {
        if (Task.threadPool == null) {
            final int poolSize = 10;
            return Task.getThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(null, r, "Task-" + Task.threadNumber.getAndIncrement(), 0);
                    t.setDaemon(true);
                    t.setPriority(Thread.NORM_PRIORITY);
                    // t.setUncaughtExceptionHandler(eh); // kunnen we hier iets met aanvangen of overbodig
                    return t;
                }
            }, poolSize);
        }
        return Task.threadPool;
    }

    /**
     * configureerbare threadpool
     */
    private static synchronized ExecutorService getThreadPool(final ThreadFactory factory, final int size) {
        if (Task.threadPool == null) {
            Task.threadPool = Executors.newFixedThreadPool(size, factory);
            // zelfde settings als SwingWorker: TTL 10 minuten
            ThreadPoolExecutor.class.cast(Task.threadPool).setKeepAliveTime(10, TimeUnit.MINUTES);
        }
        return Task.threadPool;
    }

    private FutureTask<T> future;

    private TaskState state;

    private Throwable exception;

    public Task() {
        Callable<T> callable = new Callable<T>() {
            @Override
            public T call() throws Exception {
                Task.this.setState(TaskState.STARTED);
                return Task.this.doInBackground();
            }
        };

        this.future = new FutureTask<T>(callable) {
            @Override
            protected void done() {
                Task.this.done();
                Task.this.setState(TaskState.DONE);
            }

            @Override
            protected void setException(Throwable t) {
                Task.this.exception = t;
                super.setException(t);
            }
        };

        this.setState(TaskState.WAITING);
    }

    final public boolean cancel(boolean mayInterruptIfRunning) {
        return this.future.cancel(mayInterruptIfRunning);
    }

    /**
     * override deze methode om in aparte thread op achtergrond uit te voeren
     * 
     * @return
     * @throws Exception
     */
    abstract protected T doInBackground() throws Exception;

    final protected void done() {
        try {
            this.done(this.get(), null);
        } catch (InterruptedException ex) {
            this.done(null, ex);
        } catch (ExecutionException ex) {
            this.done(null, ex.getCause());
        }
    }

    /**
     * override deze methode om te reageren op het aflopen van een taak, normaal en door exception
     * 
     * @param returnValue
     * @param cause
     */
    abstract protected void done(T returnValue, Throwable cause);

    final public void execute() {
        Task.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Task.this.future.run();
            }
        });
    }

    final public T get() throws InterruptedException, ExecutionException {
        return this.future.get();
    }

    final public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.future.get(timeout, unit);
    }

    final public Throwable getException() {
        return this.exception;
    }

    final public TaskState getState() {
        return this.state;
    }

    final public boolean isCancelled() {
        return this.future.isCancelled();
    }

    final public boolean isDone() {
        return this.future.isDone();
    }

    final private void setState(TaskState state) {
        this.state = state;
    }

}
