package org.jhaws.common.pool;

public abstract class PoolWhenDoneListener<M> extends PoolListenerAdapter<M> {
    @Override
    public void ended(Pool<M> pool, Job<M> job) {
        if (pool.getQueued().isEmpty()) {
            whenDone(pool);
        }
    }

    abstract public void whenDone(Pool<M> pool);
}
