package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * @author Jurgen
 */
public class EDateEditor extends AbstractELabeledTextFieldButtonComponent<ELabel, ESpinner<Date>, EButton> {
	private static final long serialVersionUID = 3275532427920825736L;

	protected JComponent parentComponent = null;

	protected EDateTimeChooser dateChooser = null;

	protected JPopupMenu popup = null;

	protected JPanel datePanel = null;

	protected SimpleDateFormat formatter;

	public EDateEditor() {
		this(new Date());
	}

	public EDateEditor(Date date) {
		init();
		setDate(date);
	}

	/**
	 * 
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void copy(ActionEvent e) {
		EComponentPopupMenu.copyToClipboard(String.valueOf(getInput().getValue()));
	}

	protected EDateTimeChooser createDateChooser() {
		return new EDateTimeChooser(DateTimeType.DATE);
	}

	/**
	 * 
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
	 */
	@Override
	protected void doAction() {
		JPopupMenu pop = getPopup();
		pop.setLocation(new Point(getLocationOnScreen().x + getWidth() - (int) pop.getPreferredSize().getWidth(), getLocationOnScreen().y + getHeight()));
		Date date = getInput().get();
		if (date == null) {
			date = new Date();
		}
		getDateChooser().setDate(date);
		pop.setVisible(true);
	}

	/**
	 * 
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
	 */
	@Override
	protected String getAction() {
		return "pick-date";//$NON-NLS-1$
	}

	/**
	 * 
	 * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getButton()
	 */
	@Override
	public EButton getButton() {
		if (button == null) {
			button = new EButton(new EButtonConfig(new EIconButtonCustomizer(new Dimension(20, 20)), getIcon()));
			button.setActionCommand(getAction());
			button.addActionListener(e -> EDateEditor.this.doAction());
		}
		return button;
	}

	public Date getDate() {
		return getInput().get();
	}

	protected EDateTimeChooser getDateChooser() {
		if (dateChooser == null) {
			dateChooser = createDateChooser();
		}
		return dateChooser;
	}

	protected JPanel getDatePanel() {
		if (datePanel == null) {
			datePanel = new JPanel(new BorderLayout());
			JPanel actions = new JPanel(new FlowLayout());
			{
				EButton okbtn = new EButton(new EButtonConfig(Messages.getString(getLocale(), "EDateEditor.OK")));//$NON-NLS-1$
				okbtn.addActionListener(e -> {
					EDateEditor.this.setDate(EDateEditor.this.getDateChooser().getDate());
					EDateEditor.this.getPopup().setVisible(false);
				});
				actions.add(okbtn);
			}
			{
				EButton nullbtn = new EButton(new EButtonConfig(Messages.getString(getLocale(), "EDateEditor.Null")));//$NON-NLS-1$
				nullbtn.addActionListener(e -> {
					EDateEditor.this.setDate(null);
					EDateEditor.this.getPopup().setVisible(false);
				});
				actions.add(nullbtn);
			}
			{
				EButton cancelbtn = new EButton(new EButtonConfig(Messages.getString(getLocale(), "EDateEditor.Cancel")));//$NON-NLS-1$
				cancelbtn.addActionListener(e -> EDateEditor.this.getPopup().setVisible(false));
				actions.add(cancelbtn);
			}
			datePanel.add(getDateChooser(), BorderLayout.CENTER);
			datePanel.add(actions, BorderLayout.SOUTH);
		}
		return datePanel;
	}

	public SimpleDateFormat getFormatter() {
		if (formatter == null) {
			formatter = SimpleDateFormat.class.cast(DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()));
		}
		return formatter;
	}

	/**
	 * 
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
	 */
	@Override
	protected Icon getIcon() {
		return Resources.getImageResource("date.png");//$NON-NLS-1$
	}

	/**
	 * 
	 * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getInput()
	 */
	@Override
	public ESpinner<Date> getInput() {
		if (input == null) {
			ESpinnerDateModel model = new ESpinnerDateModel();
			model.setValue(new Date());
			input = new ESpinner<>(model);
			input.setEditor(new ESpinner.DateEditor(input, getFormatter().toPattern()));
			input.setBorder(null);
		}
		return input;
	}

	/**
	 * 
	 * @see org.swingeasy.AbstractELabeledTextFieldButtonComponent#getLabel()
	 */
	@Override
	public ELabel getLabel() {
		if (label == null) {
			label = new ELabel();
			label.setLabelFor(getInput());
		}
		return label;
	}

	/**
	 * 
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return parentComponent;
	}

	protected JPopupMenu getPopup() {
		if (popup == null) {
			popup = new JPopupMenu();
			popup.setLightWeightPopupEnabled(true);
			popup.add(getDatePanel());
			UIUtils.getRootWindow(getButton()).addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					popup.setVisible(false);
				}

				@Override
				public void windowIconified(WindowEvent e) {
					popup.setVisible(false);
				}
			});
		}
		return popup;
	}

	protected void hidePopup() {
		if (popup != null) {
			popup.setVisible(false);
		}
	}

	protected void init() {
		installAncestorListener();
	}

	protected void installAncestorListener() {
		addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				EDateEditor.this.hidePopup();
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				if (event.getSource() != EDateEditor.this) {
					EDateEditor.this.hidePopup();
				}
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				EDateEditor.this.hidePopup();
			}
		});
	}

	public void setDate(Date date) {
		getInput().setValue(date);
	}

	/**
	 * 
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		getButton().setToolTipText(Messages.getString(l, "EDateEditor.button.text"));//$NON-NLS-1$
		getLabel().setText(Messages.getString(l, "EDateEditor.label.text") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
		// TODO set local format spinner
	}

	public void setParentComponent(JComponent parentComponent) {
		this.parentComponent = parentComponent;
	}
}
