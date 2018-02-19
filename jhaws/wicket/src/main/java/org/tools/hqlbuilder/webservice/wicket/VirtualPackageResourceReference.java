package org.tools.hqlbuilder.webservice.wicket;

import java.net.URL;

import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

public class VirtualPackageResourceReference extends StreamResourceReference {
    private static final long serialVersionUID = -7397543245367865603L;

    private String path;

    private Class<?> packageScope;

    public VirtualPackageResourceReference(Class<?> scope, String name, Class<?> packageScope, String path) {
        super(scope, name);
        this.packageScope = packageScope;
        this.path = path;
    }

    @Override
    public IResourceStream getResourceStream() {
        URL fullpath = packageScope.getClassLoader().getResource(packageScope.getPackage().getName().replace('.', '/') + "/" + path);
        return new UrlResourceStream(fullpath);
    }
}
