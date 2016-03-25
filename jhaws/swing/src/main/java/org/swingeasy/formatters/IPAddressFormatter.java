package org.swingeasy.formatters;

import java.text.ParseException;
import java.util.StringTokenizer;

import javax.swing.text.DefaultFormatter;

/**
 * A formatter for 4-byte IP addresses of the form a.b.c.d
 * 
 * @see http://www.java2s.com/Code/Java/Swing-JFC/Acollectionofformattedtextfieldsandabuttonthatdisplaysthefieldvalues.htm
 */
public class IPAddressFormatter extends DefaultFormatter {
    private static final long serialVersionUID = -5848169520434688494L;

    protected final int length;

    public IPAddressFormatter() {
        this(4);
    }

    public IPAddressFormatter( boolean expanded) {
        this(6);
    }

    protected IPAddressFormatter(int l) {
        this.length = l;
    }

    /**
     * 
     * @see javax.swing.text.DefaultFormatter#stringToValue(java.lang.String)
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(text, ".");
        byte[] a = new byte[this.length];
        for (int i = 0; i < this.length; i++) {
            int b = 0;
            if (!tokenizer.hasMoreTokens()) {
                throw new ParseException("Too few bytes", 0);
            }
            try {
                b = Integer.parseInt(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                throw new ParseException("Not an integer", 0);
            }
            if ((b < 0) || (b >= 256)) {
                throw new ParseException("Byte out of range", 0);
            }
            a[i] = (byte) b;
        }
        if (tokenizer.hasMoreTokens()) {
            throw new ParseException("Too many bytes", 0);
        }
        return a;
    }

    /**
     * 
     * @see javax.swing.text.DefaultFormatter#valueToString(java.lang.Object)
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        if (!(value instanceof byte[])) {
            throw new ParseException("Not a byte[]", 0);
        }
        byte[] a = (byte[]) value;
        if (a.length != this.length) {
            throw new ParseException("Length != " + this.length, 0);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.length; i++) {
            int b = a[i];
            if (b < 0) {
                b += 256;
            }
            builder.append(String.valueOf(b));
            if ((i + 1) < this.length) {
                builder.append('.');
            }
        }
        return builder.toString();
    }
}
