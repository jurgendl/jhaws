package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class SpinnerDemo {
    private static void addComponents(Container container) {
        final ESpinner<Integer> comp = new ESpinner<>(new SpinnerNumberModel(1, -100, 100, 1));
        container.add(comp);
        JButton b = new JButton("w");
        b.addActionListener(e -> System.out.println(comp.getValue()));
        container.add(b, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        SpinnerDemo.addComponents(frame.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("SpinnerDemo");
        frame.setVisible(true);
    }
}