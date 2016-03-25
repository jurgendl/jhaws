package org.swingeasy;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JFrame;

/**
 * @author Jurgen
 */
public class FileSelectionDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 250);
        f.setLocationRelativeTo(null);
        f.getContentPane().setLayout(new GridLayout(-1, 1));
        f.getContentPane().add(new FileSelection(new FileSelectionConfig()));
        File currentdir = new File(".").getAbsoluteFile().getParentFile();
        f.getContentPane().add(new FileSelection(new FileSelectionConfig(currentdir)));
        f.getContentPane().add(new FileSelection(new FileSelectionConfig("pdf")));
        f.getContentPane().add(new FileSelection(new FileSelectionConfig(new File(currentdir, "1.pdf"), "pdf")));
        f.getContentPane().add(new FileSelection(new FileSelectionConfig("pdf", "txt")));
        f.getContentPane().add(new FileSelection(new FileSelectionConfig(new File(currentdir, "1.txt"), "pdf", "txt")));
        f.setVisible(true);
    }
}
