package org.jhaws.common.lang;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class PropertyDescriptorsBean {
    private static final String SPLIT = "_";

    private final Map<String, PropertyDescriptor> propertyDescriptors;

    private final Map<String, Method> get = new HashMap<>();

    private final Map<String, Method> set = new HashMap<>();

    private final Map<String, Method> is = new HashMap<>();

    public static class BufferFunction<T, R> implements Function<T, R> {
        private final Function<T, R> delegate;

        private T initialValue;

        private R returnValue;

        public BufferFunction(Function<T, R> delegate) {
            this.delegate = delegate;
        }

        @Override
        public R apply(T initialValue) {
            if (returnValue == null || initialValue != this.initialValue)
                returnValue = delegate.apply(this.initialValue = initialValue);
            return returnValue;
        }
    }

    public static class Jumper<T> implements Iterator<T> {
        private final T initial;

        private final Function<T, T> nextItem;

        private final Predicate<T> accept;

        private T current = null;

        public Jumper(T initial, Function<T, T> nextItem) {
            this(initial, nextItem, t -> true);
        }

        public Jumper(T initial, Function<T, T> nextItem, Predicate<T> accept) {
            Objects.nonNull(initial);
            this.initial = initial;
            this.nextItem = new BufferFunction<>(nextItem);
            this.accept = accept;
        }

        @Override
        public boolean hasNext() {
            if (current == null) {
                return accept.test(initial);
            }
            T next = nextItem.apply(current);
            return next != null && accept.test(next);
        }

        @Override
        public T next() {
            if (current == null) {
                return current = initial;
            }
            T next = nextItem.apply(current);
            return current = next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    public PropertyDescriptorsBean(Class<?> clazz) {
        Predicate<Method> mf = ((Predicate<Method>) m -> isPublic(m.getModifiers())).and(m -> !isStatic(m.getModifiers()))
                .and(m -> !isAbstract(m.getModifiers()));
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Jumper<Class<?>>(clazz, Class::getSuperclass, c -> !Object.class.equals(c)), 0),
                false).forEach(current -> {
                    get.putAll(stream(current.getDeclaredMethods())//
                            .parallel()
                            .filter(mf)
                            .filter(m -> m.getName().matches("^get.+"))
                            .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                            .filter(m -> m.getParameterTypes().length == 0)
                            .filter(m -> !get.containsKey(getKey(m)))
                            .collect(toMap(this::getKey, identity())));
                    is.putAll(stream(current.getDeclaredMethods())//
                            .parallel()
                            .filter(mf)
                            .filter(m -> m.getName().matches("^is.+"))
                            .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                            .filter(m -> m.getParameterTypes().length == 0)
                            .filter(m -> !is.containsKey(isKey(m)))
                            .collect(toMap(this::isKey, identity())));
                    set.putAll(stream(current.getDeclaredMethods())//
                            .parallel()
                            .filter(mf)
                            .filter(m -> m.getName().matches("^set.+"))
                            .filter(m -> Void.TYPE.isAssignableFrom(m.getReturnType()))
                            .filter(m -> m.getParameterTypes().length == 1)
                            .filter(m -> !set.containsKey(setKey(m)))
                            .collect(toMap(this::setKey, identity())));
                });
        this.propertyDescriptors = unmodifiableMap(of(get, is)//
                .map(Map::entrySet)//
                .flatMap(Collection::stream)
                .map(this::create)
                .filter(p -> p != null)
                .collect(toMap(PropertyDescriptor::getName, identity(), (p1, p2) -> p1)));
    }

    private String getKey(Method m) {
        return m.getName().substring(3) + SPLIT + m.getReturnType().getName();
    }

    private String isKey(Method m) {
        return m.getName().substring(2) + SPLIT + m.getReturnType().getName();
    }

    private String setKey(Method m) {
        return m.getName().substring(3) + SPLIT + m.getParameterTypes()[0].getName();
    }

    private PropertyDescriptor create(Entry<String, Method> e) {
        try {
            return new PropertyDescriptor(getPropertyName(e.getKey()), e.getValue(), set.get(e.getKey()));
        } catch (Exception ignore) {
            return null;
        }
    }

    private String getPropertyName(String key) {
        String[] p = key.split(SPLIT);
        return Character.toLowerCase(p[0].charAt(0)) + p[0].substring(1);
    }

    public Map<String, PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
