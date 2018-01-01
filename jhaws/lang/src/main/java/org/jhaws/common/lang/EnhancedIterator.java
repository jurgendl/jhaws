package org.jhaws.common.lang;

import java.util.Iterator;
import java.util.stream.Stream;

public interface EnhancedIterator<T> extends Iterator<T> {
	default Stream<T> stream() {
		return CollectionUtils8.stream(this);
	}
}
