package org.tools.hqlbuilder.webservice.jquery.ui.blazy;

import java.net.URI;
import java.net.URL;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.tools.hqlbuilder.webservice.wicket.components.ExternalImage;

import de.agilecoders.wicket.core.markup.html.bootstrap.behavior.CssClassNameAppender;

public class BLazyImage extends Image {
	private static final long serialVersionUID = 2477527041537407931L;

	protected IModel<?> url;

	public BLazyImage(String id, IResource imageResource) {
		super(id, imageResource);
		adjustImage(this);
	}

	public BLazyImage(String id, ResourceReference resourceReference) {
		super(id, resourceReference);
		adjustImage(this);
	}

	public BLazyImage(String id, ResourceReference resourceReference, PageParameters resourceParameters) {
		super(id, resourceReference, resourceParameters);
		adjustImage(this);
	}

	public BLazyImage(String id) {
		super(id);
		adjustImage(this);
	}

	public BLazyImage(String id, IModel<?> url) {
		this(id);
		this.url = url;
	}

	public BLazyImage(String id, URL url) {
		this(id, Model.of(url));
	}

	public BLazyImage(String id, URI uri) {
		this(id, Model.of(uri));
	}

	public BLazyImage(String id, String path) {
		this(id, Model.of(path));
	}

	public static Image adjustImage(Image image) {
		image.add(new CssClassNameAppender(BLazy.BLAZY_CLASS));
		return image;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if (url != null) {
			tag.put(ExternalImage.SRC, url.getObject().toString());
		}
		adjustTag(tag);
	}

	public static ComponentTag adjustTag(ComponentTag tag) {
		tag.getAttributes().put(BLazy.BLAZY_SRC, tag.getAttributes().getString(ExternalImage.SRC));
		tag.getAttributes().put(ExternalImage.SRC, "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==");
		return tag;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		if (!isEnabledInHierarchy()) {
			return;
		}
		adjustResponse(response);
	}

	public static IHeaderResponse adjustResponse(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(BLazy.BLAZY_JS));
		response.render(BLazy.BLAZY_FACTORY_JS);
		response.render(CssHeaderItem.forReference(BLazy.BLAZY_CSS));
		return response;
	}

	@Override
	protected boolean getStatelessHint() {
		return url != null || super.getStatelessHint();
	}
}
