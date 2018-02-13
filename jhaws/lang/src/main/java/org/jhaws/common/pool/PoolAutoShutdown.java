package org.jhaws.common.pool;

public class PoolAutoShutdown<M> extends PoolListenerAdapter<M> {
    @Override
    public void ended(Pool<M> pool, Job<M> job) {
        if (pool.getQueued().isEmpty()) {
            pool.shutdown();
        }
    }
}
