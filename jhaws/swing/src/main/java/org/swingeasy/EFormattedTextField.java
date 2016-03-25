package org.swingeasy;

import java.text.Format;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.ToolTipManager;
import javax.swing.text.DefaultFormatterFactory;

import org.swingeasy.MethodInvoker.InvocationException;
import org.swingeasy.formatters.EFormatBuilder;
import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class EFormattedTextField<T> extends JFormattedTextField implements EComponentI {
    private static final long serialVersionUID = 3962285208926281649L;

    protected EFormatBuilder factory;

    protected final EFormattedTextFieldConfig cfg;

    // public EFormattedTextField(DefaultFormatter factory) {
    // this(new DefaultFormatterFactory(factory, factory, factory, factory));
    // }
    //
    // public EFormattedTextField(DefaultFormatter factory, T currentValue) {
    // this(new DefaultFormatterFactory(factory, factory, factory, factory));
    // this.setValue(currentValue);
    // }

    public EFormattedTextField(EFormattedTextFieldConfig cfg) {
        this.init(this.cfg = cfg.lock());
    }

    public EFormattedTextField(EFormattedTextFieldConfig cfg, T currentValue) {
        this.init(this.cfg = cfg.lock());
        this.setValue(currentValue);
    }

    public void addDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().addDocumentListener(listener);
        this.addKeyListener(listener);
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            String text = this.getText();
            if (text.trim().length() == 0) {
                text = null;
            }
            return text;
        }
        return toolTipText;
    }

    protected void init(EFormattedTextFieldConfig config) {
        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        if (config.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installTextComponentPopupMenu(this));
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        this.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

        if (config.getFactory() != null) {
            this.factory = config.getFactory();
            this.setFormat(this.factory.build(SystemSettings.getCurrentLocale()));
        } else {
            this.setFormatter(config.other);
        }
    }

    protected void installPopupMenuAction( EComponentPopupMenu menu) {
        //
    }

    public void removeDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().removeDocumentListener(listener);
        this.removeKeyListener(listener);
    }

    protected void setFormat(Format format) {
        AbstractFormatterFactory ff;
        try {
            // thank you for making this private
            ff = MethodInvoker.invoke(this, "getDefaultFormatterFactory", Object.class, format, AbstractFormatterFactory.class);

            if (ff instanceof DefaultFormatterFactory) {
                DefaultFormatterFactory defaultFormatterFactory = DefaultFormatterFactory.class.cast(ff);
                defaultFormatterFactory.setDisplayFormatter(defaultFormatterFactory.getDefaultFormatter());
                defaultFormatterFactory.setEditFormatter(defaultFormatterFactory.getDefaultFormatter());
                defaultFormatterFactory.setNullFormatter(defaultFormatterFactory.getDefaultFormatter());
            }
        } catch (InvocationException ex) {
            throw new RuntimeException(ex);
        }
        this.setFormatterFactory(ff);
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        if (this.factory != null) {
            this.setFormat(this.factory.build(l));
        }
    }
}
