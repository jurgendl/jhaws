package org.swingeasy;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * @author Jurgen
 */
public class CheckBoxTitledBorder extends ComponentTitledBorder {
    public CheckBoxTitledBorder(final JComponent container, String checkboxText) {
        super(new JCheckBox(checkboxText), container, BorderFactory.createEtchedBorder());
        final JCheckBox checkBox = this.getCheckbox();
        checkBox.setSelected(true);
        checkBox.setFocusPainted(false);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enable = checkBox.isSelected();
                Component[] components = container.getComponents();
                for (Component component : components) {
                    component.setEnabled(enable);
                }
            }
        });
    }

    public JCheckBox getCheckbox() {
        return JCheckBox.class.cast(this.comp);
    }
}
