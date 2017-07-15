package org.swingeasy.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Jurgen
 */
public interface Pipe {
    public InputStream getInputStream();

    public OutputStream getOutputStream();
}
