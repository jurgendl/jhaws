package org.jhaws.common.lang;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommonNestedPojo {
	private String commonNestedField;

	public String getCommonNestedField() {
		return this.commonNestedField;
	}

	public void setCommonNestedField(String commonNestedField) {
		this.commonNestedField = commonNestedField;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append("commonNestedField", commonNestedField).toString();
	}
}
