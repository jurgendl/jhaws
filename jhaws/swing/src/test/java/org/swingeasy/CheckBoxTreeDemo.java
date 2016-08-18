package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class CheckBoxTreeDemo {
    public static void main(String[] args) {
        try {
            UIUtils.systemLookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            ECheckBoxTreeNode<String> root = new ECheckBoxTreeNode<>("on1", true); //$NON-NLS-1$
            root.add(new ECheckBoxTreeNode<>("on11", true)); //$NON-NLS-1$
            root.add(new ECheckBoxTreeNode<>("on12", true)); //$NON-NLS-1$
            ECheckBoxTree<String> tree = new ECheckBoxTree<>(root);
            frame.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
            SwingUtilities.invokeLater(() -> {
                frame.setSize(200, 200);
                frame.setVisible(true);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
