package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.swingeasy.ECheckBoxList.ECheckBoxListRecord;

/**
 * @author Jurgen
 */
public class ListDemo3 {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        ListDemo3.newFrameOld();
        // ListDemo3.newFrame();
    }

    protected static void newFrame() {
        JFrame f = new JFrame("new");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final ECheckBoxList cbl = new ECheckBoxList(new EListConfig().setSortable(false));
        final EList<Boolean> stsi = cbl.stsi();
        for (int i = 0; i < 10; i++) {
            stsi.addRecord(new ECheckBoxListRecord("item " + i, true));
        }
        f.getContentPane().add(new JScrollPane(cbl));
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        EButton selectedbtn = new EButton(new EButtonConfig("selected?"));
        selectedbtn.addActionListener(e -> {
            for (EListRecord<Boolean> record : stsi.getSelectedRecords()) {
                System.out.println(record.getStringValue() + "=" + record.get());
            }
        });
        f.getContentPane().add(selectedbtn, BorderLayout.NORTH);
        EButton checkedbtn = new EButton(new EButtonConfig("checked?"));
        checkedbtn.addActionListener(e -> {
            for (EListRecord<Boolean> record : stsi.getRecords()) {
                System.out.println(record.getStringValue() + "=" + record.get());
            }
        });
        f.getContentPane().add(checkedbtn, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    protected static void newFrameOld() {
        JFrame f = new JFrame("old");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final EList<String> cbl = new EList<>(new EListConfig().setSortable(false));
        final EList<String> stsi = cbl.stsi();
        for (int i = 0; i < 10; i++) {
            stsi.addRecord(new EListRecord<>("item " + i));
        }
        f.getContentPane().add(new JScrollPane(cbl));
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        EButton selectedbtn = new EButton(new EButtonConfig("selected?"));
        selectedbtn.addActionListener(e -> {
            for (EListRecord<String> record : stsi.getSelectedRecords()) {
                System.out.println(record.get());
            }
        });
        f.getContentPane().add(selectedbtn, BorderLayout.NORTH);
        f.setVisible(true);
    }
}
