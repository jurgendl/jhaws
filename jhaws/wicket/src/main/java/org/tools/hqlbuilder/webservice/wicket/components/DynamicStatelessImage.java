package org.tools.hqlbuilder.webservice.wicket.components;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class DynamicStatelessImage extends Image {
    private static final long serialVersionUID = -607347340798582414L;

    protected ImageDataProvider imageDataProvider;

    public DynamicStatelessImage(String id, ImageDataProvider imageProvider) {
        this(DynamicStatelessImage.class, id, imageProvider);
    }

    public DynamicStatelessImage(Class<?> scope, String id, ImageDataProvider imageProvider) {
        super(id);
        this.imageDataProvider = imageProvider;
        setImageResource(new DynamicImageResource() {
            private static final long serialVersionUID = -9082465729974294243L;

            @Override
            protected byte[] getImageData(Attributes attributes) {
                return imageDataProvider.getImageData(attributes);
            }
        });
        setImageResourceReference(new ResourceReference(scope, imageProvider.getName()) {
            private static final long serialVersionUID = -3929292825497004941L;

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
