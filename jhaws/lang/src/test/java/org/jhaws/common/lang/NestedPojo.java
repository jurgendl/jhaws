package org.jhaws.common.lang;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NestedPojo {
	private String nestedVeld;

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("nestedVeld", nestedVeld).toString();
	}

	public String getNestedVeld() {
		return this.nestedVeld;
	}

	public void setNestedVeld(String nestedVeld) {
		this.nestedVeld = nestedVeld;
	}
}
