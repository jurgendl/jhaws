package org.jhaws.common.pool;

import java.util.concurrent.FutureTask;

public class Task<M> extends FutureTask<M> {
    private final Job<M> callable;

    public Task(Job<M> callable) {
        super(callable);
        this.callable = callable;
    }

    @Override
    protected void done() {
        //
    }

    public Job<M> getCallable() {
        return callable;
    }

    @Override
    public String toString() {
        return "Task:Job:meta[" + callable.getMeta() + "]";
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancel = super.cancel(mayInterruptIfRunning);
        return cancel;
    }

    @Override
    protected void set(M v) {
        super.set(v);
        if (isCancelled()) {
            callable.setState(JobState.CANCELLED);
        } else {
            callable.setState(JobState.DONE);
        }
    }

    @Override
    protected void setException(Throwable t) {
        super.setException(t);
        callable.setState(JobState.ERROR);
    }
}