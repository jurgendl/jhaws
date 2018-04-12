package org.jhaws.common.lang;

import java.util.Objects;

@FunctionalInterface
public interface QuadruplePredicate<A, B, C, D> {
    boolean test(A a, B b, C c, D d);

    default QuadruplePredicate<A, B, C, D> and(QuadruplePredicate<? super A, ? super B, ? super C, ? super D> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c, D d) -> test(a, b, c, d) && other.test(a, b, c, d);
    }

    default QuadruplePredicate<A, B, C, D> negate() {
        return (A a, B b, C c, D d) -> !test(a, b, c, d);
    }

    default QuadruplePredicate<A, B, C, D> or(QuadruplePredicate<? super A, ? super B, ? super C, ? super D> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c, D d) -> test(a, b, c, d) || other.test(a, b, c, d);
    }

    public static class NotNullQuadruplePredicate<A, B, C, D> implements QuadruplePredicate<A, B, C, D> {
        @Override
        public boolean test(A a, B b, C c, D d) {
            return a != null || b != null || c != null || d != null;
        }
    }

    public static class NotNullQuadrupleStringPredicate implements QuadruplePredicate<String, String, String, String> {
        @Override
        public boolean test(String a, String b, String c, String d) {
            return StringUtils.isNotBlank(a) || StringUtils.isNotBlank(b) || StringUtils.isNotBlank(c) || StringUtils.isNotBlank(d);
        }
    }
}