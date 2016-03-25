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
        this.other = factory;
    }

    public EFormattedTextFieldConfig(EFormatBuilder factory) {
        this.factory = factory;
    }

    public EFormatBuilder getFactory() {
        if (this.factory == null) {
            this.factory = new NumberFormatBuilder(NumberFormatBuilder.Type.Default);
        }
        return this.factory;
    }

    public EFormattedTextFieldConfig setFactory(EFormatBuilder factory) {
        this.lockCheck();
        this.factory = factory;
        return this;
    }
}
