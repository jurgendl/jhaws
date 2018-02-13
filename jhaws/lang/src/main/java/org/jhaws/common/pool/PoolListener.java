package org.jhaws.common.pool;

public interface PoolListener<M> {
    void shutdown(Pool<M> pool);

    void queued(Pool<M> pool, Job<M> job);

    void started(Pool<M> pool, Job<M> job);

    void ended(Pool<M> pool, Job<M> job);
}
