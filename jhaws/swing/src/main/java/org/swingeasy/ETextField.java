package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jurgen
 */
public class ETextField extends JTextField implements EComponentI, HasValue<String> {
	private static final long serialVersionUID = -7074333880526075392L;

	protected final List<ValueChangeListener<String>> valueChangeListeners = new ArrayList<ValueChangeListener<String>>();

	protected final ETextFieldConfig cfg;

	protected ETextField() {
		this.cfg = null;
	}

	public ETextField(ETextFieldConfig cfg) {
		this.init(this.cfg = cfg.lock());
	}

	public void addDocumentKeyListener(DocumentKeyListener listener) {
		this.getDocument().addDocumentListener(listener);
		this.addKeyListener(listener);
	}

	/**
	 * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(ValueChangeListener<String> listener) {
		this.valueChangeListeners.add(listener);
	}

	/**
	 * @see org.swingeasy.HasValue#clearValueChangeListeners()
	 */
	@Override
	public void clearValueChangeListeners() {
		this.valueChangeListeners.clear();
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
		this.setColumns(this.cfg.getColumns());

		this.setEditable(config.isEnabled());

		if (config.isSelectAllOnFocus()) {
			this.addFocusListener(new ETextComponentSelectAllOnFocus());
		}

		if (config.isTooltips()) {
			ToolTipManager.sharedInstance().registerComponent(this);
		}

		if (config.isDefaultPopupMenu()) {
			this.installPopupMenuAction(EComponentPopupMenu.installTextComponentPopupMenu(this));
		}

		if (config.isLocalized()) {
			UIUtils.registerLocaleChangeListener((EComponentI) this);
		}

		this.addDocumentKeyListener(new DocumentKeyListener() {
			@Override
			public void update(Type type, DocumentEvent e) {
				String value = ETextField.this.getValue();
				for (ValueChangeListener<String> valueChangeListener : ETextField.this.valueChangeListeners) {
					valueChangeListener.valueChanged(value);
				}
			}
		});

		if (config.getText() != null) {
			this.setText(config.getText());
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
		this.getDocument().removeDocumentListener(listener);
		this.removeKeyListener(listener);
	}

	/**
	 * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(ValueChangeListener<String> listener) {
		this.valueChangeListeners.remove(listener);
	}
}
