package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * @author Jurgen
 */
public class TreeDemo {
    private static class TreeDemoNode extends ETreeNode<String> {
        private static final long serialVersionUID = 4389106694997553842L;

        public TreeDemoNode() {
            this("0"); //$NON-NLS-1$
        }

        public TreeDemoNode(String userObject) {
            super(userObject);
        }

        @Override
        protected void initChildren(List<ETreeNode<String>> list) {
            System.out.println("lazy-init " + this); //$NON-NLS-1$
            if (!this.getUserObject().toString().endsWith("5")) { //$NON-NLS-1$
                for (int i = 0; i < 10; i++) {
                    String s = this.getUserObject() + "." + i; //$NON-NLS-1$
                    list.add(new TreeDemoNode(s));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIUtils.systemLookAndFeel();
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            ETree<String> tree = new ETree<String>(new ETreeConfig().setEditable(false).setFocusColor(Color.GREEN), new TreeDemoNode());
            frame.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
            frame.getContentPane().add(tree.getSearchComponent(), BorderLayout.NORTH);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.setSize(400, 400);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
