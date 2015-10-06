package org.jhaws.common.net.client.v45;

import java.io.Serializable;

public interface InputElement extends Serializable {
    public String getId();

    public String getName();

    public InputType getType();

    public String getValue();

    public void setValue(String value);
}
