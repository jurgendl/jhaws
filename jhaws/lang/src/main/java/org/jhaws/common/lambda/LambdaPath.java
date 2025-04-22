package org.jhaws.common.lambda;


import org.jhaws.common.lang.functions.SerializableFunction;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

public class LambdaPath<S, T> implements Serializable {
    final Class<S> type;
    final String methodName;
    final Class<T> methodResultType;
    final LambdaPath<?, ?> parent;

    private LambdaPath(LambdaPath<?, ?> parent, Class<S> type, String methodName, Class<T> methodResultType) {
        this.parent = parent;
        this.type = type;
        this.methodName = methodName;
        this.methodResultType = methodResultType;
    }

    public Class<?> getType() {
        return type;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> getMethodResultType() {
        return methodResultType;
    }

    public String getFullPath() {
        LambdaPath<?, ?> current = this;
        Stack<String> path = new Stack<>();
        while (current != null) {
            path.add(current.methodName);
            current = current.parent;
        }
        StringBuilder pathBuilder = new StringBuilder();
        while (!path.isEmpty()) {
            pathBuilder.append(path.pop());
            if (!path.isEmpty()) pathBuilder.append(".");
        }
        return pathBuilder.toString();
    }

    @Override
    public String toString() {
        return (parent == null ? "" : parent + " > ") + type.getName() + " # " + methodResultType.getName() + " " + methodName;
    }

    public <U> LambdaPath<T, U> j(SerializableFunction<T, U> function) {
        return p(this, function);
    }

    public static <S, T> LambdaPath<S, T> p(SerializableFunction<S, T> function) {
        return p(null, function);
    }

    private static <S, T> LambdaPath<S, T> p(LambdaPath<?, ?> parent, SerializableFunction<S, T> function) {
        SerializedLambda sf = getSerializedLambda(function);
        if (sf.getImplMethodKind() == 6) throw new IllegalArgumentException("expected LambdaFunction");
        String methodName = sf.getImplMethodName();
        if (!methodName.startsWith("get") && !methodName.startsWith("set"))
            throw new IllegalArgumentException("expected getter or setter");
        methodName = methodName.substring(3);
        methodName = Character.toLowerCase(methodName.charAt(0)) + methodName.substring(1);
        try {
            Class<S> type = (Class<S>) Class.forName(sf.getImplClass().replace('/', '.'));
            Class<T> returnType = (Class<T>) type.getDeclaredMethod(sf.getImplMethodName()).getReturnType();
            return new LambdaPath<>(parent, type, methodName, returnType);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> SerializedLambda getSerializedLambda(SerializableFunction<T, ?> function) {
        try {
            Method writeReplace = function.getClass().getDeclaredMethod("writeReplace");
            writeReplace.trySetAccessible();
            return (SerializedLambda) writeReplace.invoke(function);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
