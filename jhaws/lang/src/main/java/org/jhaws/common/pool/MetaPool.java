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
	public org.jhaws.common.pool.Pool.Task<PoolMeta> addJob(Runnable runnable, PoolMeta meta) {
		meta.enqueued = System.currentTimeMillis();
		return super.addJob(runnable, meta);
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
