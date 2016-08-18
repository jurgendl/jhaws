package org.tools.hqlbuilder.webservice.wicket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.ResourceReference;

import com.google.common.collect.Lists;

public class JavaScriptResourceReference extends org.apache.wicket.request.resource.JavaScriptResourceReference {
	private static final long serialVersionUID = -482388159288147046L;

	public JavaScriptResourceReference(Class<?> scope, String name, Locale locale, String style, String variation) {
		super(scope, name, locale, style, variation);
	}

	public JavaScriptResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	public JavaScriptResourceReference(Key key) {
		super(key);
	}

	protected final List<HeaderItem> dependencies = new ArrayList<>();

	protected final List<ResourceReference> dependenciesJavaScript = new ArrayList<>();

	protected final List<ResourceReference> dependenciesCss = new ArrayList<>();

	public JavaScriptResourceReference addDependency(HeaderItem dependency) {
		dependencies.add(dependency);
		return this;
	}

	public JavaScriptResourceReference addJavaScriptResourceReferenceDependency(ResourceReference dependency) {
		dependenciesJavaScript.add(dependency);
		return this;
	}

	public JavaScriptResourceReference addCssResourceReferenceDependency(ResourceReference dependency) {
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
