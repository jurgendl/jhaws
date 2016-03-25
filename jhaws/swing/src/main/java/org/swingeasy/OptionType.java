package org.swingeasy;

import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public enum OptionType {
    /**
     * Type meaning Look and Feel should not supply any options -- only use the options from the <code>JOptionPane</code>.
     */
    OK(JOptionPane.DEFAULT_OPTION, ResultType.OK),
    /** Type used for <code>showConfirmDialog</code>. */
    YES_NO(JOptionPane.YES_NO_OPTION, ResultType.YES, ResultType.NO),
    /** Type used for <code>showConfirmDialog</code>. */
    YES_NO_CANCEL(JOptionPane.YES_NO_CANCEL_OPTION, ResultType.YES, ResultType.NO, ResultType.CANCEL),
    /** Type used for <code>showConfirmDialog</code>. */
    OK_CANCEL(JOptionPane.OK_CANCEL_OPTION, ResultType.OK, ResultType.CANCEL);

    int code;

    private ResultType[] resultTypes;

    private OptionType(int code, ResultType... resultTypes) {
        this.code = code;
        this.resultTypes = resultTypes;
    }

    ResultType getResultType(int resultTypeCode) {
        for (ResultType resultType : this.resultTypes) {
            if (resultType.code == resultTypeCode) {
                return resultType;
            }
        }
        throw new IllegalArgumentException();
    }
}