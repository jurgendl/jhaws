package org.swingeasy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public interface Stream {
    public InputStream getInputStream();

    public OutputStream getOutputStream();
}
