package org.jhaws.common.pool;

import java.util.concurrent.Callable;

public class Job<M> implements Callable<M> {
	private M meta;

	private Runnable runnable;

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
		return "Job:meta[" + meta + "]";
	}

	public M getMeta() {
		return meta;
	}

	protected void setMeta(M meta) {
		this.meta = meta;
	}
}