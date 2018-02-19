package org.jhaws.common.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class WorkerQueue<T> {
    private final BlockingQueue<T> workQueue;

    private final ExecutorService service;

    public WorkerQueue(int numWorkers, int workQueueSize, Consumer<T> action) {
        workQueue = new LinkedBlockingQueue<>(workQueueSize);
        service = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            service.submit(new Worker<>(workQueue, action));
        }
    }

    public void produce(T item) {
        try {
            workQueue.put(item);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static class Worker<T> implements Runnable {
        private final BlockingQueue<T> workQueue;

        private final Consumer<T> action;

        public Worker(BlockingQueue<T> workQueue, Consumer<T> action) {
            this.workQueue = workQueue;
            this.action = action;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    T item = workQueue.take();
                    action.accept(item);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
