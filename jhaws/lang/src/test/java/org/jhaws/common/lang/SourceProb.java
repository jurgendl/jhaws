package org.jhaws.common.lang;

import java.util.Date;

public class SourceProb {
	private Date date;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void test() {
		this.date = new Date(0);
	}
}
