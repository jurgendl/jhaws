package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class ListDnDDemo {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        JSplitPane jsp = new JSplitPane();
        f.getContentPane().add(jsp, BorderLayout.CENTER);
        {
            EListConfig cfg = new EListConfig();
            EList<String> list = new EList<>(cfg);
            list.addRecord(new EListRecord<>("bean1"));
            list.addRecord(new EListRecord<>("bean2"));
            list.addRecord(new EListRecord<>("bean3"));
            jsp.setLeftComponent(list);
        }
        {
            EListConfig cfg = new EListConfig();
            EList<String> list = new EList<>(cfg);
            jsp.setRightComponent(list);
        }
        jsp.setDividerLocation(200);
        f.setSize(400, 400);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
