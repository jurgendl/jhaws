package org.jhaws.common.lang;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

// https://stackoverflow.com/questions/184147/countdownlatch-vs-semaphore
// https://www.baeldung.com/java-cyclicbarrier-countdownlatch
public class LatchTest {
    public static void main(String[] args) {
        int c = 10;
        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(c);
        CyclicBarrier barrier = new CyclicBarrier(c);
        Random r = new Random(0);
        for (int i = 0; i < c; ++i) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException ex1) {
                        //
                    }
                    System.out.println("start " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    try {
                        Thread.sleep(1000 + r.nextInt(4000));
                    } catch (InterruptedException ex) {
                        //
                    }
                    System.out.println("end " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
                    latch.countDown();
                }
            });
            t.start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex1) {
                //
            }
        }
        System.out.println("start");
        try {
            latch.await();
        } catch (InterruptedException ex) {
            //
        }
        System.out.println("end");
    }
}
