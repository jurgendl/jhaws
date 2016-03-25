package org.swingeasy;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Jurgen
 */
public abstract class ELabeledTextFieldButtonComponent extends AbstractELabeledTextFieldButtonComponent<ELabel, ETextField, EButton> {
    private static final long serialVersionUID = 3476659373915137098L;

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        EComponentPopupMenu.copyToClipboard(this.getInput().getText());
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getButton()
     */
    @Override
    public EButton getButton() {
        if (this.button == null) {
            EButton _button = new EButton(new EButtonConfig(new EIconButtonCustomizer(new Dimension(20, 20)), this.getIcon()));
            _button.setActionCommand(this.getAction());
            _button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ELabeledTextFieldButtonComponent.this.doAction();
                }
            });
            this.button = _button;
        }
        return this.button;
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getInput()
     */
    @Override
    public ETextField getInput() {
        if (this.input == null) {
            ETextField _input = new ETextField(new ETextFieldConfig());
            _input.setBorder(null);
            _input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        ELabeledTextFieldButtonComponent.this.doAction();
                    }
                }
            });
            this.input = _input;
        }
        return this.input;
    }

    /**
     * 
     * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getLabel()
     */
    @Override
    public ELabel getLabel() {
        if (this.label == null) {
            ELabel _label = new ELabel();
            _label.setLabelFor(this.getInput());
            this.label = _label;
        }
        return this.label;
    }
}
