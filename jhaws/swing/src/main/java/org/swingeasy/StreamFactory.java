package org.swingeasy;

/**
 * @author Jurgen
 */
public class StreamFactory {
    protected static boolean tryOnce = true;

    public static Stream create() {
        if (StreamFactory.tryOnce) {
            try {
                return (Stream) Class.forName("org.swingeasy.CircularByteBuffer").newInstance();
            } catch (Exception ex) {
                //
            } finally {
                StreamFactory.tryOnce = false;
            }
        }
        return new InMemoryStream();
    }
}
