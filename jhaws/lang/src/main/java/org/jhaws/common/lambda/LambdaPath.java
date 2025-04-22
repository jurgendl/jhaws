package org.jhaws.common.lambda;


import org.jhaws.common.lang.functions.SerializableFunction;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

public class LambdaPath<C> implements Serializable {
    final Class<?> type;
    final String methodName;
    final LambdaPath<?> parent;

    private LambdaPath(Class<?> type, String methodName) {
        this(null, type, methodName);
    }

    private LambdaPath(LambdaPath<?> parent, Class<?> type, String methodName) {
        this.parent = parent;
        this.type = type;
        this.methodName = methodName;
    }

    public Class<?> getType() {
        return type;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFullPath() {
        LambdaPath<?> current = this;
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
        return (parent == null ? "" : parent + ">") + type.getName() + "#" + methodName;
    }

    public <D> LambdaPath<D> j(SerializableFunction<C, D> function) {
        return p(this, function);
    }

    public static <S, T> LambdaPath<T> p(SerializableFunction<S, T> function) {
        return p(null, function);
    }

    private static <S, T> LambdaPath<T> p(LambdaPath<?> parent, SerializableFunction<S, T> function) {
        SerializedLambda sf = getSerializedLambda(function);
        if (sf.getImplMethodKind() == 6) throw new IllegalArgumentException("expected LambdaFunction");
        String fn = sf.getImplMethodName();
        if (!fn.startsWith("get") && !fn.startsWith("set"))
            throw new IllegalArgumentException("expected getter or setter");
        fn = fn.substring(3);
        fn = Character.toLowerCase(fn.charAt(0)) + fn.substring(1);
        try {
            Class<?> c = Class.forName(sf.getImplClass().replace('/', '.'));
            return new LambdaPath<>(parent, c, fn);
        } catch (ClassNotFoundException e) {
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
