package org.jhaws.common.web.wicket.sass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jhaws.common.web.wicket.StreamResourceReference;

import ro.isdc.wro.extensions.processor.css.RubySassCssProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.impl.css.JawrCssMinifierProcessor;

/**
 * response.render(CssHeaderItem.forReference(new SassResourceReference(WicketCSSRoot.class, "table.sass")));
 *
 * @see http://sass-lang.com/
 * @see https://code.google.com/p/wro4j/
 */
@SuppressWarnings("serial")
public class SassResourceReference extends StreamResourceReference implements IResourceStream {
    private static final String UTF_8 = "utf-8";

    protected static final Logger logger = LoggerFactory.getLogger(SassResourceReference.class);

    protected String charset = UTF_8;

    protected String contentType = "text/css";

    protected transient Bytes length = null;

    protected transient String css = "";

    protected transient Time lastModified = null;

    protected transient RubySassCssProcessor sassCssProcessor;

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
            this.cssCompressorProcessor = new JawrCssMinifierProcessor();
        }
        return this.cssCompressorProcessor;
    }

    @Override
    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            boolean rebuild = false;
            String fullPath = this.getResourcePath() + '/' + this.getSassName();
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
                rebuild();
            }
            return new ByteArrayInputStream(this.css.getBytes(this.charset));
        } catch (URISyntaxException | IOException ex) {
            throw new ResourceStreamNotFoundException(ex);
        }
    }

    protected synchronized void rebuild() throws IOException, URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource(this.getResourcePath() + '/' + this.getSassName());
        String scss = new String(IOUtils.toByteArray(resource), UTF_8);

        String name = getName();
        if (name.contains("/")) name = name.substring(name.lastIndexOf("/") + 1);
        if (name.contains("\\")) name = name.substring(name.lastIndexOf("\\") + 1);

        // combineert imports 1 niveau diep
        scss = replace(Paths.get(resource.toURI()).getParent(), scss);

        // development
        if (WebApplication.get().usesDevelopmentConfig()) {
            File tmp = new File(System.getProperty("java.io.tmpdir"), name + ".combined." + System.currentTimeMillis() + ".css");
            IOUtils.write(scss.getBytes(UTF_8), new FileOutputStream(tmp));
            logger.info(tmp.getAbsolutePath());
        }

        // scss > css
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            this.getSassCssProcessor().process(null, new StringReader(scss), new OutputStreamWriter(out));
            byte[] byteArray = out.toByteArray();
            this.length = Bytes.bytes(byteArray.length);
            this.css = new String(byteArray, this.charset);

            // development
            if (WebApplication.get().usesDevelopmentConfig()) {
                File tmp = new File(System.getProperty("java.io.tmpdir"), name + ".uncompressed." + System.currentTimeMillis() + ".css");
                IOUtils.write(byteArray, new FileOutputStream(tmp));
                logger.info(tmp.getAbsolutePath());
            }
        }

        // optionele post processor (bv die css verkleint)
        if (getCssCompressorProcessor() != null) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                getCssCompressorProcessor().process(null, new InputStreamReader(new ByteArrayInputStream(this.css.getBytes(charset))),
                        new OutputStreamWriter(out));
                byte[] byteArray = out.toByteArray();
                this.length = Bytes.bytes(byteArray.length);
                this.css = new String(byteArray, this.charset).trim();

                // development
                if (WebApplication.get().usesDevelopmentConfig()) {
                    File tmp = new File(System.getProperty("java.io.tmpdir"), name + ".compressed." + System.currentTimeMillis() + ".css");
                    IOUtils.write(byteArray, new FileOutputStream(tmp));
                    logger.info(tmp.getAbsolutePath());
                }
            }
        }

        // zet tijdstip
        this.lastModified = Time.now();
    }

    // @Override
    // public PackageResource getResource() {
    // return super.getResource();
    // }

    protected String getResourcePath() {
        return this.getScope().getPackage().getName().replace('.', '/');
    }

    @Override
    public IResourceStream getResourceStream() {
        return this;
    }

    public RubySassCssProcessor getSassCssProcessor() {
        if (this.sassCssProcessor == null) {
            this.sassCssProcessor = new RubySassCssProcessor();
        }
        return this.sassCssProcessor;
    }

    protected String getSassName() {
        return this.getName().replace(".css", ".scss");
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
        logger.trace(this.getSassName() + " - sending lastModifiedTime: " + this.lastModified);
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

    @Override
    public void setStyle(String style) {
        //
    }

    @Override
    public void setVariation(String variation) {
        //
    }

    // protected void write(OutputStream out) throws IOException {
    // this.getSassCssProcessor().process(new InputStreamReader(this.read()), new OutputStreamWriter(out));
    // out.flush();
    // }

    // public static void main(String[] args) {
    // try {
    // RubySassCssProcessor scss = new RubySassCssProcessor();
    // InputStream in = IOUtils.openFileOrResource("file.scss");
    // ByteArrayOutputStream outtmp = new ByteArrayOutputStream();
    // IOUtils.copy(in, outtmp);
    // // logger.info(new String(outtmp.toByteArray()));
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // scss.process(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outtmp.toByteArray()))),
    // new BufferedWriter(new OutputStreamWriter(out)));
    // out.close();
    // // logger.info("=============");
    // logger.info(new String(out.toByteArray()));
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // }
    // }

    protected static final Pattern PATTERN_IMPORT = Pattern.compile("^@import \"([^\"]++)\";", Pattern.MULTILINE);

    protected static String replace(Path base, String css) throws IOException {
        Matcher m = PATTERN_IMPORT.matcher(css);
        if (m.find()) {
            String relative = m.group(1);
            Path imported = base.resolve(relative + ".scss");
            if (imported.toFile().exists()) {
                //
            } else {
                imported = base.resolve(relative + ".css");
                if (imported.toFile().exists()) {
                    //
                } else {
                    throw new FileNotFoundException(String.valueOf(imported));
                }
            }
            String importing = new String(Files.readAllBytes(imported), UTF_8);
            Matcher mi = PATTERN_IMPORT.matcher(importing);
            if (mi.find()) {
                throw new IOException("no 2 level imports: " + imported + " >> " + mi.group(1));
            }
            css = css.substring(0, m.start()) + importing + css.substring(m.end());
            replace(base, css);
        }
        return css;
    }
}
