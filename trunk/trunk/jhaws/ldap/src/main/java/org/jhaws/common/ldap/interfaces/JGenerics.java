package org.jhaws.common.ldap.interfaces;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * copy {@link util.classes.generics.JGenerics}
 * 
 * @author Jurgen De landsheer
 * 
 * @see util.classes.generics.JGenerics
 */
public class JGenerics {
    /**
     * NA
     * 
     * @param o NA
     * @param tvarindex NA
     * 
     * @return NA
     * 
     * @throws IllegalArgumentException NA
     */
    public static Class<?> findImplementation(Object o, int tvarindex) {
        Class<?> clazz = o.getClass();

        while (clazz.getSuperclass() != null) {
            Class<?> pojoClass = findImplementationForClass(clazz, tvarindex);

            if (null != pojoClass) {
                return pojoClass;
            }

            clazz = clazz.getSuperclass();
        }

        throw new IllegalArgumentException("no implementation found for generic type"); //$NON-NLS-1$
    }

    /**
     * NA
     * 
     * @param o NA
     * 
     * @return NA
     */
    public static Class<?> findImplementation(Object o) {
        return findImplementation(o, 0);
    }

    /**
     * NA
     * 
     * @param clazz NA
     * 
     * @return NA
     */
    public static Class<?> findImplementationForClass(Class<?> clazz) {
        return findImplementationForClass(clazz, 0);
    }

    /**
     * NA
     * 
     * @param clazz NA
     * @param tvarindex NA
     * 
     * @return NA
     */
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
}
