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

public class Pool<M> {
    protected static final AtomicInteger id = new AtomicInteger();

    protected final PoolAutoShutdownListener<M> autoShutdown = new PoolAutoShutdownListener<>();

    protected ThreadFactory threadFactory;

    protected List<Job<M>> current;

    protected List<Job<M>> completed;

    protected List<Job<M>> queued;

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
        Job<M> task = new Job<>(runnable, meta);
        return addJob(task);
    }

    public Task<M> addJob(Job<M> task) {
        return (Task<M>) getThreadPoolExecutor().submit(task);
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

    public List<Job<M>> getQueued() {
        if (queued == null) {
            queued = Collections.synchronizedList(new ArrayList<Job<M>>());
        }
        return queued;
    }

    protected BlockingQueue<Task<M>> getQueue() {
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
        }
        return queue;
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "cast" })
    public synchronized void afterPropertiesSet() {
        if (threadPoolExecutor != null) throw new UnsupportedOperationException("past creation");
        threadPoolExecutor = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) (BlockingQueue) getQueue(),
                getThreadFactory()) {
            @Override
            protected <U> FutureTask<U> newTaskFor(Callable<U> callable) {
                Job job = Job.class.cast(callable);
                Pool.this.whenQueued(job);
                return new Task(getCompleted(), job);
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
        getQueued().add(job);
        listeners.forEach(l -> l.queued(this, job));
    }

    protected void beforeExecute(Job<M> job) {
        getQueued().remove(job);
        job.setStarted(System.currentTimeMillis());
        getCurrent().add(job);
        listeners.forEach(l -> l.started(this, job));
    }

    protected void afterExecute(Job<M> job) {
        getCurrent().remove(job);
        job.setFinished(System.currentTimeMillis());
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
        if (threadPoolExecutor != null) throw new UnsupportedOperationException("past creation");
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
        if (threadPoolExecutor != null) throw new UnsupportedOperationException("past creation");
        this.priority = priority;
    }

    public boolean getDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        if (threadPoolExecutor != null) throw new UnsupportedOperationException("past creation");
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
        if (threadPoolExecutor != null) throw new UnsupportedOperationException("past creation");
        this.name = name;
    }

    public void shutdown() {
        threadPoolExecutor.shutdown();
        listeners.forEach(l -> l.shutdown(this));
    }

    public void addPoolListener(PoolListener<M> listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removePoolListener(PoolListener<M> listener) {
        if (listeners.contains(listener)) listeners.remove(listener);
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
}
