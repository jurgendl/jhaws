package org.jhaws.common.lang;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ClassUtils {
    public static interface ApiMethodCollector {
        Collection<Method> collect(Class<?> clazz);
    }

    public static class Default8ApiMethodCollector implements ApiMethodCollector {
        @Override
        public Collection<Method> collect(Class<?> clazz) {
            return getAllMethods(clazz, false, false);
        }
    }

    /**
     * @see http://stackoverflow.com/questions/28400408/what-is-the-new-way-of-getting-all-methods-of-a-class-including-inherited-defau
     */
    public static Collection<Method> getAllMethods(Class<?> clazz, boolean includeAllPackageAndPrivateMethodsOfSuperclasses,
            boolean includeOverridenAndHidden) {
        Predicate<Method> include = m -> !m.isBridge() && !m.isSynthetic() && Character.isJavaIdentifierStart(m.getName().charAt(0))
                && m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

        Set<Method> methods = new LinkedHashSet<>();
        Collections.addAll(methods, clazz.getMethods());
        methods.removeIf(include.negate());
        Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);

        final int access = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

        Package p = clazz.getPackage();
        if (!includeAllPackageAndPrivateMethodsOfSuperclasses) {
            int pass = includeOverridenAndHidden ? Modifier.PUBLIC | Modifier.PROTECTED : Modifier.PROTECTED;
            include = include.and(m -> (m.getModifiers() & pass) != 0 || (m.getModifiers() & access) == 0 && m.getDeclaringClass().getPackage() == p);
        }
        if (!includeOverridenAndHidden) {
            Map<Object, Set<Package>> types = new HashMap<>();
            final Set<Package> pkgIndependent = Collections.emptySet();
            for (Method m : methods) {
                int acc = m.getModifiers() & access;
                if (acc == Modifier.PRIVATE) continue;
                if (acc != 0)
                    types.put(methodKey(m), pkgIndependent);
                else
                    types.computeIfAbsent(methodKey(m), x -> new HashSet<>()).add(p);
            }
            include = include.and(m -> {
                int acc = m.getModifiers() & access;
                return acc != 0 ? acc == Modifier.PRIVATE || types.putIfAbsent(methodKey(m), pkgIndependent) == null
                        : noPkgOverride(m, types, pkgIndependent);
            });
        }
        for (clazz = clazz.getSuperclass(); clazz != null; clazz = clazz.getSuperclass())
            Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);
        return methods;
    }

    private static boolean noPkgOverride(Method m, Map<Object, Set<Package>> types, Set<Package> pkgIndependent) {
        Set<Package> pkg = types.computeIfAbsent(methodKey(m), key -> new HashSet<>());
        return pkg != pkgIndependent && pkg.add(m.getDeclaringClass().getPackage());
    }

    private static Object methodKey(Method m) {
        return Arrays.asList(m.getName(), MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
    }

    public static Class<?> findImplementation(Object o) {
        return ClassUtils.findImplementation(o, 0);
    }

    public static Class<?> findImplementation(Object o, int tvarindex) {
        Class<?> clazz = o.getClass();

        while (clazz.getSuperclass() != null) {
            Class<?> pojoClass = ClassUtils.findImplementationForClass(clazz, tvarindex);

            if (null != pojoClass) {
                return pojoClass;
            }

            clazz = clazz.getSuperclass();
        }

        throw new IllegalArgumentException("no implementation found for generic type"); //$NON-NLS-1$
    }

    public static Class<?> findImplementationForClass(Class<?> clazz) {
        return ClassUtils.findImplementationForClass(clazz, 0);
    }

    public static Class<?> findImplementationForClass(Class<?> clazz, int tvarindex) {
        Type genericSuperClazz = clazz.getGenericSuperclass();

        if (genericSuperClazz instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericSuperClazz;
            Type[] typeArguments = pt.getActualTypeArguments();
            Type typeArgument = typeArguments[tvarindex];

            // for (Type typeArgument : typeArguments) {
            // Class<?> classOfTypeArgument = typeArgument.getClass();
            if (!(typeArgument instanceof TypeVariable)) {
                Class<?> typeArgumentsClass = (Class<?>) typeArgument;

                return typeArgumentsClass;
            } /*
               * else { TypeVariable typeArgumentsTypeVariable = (TypeVariable) typeArgument; GenericDeclaration
               * typeArgumentsTypeVariableGenericDeclaration = typeArgumentsTypeVariable.getGenericDeclaration(); Type[]
               * typeArgumentsTypeVariableBounds = typeArgumentsTypeVariable.getBounds(); for (Type typeArgumentsTypeVariableBound :
               * typeArgumentsTypeVariableBounds) { //ignore } }
               */
            // }
        }

        return null;
    }

    public static Object create(String className) {
        return create(className, new Class[0], new Object[0]);
    }

    public static Object create(String className, Class<?> parameterTypes, Object initargs) {
        return create(className, new Class[] { parameterTypes }, new Object[] { initargs });
    }

    public static Object create(String className, Object initargs) {
        return create(className, new Class[] { initargs.getClass() }, new Object[] { initargs });
    }

    public static Object create(String className, Object[] initargs) {
        @SuppressWarnings("rawtypes")
        Class[] parameterTypes = new Class[initargs.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypes[i] = initargs[i].getClass();
        }
        return create(className, parameterTypes, initargs);
    }

    public static Object create(String className, @SuppressWarnings("rawtypes") Class[] parameterTypes, Object[] initargs) {
        try {
            return Class.forName(className).getConstructor(parameterTypes).newInstance(initargs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
