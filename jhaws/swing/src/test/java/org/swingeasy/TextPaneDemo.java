package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TextPaneDemo {
    private static void addComponents(Container container) {
        final ETextPane pane = new ETextPane(new ETextPaneConfig());
        container.add(pane);
        container.add(pane.getToolbar(), BorderLayout.NORTH);

        JButton btn = new JButton("test");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(pane.getText());
            }
        });
        container.add(btn, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TextPaneDemo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("TextPaneDemo");
        frame.setVisible(true);
    }
}
