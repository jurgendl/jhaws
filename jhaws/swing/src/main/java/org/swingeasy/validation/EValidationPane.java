package org.swingeasy.validation;

import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * @author Jurgen
 */
public class EValidationPane extends JLayeredPane {
    private static final long serialVersionUID = 1570586590646022420L;

    protected JPanel frontPanel = new JPanel(null);

    public EValidationPane(Container validates) {
        this.frontPanel.setOpaque(false);
        this.add(validates, new Integer(0), 0);
        this.add(this.frontPanel, new Integer(1), 0);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (int i = 0; i < EValidationPane.this.getComponentCount(); i++) {
                    EValidationPane.this.getComponent(i).setBounds(0, 0, EValidationPane.this.getWidth(), EValidationPane.this.getHeight());
                }
            }
        });
    }
}