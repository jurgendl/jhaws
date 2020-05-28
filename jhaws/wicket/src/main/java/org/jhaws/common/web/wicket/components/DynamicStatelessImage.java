package org.jhaws.common.web.wicket.components;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

@SuppressWarnings("serial")
public class DynamicStatelessImage extends Image {
	protected ImageDataProvider imageDataProvider;

	public DynamicStatelessImage(String id, ImageDataProvider imageProvider) {
		this(DynamicStatelessImage.class, id, imageProvider);
	}

	public DynamicStatelessImage(Class<?> scope, String id, ImageDataProvider imageProvider) {
		super(id);
		this.imageDataProvider = imageProvider;
		setImageResource(new DynamicImageResource() {
			@Override
			protected byte[] getImageData(Attributes attributes) {
				return imageDataProvider.getImageData(attributes);
			}
		});
		setImageResourceReference(new ResourceReference(scope, imageProvider.getName()) {
			@Override
			public IResource getResource() {
				return getImageResource();
			}
		});
	}

	@Override
	public IResource getImageResource() {
		return super.getImageResource();
	}

	@Override
	public ResourceReference getImageResourceReference() {
		return super.getImageResourceReference();
	}
}
