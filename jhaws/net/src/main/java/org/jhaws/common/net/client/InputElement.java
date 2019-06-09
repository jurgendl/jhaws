package org.jhaws.common.net.client;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public interface InputElement extends Serializable {
	public String getId();

	public String getName();

	public InputType getType();

	public String getValue();

	public void setValue(String value);

	default String getNameOrId() {
		String nameOrId = getName();
		if (StringUtils.isBlank(nameOrId))
			nameOrId = getId();
		return nameOrId;
	}
}
