package org.tools.hqlbuilder.webservice.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.resource.IResourceStream;

import com.google.common.collect.Lists;

public abstract class StreamResourceReference extends PackageResourceReference {
    private static final long serialVersionUID = 2445853607747904603L;

    public StreamResourceReference(Class<?> scope, String name) {
        super(scope, name);
    }

    @Override
    public PackageResource getResource() {
        PackageResource resource = new PackageResource(getScope(), getName(), getLocale(), getStyle(), getVariation()) {
            private static final long serialVersionUID = -1135314184370523460L;

            @Override
            public IResourceStream getResourceStream() {
                return StreamResourceReference.this.getResourceStream();
            }
        };
        removeCompressFlagIfUnnecessary(resource);
        return resource;
    }

    public abstract IResourceStream getResourceStream();

    protected final List<HeaderItem> dependencies = new ArrayList<>();

    protected final List<ResourceReference> dependenciesJavaScript = new ArrayList<>();

    protected final List<ResourceReference> dependenciesCss = new ArrayList<>();

    public StreamResourceReference addDependency(HeaderItem dependency) {
        dependencies.add(dependency);
        return this;
    }

    public StreamResourceReference addJavaScriptResourceReferenceDependency(ResourceReference dependency) {
        dependenciesJavaScript.add(dependency);
        return this;
    }

    public StreamResourceReference addCssResourceReferenceDependency(ResourceReference dependency) {
        dependenciesCss.add(dependency);
        return this;
    }

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
        List<HeaderItem> l = Lists.newArrayList(super.getDependencies());
        l.addAll(dependencies);
        for (ResourceReference dependency : dependenciesJavaScript) {
            l.add(JavaScriptHeaderItem.forReference(dependency));
        }
        for (ResourceReference dependency : dependenciesCss) {
            l.add(CssHeaderItem.forReference(dependency));
        }
        return l;
    }
}
