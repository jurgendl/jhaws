package org.jhaws.common.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class EnhancedLinkedList<T> extends LinkedList<T> implements EnhancedList<T> {
	private static final long serialVersionUID = -4025615187867458155L;

	public EnhancedLinkedList() {
		super();
	}

	public EnhancedLinkedList(Collection<? extends T> c) {
		super(c);
	}

	public EnhancedLinkedList(T c) {
		super(Arrays.asList(c));
	}

	public EnhancedLinkedList(@SuppressWarnings("unchecked") T... c) {
		super(Arrays.asList(c));
	}
}
