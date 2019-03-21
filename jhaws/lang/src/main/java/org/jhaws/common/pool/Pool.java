package org.jhaws.common.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Pool<M> {
	protected static final AtomicInteger id = new AtomicInteger();

	protected final PoolAutoShutdownListener<M> autoShutdown = new PoolAutoShutdownListener<>();

	protected ThreadFactory threadFactory;

	protected List<Job<M>> all;

	protected BlockingQueue<Task<M>> queue;

	protected ThreadPoolExecutor threadPoolExecutor;

	protected int size = 2;

	protected int priority = 5;

	protected boolean daemon = true;

	protected String name;

	protected final List<PoolListener<M>> listeners = new ArrayList<>();

	public Pool() {
		this(UUID.randomUUID().toString());
	}

	public Pool(String name) {
		this(name, Runtime.getRuntime().availableProcessors());
	}

	public Pool(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public Task<M> addJob(Runnable runnable) {
		return addJob(runnable, null);
	}

	public Task<M> addJob(Runnable runnable, M meta) {
		Job<M> job = new Job<>(runnable, meta);
		return addJob(job);
	}

	public Task<M> addJob(Job<M> job) {
		getAll().add(job);
		return (Task<M>) getThreadPoolExecutor().submit(job);
	}

	public List<Job<M>> getAll() {
		if (all == null) {
			all = Collections.synchronizedList(new ArrayList<Job<M>>());
		}
		return all;
	}

	protected BlockingQueue<Task<M>> getQueue() {
		if (queue == null) {
			queue = new LinkedBlockingQueue<>();
		}
		return queue;
	}

	public synchronized void afterPropertiesSet() {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		@SuppressWarnings("unchecked")
		BlockingQueue<Runnable> q = (BlockingQueue<Runnable>) (BlockingQueue<?>) getQueue();
		threadPoolExecutor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, q, getThreadFactory()) {
			@Override
			protected <U> FutureTask<U> newTaskFor(Callable<U> callable) {
				@SuppressWarnings("unchecked")
				Job<M> job1 = Job.class.cast(callable);
				whenQueued(job1);
				@SuppressWarnings("unchecked")
				Job<U> job2 = Job.class.cast(callable);
				return new Task<U>(job2);
			}

			@Override
			protected <U> FutureTask<U> newTaskFor(Runnable runnable, U value) {
				throw new UnsupportedOperationException();
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void beforeExecute(Thread t, Runnable r) {
				@SuppressWarnings("rawtypes")
				Task task = Task.class.cast(r);
				Pool.this.beforeExecute(task.getCallable());
				super.beforeExecute(t, r);
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				super.afterExecute(r, t);
				@SuppressWarnings("rawtypes")
				Task task = Task.class.cast(r);
				Pool.this.afterExecute(task.getCallable(), t);
				if (t != null) {
					t.printStackTrace();
				}
			}
		};
	}

	protected synchronized ThreadPoolExecutor getThreadPoolExecutor() {
		if (threadPoolExecutor == null) {
			afterPropertiesSet();
		}
		return threadPoolExecutor;
	}

	protected void whenQueued(Job<M> job) {
		job.setQueued(System.currentTimeMillis());
		job.setState(JobState.QUEUED);
		listeners.forEach(l -> l.queued(this, job));
	}

	protected void beforeExecute(Job<M> job) {
		job.setStarted(System.currentTimeMillis());
		job.setState(JobState.EXECUTING);
		listeners.forEach(l -> l.started(this, job));
	}

	protected void afterExecute(Job<M> job, Throwable error) {
		job.setFinished(System.currentTimeMillis());
		if (error != null) {
			job.setError(error);
			// job.setState(JobState.ERROR);
		} else {
			// job.setState(JobState.DONE);
		}
		listeners.forEach(l -> l.ended(this, job));
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

	public int getSize() {
		return size;
	}

	public Pool<M> size(int size) {
		setSize(size);
		return this;
	}

	public void setSize(int size) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.size = size;
	}

	public int getPriority() {
		return priority;
	}

	public Pool<M> priority(int priority) {
		setPriority(priority);
		return this;
	}

	public void setPriority(int priority) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.priority = priority;
	}

	public boolean getDaemon() {
		return daemon;
	}

	public void setDaemon(boolean daemon) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.daemon = daemon;
	}

	public Pool<M> daemon(boolean daemon) {
		setDaemon(daemon);
		return this;
	}

	public String getName() {
		return name;
	}

	public Pool<M> name(String name) {
		setName(name);
		return this;
	}

	public void setName(String name) {
		if (threadPoolExecutor != null)
			throw new UnsupportedOperationException("past creation");
		this.name = name;
	}

	public void shutdown() {
		threadPoolExecutor.shutdown();
		listeners.forEach(l -> l.shutdown(this));
	}

	public void addPoolListener(PoolListener<M> listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removePoolListener(PoolListener<M> listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}

	public void removeAllPoolListener() {
		listeners.clear();
	}

	public List<PoolListener<M>> getPoolListeners() {
		return Collections.unmodifiableList(listeners);
	}

	public Pool<M> enableAutoShutdown() {
		setAutoShutdown(true);
		return this;
	}

	public Pool<M> disableAutoShutdown() {
		setAutoShutdown(false);
		return this;
	}

	public boolean isAutoShutdown() {
		return listeners.contains(autoShutdown);
	}

	public void setAutoShutdown(boolean enabled) {
		if (enabled) {
			addPoolListener(autoShutdown);
		} else {
			removePoolListener(autoShutdown);
		}
	}

	public Pool<M> autoShutdown(boolean enabled) {
		setAutoShutdown(enabled);
		return this;
	}

	public void remove(Task<M> task) {
		task.cancel(true);
		// Job<M> job = task.getCallable();
		// job.setState(JobState.CANCELLED);
		getThreadPoolExecutor().purge();
	}

	public List<Job<M>> getCurrent() {
		return all != null ? null
				: all.stream()
						.filter(job -> job != null && job.getState() != null && job.getState() == JobState.EXECUTING)
						.collect(Collectors.toList());
	}

	public List<Job<M>> getCompleted() {
		return all != null ? null
				: all.stream().filter(job -> job != null && job.getState() != null && job.getState() == JobState.DONE)
						.collect(Collectors.toList());
	}

	public List<Job<M>> getQueued() {
		return all != null ? null
				: all.stream().filter(job -> job != null && job.getState() != null && job.getState() == JobState.QUEUED)
						.collect(Collectors.toList());
	}

	public List<Job<M>> getFailed() {
		return all != null ? null
				: all.stream().filter(job -> job != null && job.getState() != null && job.getState() == JobState.ERROR)
						.collect(Collectors.toList());
	}

	public List<Job<M>> getCancelled() {
		return all.stream().filter(job -> job != null && job.getState() != null && job.getState() == JobState.CANCELLED)
				.collect(Collectors.toList());
	}
}
