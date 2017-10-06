package org.jhaws.common.lang;

import java.util.Collection;
import java.util.Optional;

public interface EnhancedCollection<T> extends Collection<T> {
    default Optional<T> optional() {
        return isEmpty() ? Optional.empty() : Optional.of(iterator().next());
    }
}
