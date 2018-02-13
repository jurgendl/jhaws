package org.jhaws.common.pool;

import java.util.Date;

import org.jhaws.common.lang.RunIndefinitely;

public class PoolTest {
    public static void main(String[] args) {
        try {
            Pool<Object> pool = new Pool<>("test", 1);
            TestServiceImpl t = PooledService.pool(pool, TestServiceImpl.class, new TestServiceImpl());
            for (int i = 0; i < 10; i++) {
                final int ii = i;
                new Thread(() -> t.doSomethingFast(ii)).start();
                new Thread(() -> t.doSomethingPooled(ii)).start();
            }
            new Thread((RunIndefinitely) () -> Long.MAX_VALUE).start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void test(int o) {
        System.out.println(new Date() + ":start:" + o);
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException ex) {
            //
        }
        System.out.println(new Date() + ":end:" + o);
    }

    public static class TestServiceImpl {
        @Pooled
        public void doSomethingPooled(Object o) {
            System.out.println(new Date() + ":doSomethingPooled:start:" + o);
            try {
                Thread.sleep(5000l);
            } catch (InterruptedException ex) {
                //
            }
            System.out.println(new Date() + ":doSomethingPooled:end:" + o);
        }

        public void doSomethingFast(Object o) {
            System.out.println(new Date() + ":doSomethingFast:start:" + o);
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException ex) {
                //
            }
            System.out.println(new Date() + ":doSomethingFast:end:" + o);
        }
    }
}
