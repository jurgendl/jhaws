package org.swingeasy;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 */
public class TableDemoPlain {
    public static void main(String[] args) {
        try {
            ETableConfig configuration = new ETableConfig(true);
            final ETable<Object[]> table = new ETable<Object[]>(configuration, null);
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.getContentPane().add(new JScrollPane(table));
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
