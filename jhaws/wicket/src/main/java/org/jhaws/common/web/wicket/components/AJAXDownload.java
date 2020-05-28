package org.jhaws.common.web.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * @author Sven Meier
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 * @author Jordi Deu-Pons (jordi@jordeu.net)
 */
public abstract class AJAXDownload extends AbstractAjaxBehavior {
	private static final long serialVersionUID = 6288222945100417920L;

	private boolean addAntiCache;

	public AJAXDownload() {
		this(true);
	}

	public AJAXDownload(boolean addAntiCache) {
		super();
		this.addAntiCache = addAntiCache;
	}

	/**
	 * Call this method to initiate the download.
	 */
	public void initiate(AjaxRequestTarget target) {
		String url = getCallbackUrl().toString();

		if (addAntiCache) {
			url = url + (url.contains("?") ? "&" : "?");
			url = url + "antiCache=" + System.currentTimeMillis();
		}

		// the timeout is needed to let Wicket release the channel
		target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");
	}

	@Override
	public void onRequest() {
		ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(getResourceStream(), getFileName());
		handler.setContentDisposition(ContentDisposition.ATTACHMENT);
		getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
	}

	/**
	 * Override this method for a file name which will let the browser prompt
	 * with a save/open dialog.
	 *
	 * @see ResourceStreamRequestTarget#getFileName()
	 */
	protected String getFileName() {
		return null;
	}

	/**
	 * Hook method providing the actual resource stream.
	 */
	protected abstract IResourceStream getResourceStream();
}