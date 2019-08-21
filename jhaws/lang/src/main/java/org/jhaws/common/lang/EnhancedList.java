package org.jhaws.common.lang;

import java.util.List;

public interface EnhancedList<T> extends List<T>, EnhancedCollection<T> {
	default T last() {
		return get(size() - 1);
	}
}
