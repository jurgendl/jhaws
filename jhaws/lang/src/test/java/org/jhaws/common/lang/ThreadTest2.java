package org.jhaws.common.lang;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jhaws.common.pool.RunnableThread;

public class ThreadTest2 {
    public static void main(String[] args) {
        RunnableThread rt = new RunnableThread(() -> {
            while (true) {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(1);
            }
        });
        rt.start();
        JFrame f = new JFrame();
        f.setSize(200, 200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton b = new JButton("x");
        b.addActionListener(ev -> {
            rt.stop();
        });
        f.getContentPane().add(b, BorderLayout.CENTER);
        f.setVisible(true);
    }
}
