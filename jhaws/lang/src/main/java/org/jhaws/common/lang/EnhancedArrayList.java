package org.jhaws.common.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EnhancedArrayList<T> extends ArrayList<T> implements EnhancedList<T> {
	private static final long serialVersionUID = -6633916123289115157L;

	public EnhancedArrayList() {
		super();
	}

	public EnhancedArrayList(T c) {
		super(Arrays.asList(c));
	}

	public EnhancedArrayList(@SuppressWarnings("unchecked") T... c) {
		super(Arrays.asList(c));
	}

	public EnhancedArrayList(Collection<? extends T> c) {
		super(c);
	}

	public EnhancedArrayList(int initialCapacity) {
		super(initialCapacity);
	}
}
