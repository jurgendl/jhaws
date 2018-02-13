package org.jhaws.common.pool;

public class MetaPool extends Pool<PoolMeta> {
    public MetaPool(String name) {
        super(name);
    }

    @Override
    public Task<PoolMeta> addJob(Runnable runnable) {
        return super.addJob(runnable, new PoolMeta());
    }

    @Override
    protected void whenQueued(Job<PoolMeta> job) {
        job.getMeta().enqueued = System.currentTimeMillis();
        super.whenQueued(job);
    }

    @Override
    protected void beforeExecute(Job<PoolMeta> job) {
        job.getMeta().started = System.currentTimeMillis();
        super.beforeExecute(job);
    }

    @Override
    protected void afterExecute(Job<PoolMeta> job) {
        super.afterExecute(job);
        job.getMeta().completed = System.currentTimeMillis();
    }
}
