package org.jhaws.common.web.wicket;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jhaws.common.lambda.LambdaPath;
import org.jhaws.common.lang.EnhancedRunnable;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


public class WebHelper {
    // static public class LocalDateArgumentCreator implements FinalClassArgumentCreator<LocalDate> {
    // // Localdate houdt intern een aantal ms bij dat afgerond is tot op niveau van een dag, we moeten dus zorgen dat elke seed wordt
    // // vermenigvuldigd
    // // met het aantal ms in een dag om telkens een andere waarde te krijgen
    // private final long MSECS_IN_DAY = 1000L * 60L * 60L * 24L;
    //
    // public LocalDate createArgumentPlaceHolder(int seed) {
    // return new LocalDate(seed * MSECS_IN_DAY);
    // }
    // }


    public static <A> Class<A> type(LambdaPath<?, ?> arg) {
        return (Class<A>) arg.getMethodResultType();
    }

    /**
     * welke generic type implementatie heeft een class implementatie (werkt ook op anonymous inner classes)
     *
     * @param object                          de implementatie class (this waar opgeroepen)
     * @param classOrInterfaceWithGenericType class of interface waardat de generic type naam op gedeclareerd staat
     * @param genericTypeIndex                de index binnen de &lt; en &gt; waar de generic type naam op gedeclareerd staat; als er maar 1 is wordt die zowiezo genomen (maw index 0)
     * @param requiredGenericType             vereiste class (alternatief voor index)
     * @return de effectieve implementatie
     * @throws IllegalArgumentException wanneer index niet gevonden wordt of geen implementatie van class c
     */
    @SuppressWarnings("unchecked")
    private static <P, G extends P> Class<G> _getImplementation(Object object, Class<?> classOrInterfaceWithGenericType, int genericTypeIndex, Class<P> requiredGenericType) throws IllegalArgumentException {
        if (object == null || classOrInterfaceWithGenericType == null) {
            throw new NullPointerException();
        }

        Class<?> clazz = object instanceof Class ? Class.class.cast(object) : object.getClass();
        ParameterizedType parameterizedType = null;

        while (parameterizedType == null && clazz != null) {
            for (Type type : clazz.getGenericInterfaces()) {
                if (type instanceof ParameterizedType && ParameterizedType.class.cast(type).getRawType().equals(classOrInterfaceWithGenericType)) {
                    parameterizedType = ParameterizedType.class.cast(type);
                }
            }

            if (parameterizedType == null) {
                Type type = clazz.getGenericSuperclass();
                if (type instanceof ParameterizedType && ParameterizedType.class.cast(type).getRawType().equals(classOrInterfaceWithGenericType)) {
                    parameterizedType = ParameterizedType.class.cast(clazz.getGenericSuperclass());
                }
            }

            if (parameterizedType == null) {
                clazz = clazz.getSuperclass();
            }
        }

        if (parameterizedType == null) {
            throw new IllegalArgumentException("no implementation");
        }

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        if (genericTypeIndex == -1 && actualTypeArguments.length == 1) {
            return (Class<G>) actualTypeArguments[0];
        }

        if (genericTypeIndex == -1) {
            if (requiredGenericType == null) {
                throw new IllegalArgumentException("requiredGenericType", new NullPointerException());
            }

            for (Type actualTypeArgument : actualTypeArguments) {
                if (requiredGenericType.isAssignableFrom((Class<?>) actualTypeArgument)) {
                    return (Class<G>) actualTypeArgument;
                }
            }
            throw new IllegalArgumentException(Arrays.toString(actualTypeArguments) + "<>" + requiredGenericType);
        }

        try {
            Type type = actualTypeArguments[genericTypeIndex];
            if (type instanceof ParameterizedType) {
                return (Class<G>) ((ParameterizedType) type).getRawType();
            }
            return (Class<G>) type;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(Arrays.toString(actualTypeArguments), ex);
        }
    }

    /**
     * @see #getImplementation(Object, Class, int) met index=-1
     */
    public static <G> Class<G> getImplementation(Object object, Class<?> classOrInterfaceWithGenericType) throws IllegalArgumentException {
        return _getImplementation(object, classOrInterfaceWithGenericType, -1, null);
    }

    /**
     * @see #getImplementation(Object, Class, int)
     */
    public static <G> Class<G> getImplementation(Object object, Class<?> classOrInterfaceWithGenericType, int genericTypeIndex) throws IllegalArgumentException {
        return _getImplementation(object, classOrInterfaceWithGenericType, genericTypeIndex, null);
    }

    /**
     * @see #getImplementation(Object, Class, int)
     */
    public static <P, G extends P> Class<G> getImplementation(Object object, Class<?> classOrInterfaceWithGenericType, Class<P> requiredGenericType) throws IllegalArgumentException {
        return _getImplementation(object, classOrInterfaceWithGenericType, -1, requiredGenericType);
    }

    public static <A> String name(LambdaPath<?, ?> arg) {
        return arg.getFullPath();
    }

    public static void run(EnhancedRunnable runnable) {
        run(null, runnable, Exception::printStackTrace);
    }

    public static void run(Long sleep, EnhancedRunnable runnable, Consumer<Exception> exceptionHandler) {
        Thread thread = new Thread(() -> {
            boolean eternal = sleep != null;
            try {
                do {
                    runnable.run();
                    if (sleep != null) {
                        sleep(sleep);
                    }
                } while (eternal);
            } catch (Exception ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            //
        }
    }

    public static <T> T create(Class<T> modelType) {
        return BeanUtils.instantiateClass(modelType);
    }

    public static <T> IModel<T> model(Class<T> modelType) {
        return model(create(modelType));
    }

    public static <T> IModel<T> model(T model) {
        return new CompoundPropertyModel<>(model);
    }

    public static <C extends Component> C show(C component) {
        if (component == null) return component;
        component.setOutputMarkupPlaceholderTag(true);
        component.setRenderBodyOnly(false);
        component.setOutputMarkupId(true);
        return component;
    }

    public static <C extends Component> C hide(C component) {
        if (component == null) return component;
        component.setOutputMarkupPlaceholderTag(false);
        component.setRenderBodyOnly(true);
        component.setOutputMarkupId(false);
        return component;
    }

    public static void untag(ComponentTag tag, String tagId) {
        tag(tag, tagId, null);
    }

    public static void tag(ComponentTag tag, String tagId, Object value) {
        if (value == null || (value instanceof String && StringUtils.isBlank(String.class.cast(value)))) {
            tag.getAttributes().remove(tagId);
        } else {
            tag.getAttributes().put(tagId, value);
        }
    }

    public static void classAdd(ComponentTag tag, String clazz) {
        tag.getAttributes().put("class", tag.getAttributes().getString("class") + " " + clazz.toString());
    }

    public static PageParameters clone(PageParameters pageParameters, String... ids) {
        PageParameters pp = new PageParameters();
        for (String id : ids) {
            if (StringUtils.isNotBlank(pageParameters.get(id).toOptionalString())) {
                pp.add(id, pageParameters.get(id).toOptionalString());
            }
        }
        return pp;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static IModel<List<String>> model(String... options) {
        return (IModel) Model.of(Arrays.asList(options));
    }
}
