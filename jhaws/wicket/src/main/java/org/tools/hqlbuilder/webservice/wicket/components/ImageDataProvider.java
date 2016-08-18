package org.tools.hqlbuilder.webservice.wicket.components;

import java.io.Serializable;

import org.apache.wicket.request.resource.IResource.Attributes;

public interface ImageDataProvider extends Serializable {
    public abstract byte[] getImageData(Attributes attributes);

    public abstract String getName();

    public abstract String getFormat();
}