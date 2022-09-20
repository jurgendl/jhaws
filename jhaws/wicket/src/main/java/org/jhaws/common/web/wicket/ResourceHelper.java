package org.jhaws.common.web.wicket;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.template.TextTemplate;

public class ResourceHelper {
    public static String load(Class<?> path, String name, Map<String, ?> variables) {
        System.out.println(path + " - " + name + " - " + variables);
        TextTemplate template = new PackageTextTemplate(path, name.toString());
        if (variables != null) {
            template.interpolate(variables);
        }
        String asString = template.asString();
        try {
            template.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
        return asString;
    }

    public static String load(Class<?> path, String name) {
        return load(path, name, null);
    }

    public static String loadJs(Class<?> path, Map<String, ?> variables) {
        return load(path, path.getSimpleName() + ".js", variables);
    }

    public static String loadJs(Class<?> path) {
        return load(path, path.getSimpleName() + ".js", null);
    }

    public static String loadJsFactory(Class<?> path, Map<String, ?> variables) {
        return load(path, path.getSimpleName() + "-factory.js", variables);
    }

    public static String loadJsFactory(Class<?> path) {
        return load(path, path.getSimpleName() + "-factory.js", null);
    }

    public static String loadCss(Class<?> path, Map<String, ?> variables) {
        return load(path, path.getSimpleName() + ".css", variables);
    }

    public static String loadCss(Class<?> path) {
        return load(path, path.getSimpleName() + ".css", null);
    }

}
