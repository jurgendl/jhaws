package org.jhaws.common.pool;

import java.util.concurrent.Callable;

public class Job<M> implements Callable<M> {
    private M meta;

    private Runnable runnable;

    private Long queued;

    private Long started;

    private Long finished;

    public Job(Runnable runnable, M meta) {
        this.runnable = runnable;
        this.meta = meta;
    }

    @Override
    public M call() throws Exception {
        runnable.run();
        return meta;
    }

    @Override
    public String toString() {
        if (finished != null) return "Job:meta[" + (finished - started) + "][" + meta + "]";
        if (started != null) return "Job:meta[~" + (System.currentTimeMillis() - started) + "][" + meta + "]";
        return "Job:meta[" + meta + "]";
    }

    public M getMeta() {
        return meta;
    }

    protected void setMeta(M meta) {
        this.meta = meta;
    }

    public Long getStarted() {
        return this.started;
    }

    protected void setStarted(Long started) {
        this.started = started;
    }

    public Long getFinished() {
        return this.finished;
    }

    protected void setFinished(Long finished) {
        this.finished = finished;
    }

    public Long getQueued() {
        return this.queued;
    }

    protected void setQueued(Long queued) {
        this.queued = queued;
    }
}