package org.swingeasy;

import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public enum MessageType {
    /** Used for error messages. */
    ERROR(JOptionPane.ERROR_MESSAGE),
    /** Used for information messages. */
    INFORMATION(JOptionPane.INFORMATION_MESSAGE),
    /** Used for warning messages. */
    WARNING(JOptionPane.WARNING_MESSAGE),
    /** Used for questions. */
    QUESTION(JOptionPane.QUESTION_MESSAGE),
    /** No icon is used. */
    PLAIN(JOptionPane.PLAIN_MESSAGE);

    int code;

    private MessageType(int code) {
        this.code = code;
    }
}
