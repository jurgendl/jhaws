package org.jhaws.common.lang;

import java.util.Objects;

@FunctionalInterface
public interface QuintuplePredicate<A, B, C, D, E> {
    boolean test(A a, B b, C c, D d, E e);

    default QuintuplePredicate<A, B, C, D, E> and(QuintuplePredicate<? super A, ? super B, ? super C, ? super D, ? super E> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c, D d, E e) -> test(a, b, c, d, e) && other.test(a, b, c, d, e);
    }

    default QuintuplePredicate<A, B, C, D, E> negate() {
        return (A a, B b, C c, D d, E e) -> !test(a, b, c, d, e);
    }

    default QuintuplePredicate<A, B, C, D, E> or(QuintuplePredicate<? super A, ? super B, ? super C, ? super D, ? super E> other) {
        Objects.requireNonNull(other);
        return (A a, B b, C c, D d, E e) -> test(a, b, c, d, e) || other.test(a, b, c, d, e);
    }

    public static class NotNullQuintuplePredicate<A, B, C, D, E> implements QuintuplePredicate<A, B, C, D, E> {
        @Override
        public boolean test(A a, B b, C c, D d, E e) {
            return a != null || b != null || c != null || d != null || e != null;
        }
    }

    public static class NotNullQuintupleStringPredicate implements QuintuplePredicate<String, String, String, String, String> {
        @Override
        public boolean test(String a, String b, String c, String d, String e) {
            return StringUtils.isNotBlank(a) || StringUtils.isNotBlank(b) || StringUtils.isNotBlank(c) || StringUtils.isNotBlank(d)
                    || StringUtils.isNotBlank(e);
        }
    }
}