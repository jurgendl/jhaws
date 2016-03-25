package org.swingeasy;

import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public enum ResultType {
    YES(JOptionPane.YES_OPTION), NO(JOptionPane.NO_OPTION), OK(JOptionPane.OK_OPTION), CANCEL(JOptionPane.CANCEL_OPTION), CLOSED(
            JOptionPane.CLOSED_OPTION);

    int code;

    private ResultType(int code) {
        this.code = code;
    }
}