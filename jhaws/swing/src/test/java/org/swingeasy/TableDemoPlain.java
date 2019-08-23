package org.swingeasy;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TableDemoPlain {
    public static void main(String[] args) {
        try {
            ETableConfig configuration = new ETableConfig(true);
            final ETable<Object[]> table = new ETable<>(configuration, null);
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.getContentPane().add(new JScrollPane(table));
            SwingUtilities.invokeLater(() -> frame.setVisible(true));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
