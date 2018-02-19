package org.tools.hqlbuilder.webservice.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;

import com.google.common.collect.Lists;

public class UrlResourceReference extends org.apache.wicket.request.resource.UrlResourceReference {
    private static final long serialVersionUID = -5775073797632156843L;

    protected final List<HeaderItem> dependencies = new ArrayList<>();

    protected final List<ResourceReference> dependenciesJavaScript = new ArrayList<>();

    protected final List<ResourceReference> dependenciesCss = new ArrayList<>();

    public UrlResourceReference(Url url) {
        super(url);
    }

    public UrlResourceReference addDependency(HeaderItem dependency) {
        dependencies.add(dependency);
        return this;
    }

    public UrlResourceReference addJavaScriptResourceReferenceDependency(ResourceReference dependency) {
        dependenciesJavaScript.add(dependency);
        return this;
    }

    public UrlResourceReference addCssResourceReferenceDependency(ResourceReference dependency) {
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
