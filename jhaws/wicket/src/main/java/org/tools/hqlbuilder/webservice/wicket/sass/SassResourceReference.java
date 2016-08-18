package org.tools.hqlbuilder.webservice.wicket.sass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.WicketApplication;

import ro.isdc.wro.extensions.processor.css.RubySassCssProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.CssCompressorProcessor;

/**
 * response.render(CssHeaderItem.forReference(new SassResourceReference(WicketCSSRoot.class, "table.sass")));
 *
 * @see http://sass-lang.com/
 * @see https://code.google.com/p/wro4j/
 */
public class SassResourceReference extends StreamResourceReference implements IResourceStream {
	private static final long serialVersionUID = 6384603768717480808L;

	protected static final Logger logger = LoggerFactory.getLogger(SassResourceReference.class);

	protected final String charset = "utf-8";

	protected final String contentType = "text/css";

	protected transient Bytes length = null;

	protected transient String css = "";

	protected transient Time lastModified = null;

	protected transient ResourcePreProcessor sassCssProcessor;

	protected transient ResourcePreProcessor cssCompressorProcessor;

	public SassResourceReference(Class<?> scope, String name) {
		super(scope, name);
	}

	@Override
	public void close() throws IOException {
		//
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	public ResourcePreProcessor getCssCompressorProcessor() {
		if (this.cssCompressorProcessor == null) {
			this.cssCompressorProcessor = new CssCompressorProcessor();
		}
		return this.cssCompressorProcessor;
	}

	@Override
	public InputStream getInputStream() throws ResourceStreamNotFoundException {
		try {
			boolean rebuild = false;
			String fullPath = this.getResourcePath() + '/' + this.getSassName();
			if (this.css == null) {
				SassResourceReference.logger.info("building " + fullPath + " because is new");
				rebuild = true;
			} else
				if (this.lastModified == null) {
					SassResourceReference.logger.info("building " + fullPath + " because out of date");
					rebuild = true;
				} else
					if (WicketApplication.get().usesDevelopmentConfig()) {
						try {
							if (this.lastModified.getMilliseconds() < this.getClass().getClassLoader().getResource(fullPath).openConnection().getLastModified()) {
								SassResourceReference.logger.info("building " + fullPath + " because template out of date");
								rebuild = true;
							}
						} catch (IOException ex1) {
							SassResourceReference.logger.info("building " + fullPath + " because of exception: " + ex1);
							rebuild = true;
						}
					}
			if (rebuild) {
				ByteArrayOutputStream out;
				try {
					out = new ByteArrayOutputStream();
					this.getSassCssProcessor().process(null, new InputStreamReader(this.read()), new OutputStreamWriter(out));
					// ByteArrayInputStream in = new ByteArrayInputStream(
					// out.toByteArray());
					// out = new ByteArrayOutputStream();
					// getCssCompressorProcessor().process(null,
					// new InputStreamReader(in),
					// new OutputStreamWriter(out));
				} catch (IOException ex) {
					throw new ResourceStreamNotFoundException(ex);
				}
				this.length = Bytes.bytes(out.size());
				this.lastModified = Time.now();
				this.css = new String(out.toByteArray(), this.charset);
			}
			return new ByteArrayInputStream(this.css.getBytes(this.charset));
		} catch (UnsupportedEncodingException ex) {
			throw new ResourceStreamNotFoundException(ex);
		}
	}

	@Override
	public PackageResource getResource() {
		return super.getResource();
	}

	protected String getResourcePath() {
		return this.getScope().getPackage().getName().replace('.', '/');
	}

	@Override
	public IResourceStream getResourceStream() {
		return this;
	}

	public ResourcePreProcessor getSassCssProcessor() {
		if (this.sassCssProcessor == null) {
			this.sassCssProcessor = new RubySassCssProcessor();
		}
		return this.sassCssProcessor;
	}

	protected String getSassName() {
		return this.getName().replaceAll("\\.css", ".scss");
	}

	@Override
	public Time lastModifiedTime() {
		if (this.lastModified == null) {
			try {
				this.getInputStream();
			} catch (ResourceStreamNotFoundException ex) {
				throw new RuntimeException(ex);
			}
		}
		SassResourceReference.logger.trace(this.getSassName() + " - sending lastModifiedTime: " + this.lastModified);
		return this.lastModified;
	}

	@Override
	public Bytes length() {
		return this.length;
	}

	protected InputStream read() throws IOException {
		return this.getClass().getClassLoader().getResourceAsStream(this.getResourcePath() + '/' + this.getSassName());
	}

	public void setCssCompressorProcessor(ResourcePreProcessor cssCompressorProcessor) {
		this.cssCompressorProcessor = cssCompressorProcessor;
	}

	@Override
	public void setLocale(Locale locale) {
		//
	}

	public void setSassCssProcessor(ResourcePreProcessor sassCssProcessor) {
		this.sassCssProcessor = sassCssProcessor;
	}

	@Override
	public void setStyle(String style) {
		//
	}

	@Override
	public void setVariation(String variation) {
		//
	}

	// protected void write(OutputStream out) throws IOException {
	// this.getSassCssProcessor().process(null, new InputStreamReader(this.read()), new OutputStreamWriter(out));
	// }
}
