package org.jhaws.common.io;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jhaws.common.io.FilePath.FilePathWatcher;

public class FilePathDemo {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton b = new JButton("stop");
        f.getContentPane().add(b, BorderLayout.CENTER);
        FilePathWatcher watch = new FilePath().watch(System.out::println);
        b.addActionListener(ev -> {
            watch.stop();
            f.dispose();
        });
        f.pack();
        f.setVisible(true);
        System.out.println("-");
    }
}
