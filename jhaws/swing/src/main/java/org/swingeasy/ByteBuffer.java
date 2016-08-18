package org.swingeasy;

import com.Ostermiller.util.CircularByteBuffer;

/**
 * @author Jurgen
 */
public class ByteBuffer extends com.Ostermiller.util.CircularByteBuffer implements Stream {
    private ByteBuffer() {
        super(CircularByteBuffer.INFINITE_SIZE);
    }
}
