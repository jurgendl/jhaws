package org.tools.hqlbuilder.webservice.wicket.components;

import java.net.URI;
import java.net.URL;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @see https://cwiki.apache.org/confluence/display/WICKET/How+to+load+an+external+image
 */
public class ExternalImage extends Image {
    private static final long serialVersionUID = 7113713255809950318L;

    public static final String SRC = "src";

    protected IModel<?> url;

    protected Integer width;

    protected Integer height;

    public ExternalImage(String id, String path) {
        this(id, Model.of(path));
    }

    public ExternalImage(String id, URI uri) {
        this(id, Model.of(uri));
    }

    public ExternalImage(String id, URL url) {
        this(id, Model.of(url));
    }

    public ExternalImage(String id, Model<?> path) {
        super(id);
        url = Model.of(path);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        tag.put(SRC, url.getObject().toString());
        if (width != null) tag.put("width", width);
        if (height != null) tag.put("height", height);
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
