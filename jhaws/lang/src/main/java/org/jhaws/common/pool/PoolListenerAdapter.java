package org.jhaws.common.pool;

public class PoolListenerAdapter<M> implements PoolListener<M> {
    @Override
    public void shutdown(Pool<M> pool) {
        //
    }

    @Override
    public void queued(Pool<M> pool, Job<M> job) {
        //
    }

    @Override
    public void started(Pool<M> pool, Job<M> job) {
        //
    }

    @Override
    public void ended(Pool<M> pool, Job<M> job) {
        //
    }
}
