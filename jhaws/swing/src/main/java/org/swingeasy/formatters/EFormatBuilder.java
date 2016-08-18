package org.swingeasy.formatters;

import java.text.Format;
import java.util.Locale;

/**
 * @author Jurgen
 */
public interface EFormatBuilder {
    public Format build(Locale locale);
}
