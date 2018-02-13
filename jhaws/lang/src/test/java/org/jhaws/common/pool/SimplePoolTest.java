package org.jhaws.common.pool;

import java.util.Iterator;

import org.jhaws.common.lang.RunIndefinitely;

public class SimplePoolTest {
    public static class Ctx {
        @Override
        public String toString() {
            return "";
        }
    }

    public static void main(String[] args) {
        Pool<Ctx> pool = new Pool<Ctx>().name("testpool").size(2).daemon(false).enableAutoShutdown();
        for (int i = 1; i <= 10; i++) {
            int ii = i;
            pool.addJob(() -> {
                System.out.println(ii + " started");
                try {
                    Thread.sleep((ii + 1) * 1000l);
                } catch (InterruptedException ex) {
                    //
                }
                System.out.println(ii + " ended");
            }, new Ctx());
            System.out.println(i + " added");
        }
        System.out.println("all added");
        Thread pooldebug = new Thread((RunIndefinitely) () -> {
            if (pool.getQueue().size() > 0 || pool.getCurrent().size() > 0) {
                System.out.println("#" + pool.getQueue().size() + " waiting, #" + pool.getCurrent().size() + " executing");
                Iterator<Job<Ctx>> d = pool.getCompleted().iterator();
                while (d.hasNext()) {
                    Job<Ctx> task = d.next();
                    System.out.println("DONE:" + task);
                }
                Iterator<Job<Ctx>> b = pool.getCurrent().iterator();
                while (b.hasNext()) {
                    Job<Ctx> task = b.next();
                    System.out.println("BUSY:" + task);
                }
                Iterator<Job<Ctx>> q = pool.getQueued().iterator();
                while (q.hasNext()) {
                    Job<Ctx> task = q.next();
                    System.out.println("QUEUED:" + task);
                }
            }
            return 1000l;
        }, "testpool");
        pooldebug.setDaemon(true);
        pooldebug.start();
    }
}
