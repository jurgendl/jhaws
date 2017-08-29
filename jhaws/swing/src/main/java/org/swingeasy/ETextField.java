package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jurgen
 */
public class ETextField extends JTextField implements EComponentI, HasValue<String> {
    private static final long serialVersionUID = -7074333880526075392L;

    protected final List<ValueChangeListener<String>> valueChangeListeners = new ArrayList<>();

    protected final ETextFieldConfig cfg;

    protected ETextField() {
        cfg = null;
    }

    public ETextField(ETextFieldConfig cfg) {
        init(this.cfg = cfg.lock());
    }

    public void addDocumentKeyListener(DocumentKeyListener listener) {
        getDocument().addDocumentListener(listener);
        addKeyListener(listener);
    }

    public void setDocumentFilter(DocumentFilter filter) {
        getAbstractDocument().setDocumentFilter(filter);
    }

    /** normally an {@link ETextFieldValidator} */
    public DocumentFilter getDocumentFilter() {
        return getAbstractDocument().getDocumentFilter();
    }

    public AbstractDocument getAbstractDocument() {
        return AbstractDocument.class.cast(getDocument());
    }

    /**
     * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<String> listener) {
        valueChangeListeners.add(listener);
    }

    /**
     * @see org.swingeasy.HasValue#clearValueChangeListeners()
     */
    @Override
    public void clearValueChangeListeners() {
        valueChangeListeners.clear();
    }

    /**
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

    /**
     * @see org.swingeasy.ValidationDemo.HasValue#getValue()
     */
    @Override
    public String getValue() {
        String text = this.getText();
        return StringUtils.isBlank(text) ? null : text;
    }

    protected void init(ETextFieldConfig config) {
        if (cfg.getColumns() > 0) setColumns(cfg.getColumns());

        setEditable(config.isEnabled());

        if (config.isSelectAllOnFocus()) {
            addFocusListener(new ETextComponentSelectAllOnFocus());
        }

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        if (config.isDefaultPopupMenu()) {
            installPopupMenuAction(EComponentPopupMenu.installTextComponentPopupMenu(this));
        }

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                String value = ETextField.this.getValue();
                for (ValueChangeListener<String> valueChangeListener : valueChangeListeners) {
                    valueChangeListener.valueChanged(value);
                }
            }
        });

        if (config.getText() != null) {
            setText(config.getText());
        }

        if (config.getFilterInput() != null || config.getTextValidator() != null) {
            setDocumentFilter(new ETextFieldValidator().setFilterInput(config.getFilterInput()).setTextValidator(config.getTextValidator()));
        }
    }

    /**
     * JDOC
     */
    protected void installPopupMenuAction(EComponentPopupMenu menu) {
        //
    }

    /**
     * JDOC
     */
    public void removeDocumentKeyListener(DocumentKeyListener listener) {
        getDocument().removeDocumentListener(listener);
        removeKeyListener(listener);
    }

    /**
     * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<String> listener) {
        valueChangeListeners.remove(listener);
    }
}
