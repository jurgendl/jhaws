package org.swingeasy;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class ComboboxAutoResizeDemo {
    public static void main(String[] args) {
        Point location = null;
        EComboBoxConfig cfg = new EComboBoxConfig();
        cfg.setAutoResizePopup(true);
        {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            EComboBox<String> combo = new EComboBox<String>(cfg);
            Dimension dim = new Dimension(100, 20);
            combo.setMaximumSize(dim);
            combo.setPreferredSize(dim);
            combo.setSize(dim);
            combo.stsi().addRecord(new EComboBoxRecord<String>("azertyuiopqsdfghjklmwxcvbn0123456789"));
            frame.getContentPane().add(combo);
            frame.setSize(new Dimension(100, 70));
            frame.setLocationRelativeTo(null);
            frame.setTitle("ComboboxAutoResizeDemo (borderlayout)");
            frame.setVisible(true);
            location = frame.getLocation();
        }
        {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            EComboBox<String> combo = new EComboBox<String>(cfg);
            Dimension dim = new Dimension(100, 20);
            combo.setMaximumSize(dim);
            combo.setPreferredSize(dim);
            combo.setSize(dim);
            combo.stsi().addRecord(new EComboBoxRecord<String>("azertyuiopqsdfghjklmwxcvbn0123456789"));
            frame.getContentPane().setLayout(new MigLayout());
            frame.getContentPane().add(combo);
            frame.setSize(new Dimension(100, 70));
            location.y += 150;
            frame.setLocation(location);
            frame.setTitle("ComboboxAutoResizeDemo (miglayout)");
            frame.setVisible(true);
        }
    }
}
