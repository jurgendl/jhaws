package org.jhaws.common.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Pool<M> {
	static final protected AtomicInteger id = new AtomicInteger();
	protected ThreadFactory threadFactory;
	protected List<Job<M>> current;
	protected List<Job<M>> completed;
	protected BlockingQueue<Task<M>> queue;
	protected ThreadPoolExecutor threadPoolExecutor;
	protected int nr = 2;
	protected int priority = 5;
	protected boolean daemon = true;
	protected String name;

	public Pool(String name) {
		this(name, Runtime.getRuntime().availableProcessors());
	}

	public Pool(String name, int nr) {
		this.name = name;
		this.nr = nr;
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

	public BlockingQueue<Task<M>> getQueue() {
		if (queue == null) {
			queue = new LinkedBlockingQueue<>();
		}
		return queue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "cast" })
	public synchronized void afterPropertiesSet() {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		threadPoolExecutor = new ThreadPoolExecutor(nr, nr, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) (BlockingQueue) getQueue(), getThreadFactory()) {
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

	protected synchronized ThreadPoolExecutor getThreadPoolExecutor() {
		if (threadPoolExecutor == null) {
			afterPropertiesSet();
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
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.nr = nr;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public Pool<M> setPriority(int priority) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.priority = priority;
		return this;
	}

	public boolean getDaemon() {
		return daemon;
	}

	public Pool<M> setDaemon(boolean daemon) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.daemon = daemon;
		return this;
	}

	public String getName() {
		return name;
	}

	public Pool<M> setName(String name) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.name = name;
		return this;
	}
}
