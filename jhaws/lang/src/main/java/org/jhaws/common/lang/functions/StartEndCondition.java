package org.jhaws.common.lang.functions;

import java.util.function.Predicate;

import org.jhaws.common.lang.BooleanValue;

public class StartEndCondition<T> implements Predicate<T> {
    final BooleanValue conditioned = new BooleanValue();

    final Predicate<T> startCondition;

    final Predicate<T> endCondition;

    public StartEndCondition(Predicate<T> startCondition, Predicate<T> endCondition) {
        this.startCondition = startCondition;
        this.endCondition = endCondition;
    }

    public boolean is() {
        return conditioned.get();
    }

    @Override
    public boolean test(T line) {
        boolean replacable = conditioned.isTrue();
        if (conditioned.isFalse() && startCondition.test(line)) {
            conditioned.setTrue();
        }
        if (conditioned.isTrue() && endCondition.test(line)) {
            conditioned.setFalse();
        }
        boolean b = replacable || conditioned.isTrue();
        return b;
    }
}
