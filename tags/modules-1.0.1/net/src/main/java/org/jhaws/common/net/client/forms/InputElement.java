package org.jhaws.common.net.client.forms;

import java.io.Serializable;

/**
 * InputElement
 */
public interface InputElement extends Serializable {
    public String getId();

    public String getName();

    public InputType getType();

    public String getValue();

    public void setValue(String value);
}
