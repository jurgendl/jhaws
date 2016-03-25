package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class DateTimeDemo {
    private static void addComponents(Container container) {
        container.setLayout(new GridLayout(-1, 1));
        final EDateEditor ede0 = new EDateEditor();
        final EDateTimeEditor edte0 = new EDateTimeEditor();
        final ETimeEditor edt0 = new ETimeEditor();
        {
            container.add(ede0, BorderLayout.NORTH);
            container.add(edte0, BorderLayout.CENTER);
            container.add(edt0, BorderLayout.SOUTH);
        }
        final EDateEditor ede = new EDateEditor();
        final EDateTimeEditor edte = new EDateTimeEditor();
        final ETimeEditor edt = new ETimeEditor();
        {
            ede.setDate(null);
            container.add(ede, BorderLayout.NORTH);
            edte.setDate(null);
            container.add(edte, BorderLayout.CENTER);
            edt.setDate(null);
            container.add(edt, BorderLayout.SOUTH);
        }
        JButton log = new JButton("log");
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(ede0.getDate());
                System.out.println(edte0.getDate());
                System.out.println(ede.getDate());
                System.out.println(edte.getDate());
            }
        });
        container.add(log);
    }

    public static void main(String[] args) {
        // SystemSettings.setCurrentLocale(Locale.US);
        UIUtils.niceLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        DateTimeDemo.addComponents(frame.getContentPane());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle("DateDemo");
        frame.setVisible(true);
    }
}
