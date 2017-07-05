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

    protected int columns = 0;

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

    public int getColumns() {
        return this.columns;
    }

    public EFormattedTextFieldConfig setColumns(int columns) {
        lockCheck();
        this.columns = columns;
        return this;
    }
}
