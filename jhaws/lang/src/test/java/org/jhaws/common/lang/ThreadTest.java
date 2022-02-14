package org.jhaws.common.lang;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jhaws.common.pool.Job;
import org.jhaws.common.pool.Pool;
import org.jhaws.common.pool.Task;

// https://stackoverflow.com/questions/10961714/how-to-properly-stop-the-thread-in-java
public class ThreadTest {
    public static void main(String[] args) {
        Pool<Object> wdljobs = new Pool<>("test", 2);
        Map<Job<Object>, Task<Object>> map = new LinkedHashMap<>();
        for (int i = 0; i < 1; i++) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("started " + this);
                    try {
                        // if (true) throw new RuntimeException();
                        while (true) {
                            // System.out.println(Thread.currentThread() + "-" + System.currentTimeMillis());
                            Thread.sleep(1000l);
                            // if (Thread.currentThread().isInterrupted()) {
                            // break;
                            // }
                            System.out.println("running " + this);
                        }
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted " + this);
                    }
                    System.out.println("done " + this);
                }
            };
            Job<Object> job = new Job<>(r, null);
            Task<Object> task = wdljobs.addJob(job);
            map.put(job, task);
        }

        JFrame f = new JFrame();
        f.setSize(200, 200);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JButton b = new JButton("x");
        b.addActionListener(ev -> {
            System.out.println("A" + wdljobs.getAll().size() + "/D" + wdljobs.getCompleted().size() + "/C" + wdljobs.getCurrent().size() + "/Q" + wdljobs.getQueued().size() + "/F" + wdljobs.getFailed().size() + "/X" + wdljobs.getCancelled().size());
            CollectionUtils8.reverse(map.values().stream()).forEach(wdljobs::remove);
            System.out.println("A" + wdljobs.getAll().size() + "/D" + wdljobs.getCompleted().size() + "/C" + wdljobs.getCurrent().size() + "/Q" + wdljobs.getQueued().size() + "/F" + wdljobs.getFailed().size() + "/X" + wdljobs.getCancelled().size());
        });
        f.getContentPane().add(b, BorderLayout.CENTER);
        f.setVisible(true);
    }
}
