package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public abstract class AbstractELabeledTextFieldButtonComponent<LABEL extends JComponent, INPUT extends JComponent, BUTTON extends JComponent> extends
        JComponent implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = 3916693177023150847L;

    protected INPUT input;

    protected BUTTON button;

    protected LABEL label;

    protected Border defaultBorder = new JTextField().getBorder();

    public AbstractELabeledTextFieldButtonComponent() {
        this.createComponent();
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    protected void createComponent() {
        this.setLayout(new BorderLayout());
        this.setLocale(null);

        JPanel internal = new JPanel(new BorderLayout());
        this.add(this.getLabel(), BorderLayout.WEST);
        this.add(internal, BorderLayout.CENTER);
        internal.add(this.getInput(), BorderLayout.CENTER);
        internal.add(this.getButton(), BorderLayout.EAST);

        internal.setBorder(this.defaultBorder);
        this.setBackground(Color.WHITE);
        internal.setBackground(Color.WHITE);
    }

    protected abstract void doAction();

    protected abstract String getAction();

    public abstract BUTTON getButton();

    protected abstract Icon getIcon();

    public abstract INPUT getInput();

    public abstract LABEL getLabel();

    public void setButton(BUTTON button) {
        this.button = button;
    }

    public void setInput(INPUT input) {
        this.input = input;
    }

    public void setLabel(LABEL label) {
        this.label = label;
    }

    public void setShowLabel(boolean showLabel) {
        this.getLabel().setVisible(showLabel);
    }
}
