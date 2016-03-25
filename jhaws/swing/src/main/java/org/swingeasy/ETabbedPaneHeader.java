package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * @author Jurgen
 */
public class ETabbedPaneHeader extends JComponent {

    private static final long serialVersionUID = -1987585120165138408L;

    public static final String ACTION_MINIMIZE = "minimize";

    public static final String ACTION_CLOSE = "close";

    protected final String title;

    protected final Icon icon;

    protected final String tip;

    public ETabbedPaneHeader(String title, Icon icon, String tip, ETabbedPaneConfig config, ActionListener actionlistener) {
        this.title = title;
        this.icon = icon;
        this.tip = tip;

        this.setLayout(new BorderLayout());

        if (config.getRotation() == Rotation.DEFAULT) {
            ELabel label = new ELabel(new ELabelConfig(title, icon, SwingConstants.LEADING));
            label.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
            this.add(label, BorderLayout.CENTER);

            JPanel container = new JPanel(new GridLayout(1, -1));
            container.setOpaque(false);
            this.add(container, BorderLayout.EAST);
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
        } else if (config.getRotation() == Rotation.CLOCKWISE) {
            RotatedLabel label = new RotatedLabel(title, icon, true);
            label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            this.add(label, BorderLayout.CENTER);

            JPanel container = new JPanel(new GridLayout(-1, 1));
            container.setOpaque(false);
            this.add(container, BorderLayout.SOUTH);
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
        } else {
            RotatedLabel label = new RotatedLabel(title, icon, false);
            label.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            this.add(label, BorderLayout.CENTER);

            JPanel container = new JPanel(new GridLayout(-1, 1));
            container.setOpaque(false);
            this.add(container, BorderLayout.NORTH);
            if (config.isMinimizable()) {
                this.makeMinimizable(container, actionlistener);
            }
            if (config.isClosable()) {
                this.makeClosable(container, actionlistener);
            }
        }
    }

    private void makeClosable(Container container, ActionListener actionlistener) {
        Icon _icon = UIManager.getIcon("InternalFrame.closeIcon");
        if (_icon == null) {
            return;
        }
        EButton closeButton = new EButton(new EButtonConfig(new EIconButtonCustomizer(new Dimension(_icon.getIconWidth(), _icon.getIconHeight())),
                _icon));
        closeButton.setActionCommand(ETabbedPaneHeader.ACTION_CLOSE);
        closeButton.addActionListener(actionlistener);
        container.add(closeButton);
    }

    private void makeMinimizable(Container container, ActionListener actionlistener) {
        // see for keys: com.sun.java.swing.plaf.windows.WindowsLookAndFeel
        Icon _icon = UIManager.getIcon("InternalFrame.iconifyIcon");
        if (_icon == null) {
            return;
        }
        EButton minimizeButton = new EButton(new EButtonConfig(new EIconButtonCustomizer(new Dimension(_icon.getIconWidth(), _icon.getIconHeight())),
                _icon));
        minimizeButton.setActionCommand(ETabbedPaneHeader.ACTION_MINIMIZE);
        minimizeButton.addActionListener(actionlistener);
        container.add(minimizeButton);
    }

}
