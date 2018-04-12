package org.jhaws.common.lang;

import java.util.Objects;

@FunctionalInterface
public interface TriplePredicate<A, B, C> {
    boolean test(A a, B b, C c);

    default TriplePredicate<A, B, C> and(TriplePredicate<? super A, ? super B, ? super C> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c) -> test(a, b, c) && other.test(a, b, c);
    }

    default TriplePredicate<A, B, C> negate() {
        return (A a, B b, C c) -> !test(a, b, c);
    }

    default TriplePredicate<A, B, C> or(TriplePredicate<? super A, ? super B, ? super C> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c) -> test(a, b, c) || other.test(a, b, c);
    }

    public static class NotNullTriplePredicate<A, B, C> implements TriplePredicate<A, B, C> {
        @Override
        public boolean test(A a, B b, C c) {
            return a != null || b != null || c != null;
        }
    }

    public static class NotNullTripleStringPredicate implements TriplePredicate<String, String, String> {
        @Override
        public boolean test(String a, String b, String c) {
            return StringUtils.isNotBlank(a) || StringUtils.isNotBlank(b) || StringUtils.isNotBlank(c);
        }
    }
}