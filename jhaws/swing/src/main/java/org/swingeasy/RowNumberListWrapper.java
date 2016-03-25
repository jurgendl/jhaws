package org.swingeasy;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.apache.poi.ss.formula.functions.T;

public class RowNumberListWrapper extends JScrollPane {
	private static final long serialVersionUID = 270584519024896629L;

	public RowNumberListWrapper(JList<T> wrapped) {
		super(wrapped);
		RowNumberList rowList = new RowNumberList(wrapped);
		this.setRowHeaderView(rowList);
	}
}
