package org.tools.hqlbuilder.webservice.wicket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class CachingUrlResourceReference extends ResourceReference {
    private static final long serialVersionUID = 3936331898019241872L;

    private final URI url;

    private final String name;

    private byte[] buffer;

    public CachingUrlResourceReference(URI url, String name) {
        super(name);
        this.url = url;
        this.name = name;
    }

    @Override
    public IResource getResource() {
        ByteArrayResource byteArrayResource = new ByteArrayResource("application/javascript", null, name + ".js") {
            private static final long serialVersionUID = -4511180390748483560L;

            @Override
            protected byte[] getData(Attributes attributes) {
                if (buffer == null) {
                    try (InputStream input = url.toURL().openStream()) {
                        buffer = read(input);
                    } catch (IOException ex) {
                        buffer = new byte[0];
                        throw new UnsupportedOperationException();
                    }
                }
                return buffer;
            }
        };
        return byteArrayResource;
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        in.close();
        return out.toByteArray();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException, NullPointerException {
        try {
            byte[] buffer = new byte[8 * 1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
                //
            }
            try {
                out.close();
            } catch (Exception ex) {
                //
            }
        }
    }
}
