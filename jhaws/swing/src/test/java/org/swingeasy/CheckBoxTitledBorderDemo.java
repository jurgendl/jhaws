package org.swingeasy;

import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class CheckBoxTitledBorderDemo {
    public static void main(String[] args) {
        final JPanel panel = new JPanel();
        panel.add(new JCheckBox("inner checkbox")); //$NON-NLS-1$
        panel.setBorder(new CheckBoxTitledBorder(panel, " outer checkbox")); //$NON-NLS-1$
        JFrame frame = new JFrame();
        Container contents = frame.getContentPane();
        contents.setLayout(new FlowLayout());
        contents.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
