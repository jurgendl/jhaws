package org.jhaws.common.pool;

import java.util.List;
import java.util.concurrent.FutureTask;

public class Task<M> extends FutureTask<M> {
	private final List<Job<M>> completion;

	private final Job<M> callable;

	public Task(List<Job<M>> completion, Job<M> callable) {
		super(callable);
		this.callable = callable;
		this.completion = completion;
	}

	@Override
	protected void done() {
		completion.add(callable);
	}

	public Job<M> getCallable() {
		return callable;
	}

	@Override
	public String toString() {
		return "Task:Job:meta[" + callable.getMeta() + "]";
	}
}