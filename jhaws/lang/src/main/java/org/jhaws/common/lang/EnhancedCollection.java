package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public interface EnhancedCollection<T> extends Collection<T> {
	default Optional<T> optional() {
		return isEmpty() ? Optional.ofNullable(null) : Optional.of(iterator().next());
	}

	default T singleton(T defaultValue) {
		return optional().orElse(defaultValue);
	}

	default T singleton(Supplier<T> defaultValue) {
		return optional().orElseGet(defaultValue);
	}

	default T singleton() {
		return optional().orElse(null);
	}

	default T get() {
		return singleton();
	}
}
