package org.tools.hqlbuilder.webservice.wicket.less;

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

import ro.isdc.wro.extensions.processor.css.NodeLessCssProcessor;
import ro.isdc.wro.extensions.processor.css.RhinoLessCssProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.CssCompressorProcessor;

/**
 * response.render(CssHeaderItem.forReference(new lessResourceReference(WicketCSSRoot.class, "table.less")));
 *
 * @see http://less-lang.com/
 * @see https://code.google.com/p/wro4j/
 * @see http://lesscss.org/features
 * @see https://github.com/duncansmart/less.js-windows
 */
public class LessResourceReference extends StreamResourceReference implements IResourceStream {
    private static final long serialVersionUID = 4347055808647080465L;

    protected static final Logger logger = LoggerFactory.getLogger(LessResourceReference.class);

    protected final String charset = "utf-8";

    protected final String contentType = "text/css";

    protected transient Bytes length = null;

    protected transient String css = "";

    protected transient Time lastModified = null;

    protected transient ResourcePreProcessor lessCssProcessor;

    protected transient ResourcePreProcessor cssCompressorProcessor;

    public LessResourceReference(Class<?> scope, String name) {
        super(scope, name);
    }

    @Override
    public PackageResource getResource() {
        return super.getResource();
    }

    @Override
    public IResourceStream getResourceStream() {
        return this;
    }

    @Override
    public Time lastModifiedTime() {
        if (lastModified == null) {
            try {
                getInputStream();
            } catch (ResourceStreamNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        logger.info(getLessName() + " - sending lastModifiedTime: " + lastModified);
        return lastModified;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public Bytes length() {
        return length;
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            boolean rebuild = false;
            String fullPath = getResourcePath() + '/' + getLessName();
            if (css == null) {
                logger.info("building " + fullPath + " because is new");
                rebuild = true;
            } else if (lastModified == null) {
                logger.info("building " + fullPath + " because out of date");
                rebuild = true;
            } else if (WicketApplication.get().usesDevelopmentConfig()) {
                try {
                    if (lastModified.getMilliseconds() < getClass().getClassLoader().getResource(fullPath).openConnection().getLastModified()) {
                        logger.info("building " + fullPath + " because template out of date");
                        rebuild = true;
                    }
                } catch (IOException ex1) {
                    logger.info("building " + fullPath + " because of exception: " + ex1);
                    rebuild = true;
                }
            }
            if (rebuild) {
                ByteArrayOutputStream out;
                try {
                    out = new ByteArrayOutputStream();
                    getLessCssProcessor().process(null, new InputStreamReader(read()), new OutputStreamWriter(out));
                    // ByteArrayInputStream in = new ByteArrayInputStream(
                    // out.toByteArray());
                    // out = new ByteArrayOutputStream();
                    // getCssCompressorProcessor().process(null,
                    // new InputStreamReader(in),
                    // new OutputStreamWriter(out));
                } catch (IOException ex) {
                    throw new ResourceStreamNotFoundException(ex);
                }
                length = Bytes.bytes(out.size());
                lastModified = Time.now();
                css = new String(out.toByteArray(), charset);
            }
            return new ByteArrayInputStream(css.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            throw new ResourceStreamNotFoundException(ex);
        }
    }

    @Override
    public void close() throws IOException {
        //
    }

    @Override
    public void setLocale(Locale locale) {
        //
    }

    @Override
    public void setStyle(String style) {
        //
    }

    @Override
    public void setVariation(String variation) {
        //
    }

    protected String getLessName() {
        return getName().replaceAll("\\.css", ".less");
    }

    protected String getResourcePath() {
        return getScope().getPackage().getName().replace('.', '/');
    }

    protected InputStream read() throws IOException {
        return getClass().getClassLoader().getResourceAsStream(getResourcePath() + '/' + getLessName());
    }

    public ResourcePreProcessor getLessCssProcessor() {
        if (lessCssProcessor == null) {
            NodeLessCssProcessor nodeLessCssProcessor = new NodeLessCssProcessor();
            this.lessCssProcessor = nodeLessCssProcessor.isSupported() ? nodeLessCssProcessor : new RhinoLessCssProcessor();
        }
        return this.lessCssProcessor;
    }

    public void setLessCssProcessor(ResourcePreProcessor lessCssProcessor) {
        this.lessCssProcessor = lessCssProcessor;
    }

    public ResourcePreProcessor getCssCompressorProcessor() {
        if (cssCompressorProcessor == null) {
            cssCompressorProcessor = new CssCompressorProcessor();
        }
        return cssCompressorProcessor;
    }

    public void setCssCompressorProcessor(ResourcePreProcessor cssCompressorProcessor) {
        this.cssCompressorProcessor = cssCompressorProcessor;
    }

}
