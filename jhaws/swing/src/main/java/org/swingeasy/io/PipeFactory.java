package org.swingeasy.io;

/**
 * @author Jurgen
 */
public class PipeFactory {
    protected static boolean tryOnce = true;

    public static Pipe create() {
        if (PipeFactory.tryOnce) {
            try {
                return (Pipe) Class.forName("org.swingeasy.CircularByteBuffer").newInstance();
            } catch (Exception ex) {
                //
            } finally {
                PipeFactory.tryOnce = false;
            }
        }
        return new InMemoryPipe();
    }
}
