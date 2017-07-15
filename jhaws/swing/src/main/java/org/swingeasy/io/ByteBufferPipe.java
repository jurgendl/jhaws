package org.swingeasy.io;

import com.Ostermiller.util.CircularByteBuffer;

/**
 * @author Jurgen
 */
public class ByteBufferPipe extends com.Ostermiller.util.CircularByteBuffer implements Pipe {
    private ByteBufferPipe() {
        super(CircularByteBuffer.INFINITE_SIZE);
    }
}
