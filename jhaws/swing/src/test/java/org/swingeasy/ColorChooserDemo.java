package org.swingeasy;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class ColorChooserDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("ColorChooserDemo");
        frame.setVisible(true);

        System.out.println(JColorChooser.showDialog(null, "", Color.WHITE));
    }
}
