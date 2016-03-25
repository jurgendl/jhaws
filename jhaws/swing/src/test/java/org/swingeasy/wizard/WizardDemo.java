package org.swingeasy.wizard;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * @author Jurgen
 */
public class WizardDemo {
    private static class WPage extends WizardPage {
        public WPage(String title, String description) {
            super(title, description);
        }

        @Override
        public JComponent createComponent() {
            JPanel jp = new JPanel(new FlowLayout());
            jp.add(new JLabel(this.getTitle()));
            return jp;
        }

        @Override
        public boolean validate() {
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            //
        }

        final JFrame f = new JFrame();
        EWizard comp = new EWizard();
        // comp.setLeftPanelVisible(false);
        // comp.setTopPanelVisible(false);
        comp.setIcon(new ImageIcon(WizardDemo.class.getClassLoader().getResource("wiz.png")));
        comp.addWizardPage(new WPage(
                "page 1",
                "A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it. A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it."));
        comp.addWizardPage(new WPage(
                "page 2",
                "A display area for a short text string or an image, or both. A label does not react to input events. As a result, it cannot get the keyboard focus. A label can, however, display a keyboard alternative as a convenience for a nearby component that has a keyboard alternative but can't display it."));
        comp.addWizardPage(new WPage("page 3", ""));
        comp.setCancelAction(new AbstractAction("cancel") {
            private static final long serialVersionUID = -3249259735298003295L;

            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
            }
        });
        comp.init();
        f.getContentPane().add(comp);
        f.pack();
        f.setSize(800, 600);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
