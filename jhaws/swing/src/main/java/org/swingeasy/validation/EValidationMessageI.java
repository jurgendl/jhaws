package org.swingeasy.validation;

import org.swingeasy.EComponentI;

/**
 * @author Jurgen
 */
public interface EValidationMessageI extends EComponentI {
    public abstract void setIsInvalid(String message);

    public abstract void setIsValid();

    public abstract void setShowWhenValid(boolean b);
}
