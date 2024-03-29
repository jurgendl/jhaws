package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class ComboBoxDemo {
    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();
        EComboBoxConfig cfg = new EComboBoxConfig();
        cfg.setSortable(true);
        EComboBox<String> cc = new EComboBox<>(cfg);
        {
            JFrame f = new JFrame();
            f.getContentPane().add(cc, BorderLayout.CENTER);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setVisible(true);
            cc = cc.stsi();
            final EComboBox<String> ccc = cc;
            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    EComboBoxRecord<String> selectedRecord = ccc.getSelectedRecord();
                    System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord); //$NON-NLS-1$
                }
            });
            f.setSize(200, 80);
        }
        cc.addRecord(null);
        final Random r = new Random(256955466579946l);
        EComboBoxRecord<String> record = null;
        for (int i = 0; i < 1000; i++) {
            record = new EComboBoxRecord<>(String.valueOf(r.nextInt(1000000000)) + String.valueOf(r.nextInt(1000000000)) + String.valueOf(r.nextInt(1000000000)) + String.valueOf(r.nextInt(1000000000)) + String.valueOf(r.nextInt(1000000000)));
            cc.addRecord(record);
        }
        cc.setSelectedRecord(record);
        EComboBoxRecord<String> selectedRecord = cc.getSelectedRecord();
        System.out.println(selectedRecord == null ? null : selectedRecord.getClass() + " " + selectedRecord); //$NON-NLS-1$
    }
}
