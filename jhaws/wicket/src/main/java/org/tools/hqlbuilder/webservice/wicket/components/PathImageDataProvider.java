package org.tools.hqlbuilder.webservice.wicket.components;

import java.nio.file.Path;

import org.apache.wicket.request.resource.IResource.Attributes;
import org.jhaws.common.io.FilePath;

@SuppressWarnings("serial")
public class PathImageDataProvider implements ImageDataProvider {
	private FilePath path;

	public PathImageDataProvider(Path path) {
		this.path = new FilePath(path);
	}

	@Override
	public byte[] getImageData(Attributes attributes) {
		return path.readAllBytes();
	}

	@Override
	public String getName() {
		return path.getName();
	}

	@Override
	public String getFormat() {
		return "image/" + path.getExtension();
	}

}
