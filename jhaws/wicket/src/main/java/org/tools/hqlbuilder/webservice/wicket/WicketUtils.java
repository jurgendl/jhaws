package org.tools.hqlbuilder.webservice.wicket;

import java.nio.file.Path;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

public class WicketUtils {
	public static String mountFileStream(Class<?> root, String name, Path path) {
		return WicketUtils.mountStream(name, new FileStreamResourceReference(root, name, path));
	}

	public static String mountFileStream(Path path) {
		return WicketUtils.mountFileStream(path.getFileName().toString().replace(" ", "_"), path);
	}

	public static String mountFileStream(String name, Path path) {
		return WicketUtils.mountFileStream(WicketUtils.class, name, path);
	}

	public static String mountStream(String name, ResourceReference resref) {
		WicketApplication.get().getSharedResources().add(name, resref.getResource());
		return RequestCycle.get().urlFor(resref, new PageParameters()).toString();
	}
}
