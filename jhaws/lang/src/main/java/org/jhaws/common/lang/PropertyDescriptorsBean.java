package org.jhaws.common.lang;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.matches;
import static java.util.stream.Collectors.toMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import static java.util.stream.Stream.*;

public class PropertyDescriptorsBean {
    private static final String SPLIT = "_";

    private final Map<String, PropertyDescriptor> propertyDescriptors;

    private final Map<String, Method> get = new HashMap<>();

    private final Map<String, Method> set = new HashMap<>();

    private final Map<String, Method> is = new HashMap<>();

    public PropertyDescriptorsBean(Class<?> clazz) {
        Predicate<Method> mf = ((Predicate<Method>) m -> isPublic(m.getModifiers())).and(m -> !isStatic(m.getModifiers()))
                .and(m -> !isAbstract(m.getModifiers()));
        Class<?> current = clazz;
        while (!current.equals(Object.class) && current != null) {
            get.putAll(stream(current.getDeclaredMethods())//
                    .parallel()
                    .filter(mf)
                    .filter(m -> matches("^get.+", m.getName()))
                    .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 0)
                    .filter(m -> !get.containsKey(getKey(m)))
                    .collect(toMap(this::getKey, identity())));
            is.putAll(stream(current.getDeclaredMethods())//
                    .parallel()
                    .filter(mf)
                    .filter(m -> matches("^is.+", m.getName()))
                    .filter(m -> !Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 0)
                    .filter(m -> !is.containsKey(isKey(m)))
                    .collect(toMap(this::isKey, identity())));
            set.putAll(stream(current.getDeclaredMethods())//
                    .parallel()
                    .filter(mf)
                    .filter(m -> matches("^set.+", m.getName()))
                    .filter(m -> Void.TYPE.isAssignableFrom(m.getReturnType()))
                    .filter(m -> m.getParameterTypes().length == 1)
                    .filter(m -> !set.containsKey(setKey(m)))
                    .collect(toMap(this::setKey, identity())));
            current = current.getSuperclass();
        }

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
