package org.jhaws.common.io.resources;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

public class ResourceUtils {
    public static Manifest manifest(Class<?> clazz) throws IOException {
        URL resource = clazz.getResource("/" + clazz.getName().replace(".", "/") + ".class");
        // System.out.println(resource);
        String resourceBase = resource.toString().replace(clazz.getName().replace(".", "/") + ".class", "");
        // System.out.println(resourceBase);
        Enumeration<URL> resources = clazz.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            try {
                URL nextElement = resources.nextElement();
                if (nextElement.toString().equals(resourceBase + "META-INF/MANIFEST.MF")) {
                    // System.out.println(nextElement);
                    Manifest manifest = new Manifest(nextElement.openStream());
                    return manifest;
                }
            } catch (IOException E) {
                //
            }
        }
        return null;
    }
}
