package org.jhaws.common.pool;

public class PoolAutoShutdownListener<M> extends PoolWhenDoneListener<M> {
    @Override
    public void whenDone(Pool<M> pool) {
        pool.shutdown();
    }
}
