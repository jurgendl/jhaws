package org.swingeasy;

import javax.swing.text.DefaultFormatter;

import org.swingeasy.formatters.EFormatBuilder;
import org.swingeasy.formatters.NumberFormatBuilder;

/**
 * @author Jurgen
 */
public class EFormattedTextFieldConfig extends EComponentConfig<EFormattedTextFieldConfig> {
    protected EFormatBuilder factory;

    protected DefaultFormatter other;

    public EFormattedTextFieldConfig(DefaultFormatter factory) {
        other = factory;
    }

    public EFormattedTextFieldConfig(EFormatBuilder factory) {
        this.factory = factory;
    }

    public EFormatBuilder getFactory() {
        if (factory == null) {
            factory = new NumberFormatBuilder(NumberFormatBuilder.Type.Default);
        }
        return factory;
    }

    public EFormattedTextFieldConfig setFactory(EFormatBuilder factory) {
        lockCheck();
        this.factory = factory;
        return this;
    }
}
