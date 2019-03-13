package org.tools.hqlbuilder.webservice.wicket.less;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.webservice.wicket.StreamResourceReference;
import org.tools.hqlbuilder.webservice.wicket.sass.SassResourceReference;

import ro.isdc.wro.extensions.processor.css.NodeLessCssProcessor;
import ro.isdc.wro.extensions.processor.css.RhinoLessCssProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.JawrCssMinifierProcessor;

// install node.js
// ((npm install less))
// npm install -g less
/**
 * response.render(CssHeaderItem.forReference(new lessResourceReference(WicketCSSRoot.class, "table.less")));
 *
 * @see http://less-lang.com/
 * @see https://code.google.com/p/wro4j/
 * @see http://lesscss.org/features
 * @see https://github.com/duncansmart/less.js-windows
 */
@SuppressWarnings("serial")
public class LessResourceReference extends StreamResourceReference implements IResourceStream {
    protected static final Logger logger = LoggerFactory.getLogger(SassResourceReference.class);

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
    public void close() throws IOException {
        //
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    public ResourcePreProcessor getCssCompressorProcessor() {
        if (this.cssCompressorProcessor == null) {
            this.cssCompressorProcessor = new JawrCssMinifierProcessor();
        }
        return this.cssCompressorProcessor;
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            boolean rebuild = false;
            String fullPath = this.getResourcePath() + '/' + this.getLessName();
            if (this.css == null) {
                logger.info("building " + fullPath + " because is new");
                rebuild = true;
            } else if (this.lastModified == null) {
                logger.info("building " + fullPath + " because out of date");
                rebuild = true;
            } else if (WebApplication.get().usesDevelopmentConfig()) {
                try {
                    if (this.lastModified
                            .getMilliseconds() < this.getClass().getClassLoader().getResource(fullPath).openConnection().getLastModified()) {
                        logger.info("building " + fullPath + " because template out of date");
                        rebuild = true;
                    }
                } catch (IOException ex1) {
                    logger.info("building " + fullPath + " because of exception: " + ex1);
                    rebuild = true;
                }
            }
            if (rebuild) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    this.getLessCssProcessor().process(null, new InputStreamReader(this.read()), new OutputStreamWriter(out));
                    byte[] byteArray = out.toByteArray();
                    this.length = Bytes.bytes(byteArray.length);
                    this.css = new String(byteArray, this.charset);
                    // System.out.println(new String( this.css));
                } catch (IOException ex) {
                    throw new ResourceStreamNotFoundException(ex);
                }
                if (getCssCompressorProcessor() != null) {
                    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        getCssCompressorProcessor().process(null, new InputStreamReader(new ByteArrayInputStream(this.css.getBytes("utf-8"))),
                                new OutputStreamWriter(out));
                        byte[] byteArray = out.toByteArray();
                        this.length = Bytes.bytes(byteArray.length);
                        this.css = new String(byteArray, this.charset);
                        // System.out.println(new String( this.css));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                this.lastModified = Time.now();
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

    public ResourcePreProcessor getLessCssProcessor() {
        if (lessCssProcessor == null) {
            NodeLessCssProcessor nodeLessCssProcessor = new NodeLessCssProcessor();
            this.lessCssProcessor = nodeLessCssProcessor.isSupported() ? nodeLessCssProcessor : new RhinoLessCssProcessor();
        }
        return this.lessCssProcessor;
    }

    protected String getLessName() {
        return this.getName().replace(".css", ".less");
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
        logger.trace(this.getLessName() + " - sending lastModifiedTime: " + this.lastModified);
        return this.lastModified;
    }

    @Override
    public Bytes length() {
        return this.length;
    }

    protected InputStream read() throws IOException {
        return this.getClass().getClassLoader().getResourceAsStream(this.getResourcePath() + '/' + this.getLessName());
    }

    public void setCssCompressorProcessor(ResourcePreProcessor cssCompressorProcessor) {
        this.cssCompressorProcessor = cssCompressorProcessor;
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

    public static void main(String[] args) {
        try {
            // ro.isdc.wro.extensions.processor.css.LessCssProcessor
            // ro.isdc.wro.extensions.processor.css.RhinoLessCssProcessor
            // ro.isdc.wro.extensions.processor.css.NodeLessCssProcessor
            NodeLessCssProcessor less = new NodeLessCssProcessor();
            InputStream in = SassResourceReference.class.getClassLoader()
                    .getResourceAsStream("org/tools/hqlbuilder/webservice/bootstrap4/social/brand.less");
            ByteArrayOutputStream outtmp = new ByteArrayOutputStream();
            IOUtils.copy(in, outtmp);
            // System.out.println(new String(outtmp.toByteArray()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            less.process(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outtmp.toByteArray()))),
                    new BufferedWriter(new OutputStreamWriter(out)));
            out.close();
            System.out.println(new String(out.toByteArray()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}