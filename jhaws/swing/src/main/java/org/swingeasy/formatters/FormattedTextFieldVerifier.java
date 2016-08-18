package org.swingeasy.formatters;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

/**
 * A verifier that checks whether the content of a formatted text field is valid.
 */
public class FormattedTextFieldVerifier extends InputVerifier {
    /**
     * 
     * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
     */
    @Override
    public boolean verify(JComponent component) {
        JFormattedTextField field = (JFormattedTextField) component;
        return field.isEditValid();
    }
}
