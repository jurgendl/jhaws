package org.jhaws.common.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Pool<M> {
	public static class Job<M> implements Callable<M> {
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

	public static class Task<M> extends FutureTask<M> {
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
			return "Task:Job:meta[" + callable.meta + "]";
		}
	}

	static final protected AtomicInteger id = new AtomicInteger();
	protected ThreadFactory threadFactory;
	protected List<Job<M>> current;
	protected List<Job<M>> completed;
	protected Queue<Task<M>> queue;
	protected ThreadPoolExecutor threadPoolExecutor;
	protected int nr = 2;
	protected int priority = 5;
	protected boolean daemon = true;
	protected String name;

	public Pool(String name) {
		this.name = name;
		this.nr = Runtime.getRuntime().availableProcessors();
	}

	public Task<M> addJob(Runnable runnable) {
		return addJob(runnable, null);
	}

	public Task<M> addJob(Runnable runnable, M meta) {
		return (Task<M>) getThreadPoolExecutor().submit(new Job<>(runnable, meta));
	}

	public List<Job<M>> getCurrent() {
		if (current == null) {
			current = Collections.synchronizedList(new ArrayList<Job<M>>());
		}
		return current;
	}

	public List<Job<M>> getCompleted() {
		if (completed == null) {
			completed = Collections.synchronizedList(new ArrayList<Job<M>>());
		}
		return completed;
	}

	public Queue<Task<M>> getQueue() {
		if (queue == null) {
			queue = new LinkedBlockingQueue<>();
		}
		return queue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ThreadPoolExecutor getThreadPoolExecutor() {
		if (threadPoolExecutor == null) {
			threadPoolExecutor = new ThreadPoolExecutor(nr, nr, 0L, TimeUnit.MILLISECONDS, (BlockingQueue) getQueue(), getThreadFactory()) {
				@Override
				protected <U> FutureTask<U> newTaskFor(Callable<U> callable) {
					return new Task(getCompleted(), Job.class.cast(callable));
				}

				@Override
				protected <U> FutureTask<U> newTaskFor(Runnable runnable, U value) {
					throw new UnsupportedOperationException();
				}

				@Override
				protected void beforeExecute(Thread t, Runnable r) {
					Pool.this.beforeExecute(Task.class.cast(r).getCallable());
					super.beforeExecute(t, r);
				}

				@Override
				protected void afterExecute(Runnable r, Throwable t) {
					super.afterExecute(r, t);
					Pool.this.afterExecute(Task.class.cast(r).getCallable());
				}
			};
		}
		return threadPoolExecutor;
	}

	protected void beforeExecute(Job<M> job) {
		getCurrent().add(job);
	}

	protected void afterExecute(Job<M> job) {
		getCurrent().remove(job);
	}

	protected ThreadFactory getThreadFactory() {
		if (threadFactory == null) {
			threadFactory = r -> {
				Thread t = new Thread(r);
				t.setName(name + "-" + id.getAndIncrement());
				t.setDaemon(daemon);
				t.setPriority(priority);
				return t;
			};
		}
		return threadFactory;
	}

	public int getNr() {
		return nr;
	}

	public Pool<M> setNr(int nr) {
		this.nr = nr;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public Pool<M> setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public boolean getDaemon() {
		return daemon;
	}

	public Pool<M> setDaemon(boolean daemon) {
		this.daemon = daemon;
		return this;
	}

	public String getName() {
		return name;
	}

	public Pool<M> setName(String name) {
		this.name = name;
		return this;
	}
}
