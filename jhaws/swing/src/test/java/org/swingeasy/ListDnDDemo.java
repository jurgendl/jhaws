package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

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
            EList<String> list = new EList<String>(cfg);
            list.addRecord(new EListRecord<String>("bean1"));
            list.addRecord(new EListRecord<String>("bean2"));
            list.addRecord(new EListRecord<String>("bean3"));
            jsp.setLeftComponent(list);
        }
        {
            EListConfig cfg = new EListConfig();
            EList<String> list = new EList<String>(cfg);
            jsp.setRightComponent(list);
        }
        jsp.setDividerLocation(200);
        f.setSize(400, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
