package org.swingeasy;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class EListSearchComponent<T> extends ELabeledTextFieldButtonComponent implements Matcher<T> {

	private static final long serialVersionUID = -8699648472825404199L;

	protected final EList<T> eList;

	protected final EListI<T> sList;

	protected Pattern pattern = null;

	public EListSearchComponent(EList<T> eList) {
		this.eList = eList;
		this.sList = eList.stsi();
	}

	/**
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#doAction()
	 */
	@Override
	protected void doAction() {
		String text = JTextField.class.cast(this.getInput()).getText();
		if (text.length() == 0) {
			return;
		}
		this.pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
		EListRecord<T> nextMatch = this.nextMatchTryTop();
		if (nextMatch != null) {
			this.onMatch(nextMatch);
		} else {
			this.onNoMatch();
		}
	}

	/**
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#getAction()
	 */
	@Override
	protected String getAction() {
		return "search";
	}

	/**
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
	 */
	@Override
	protected Icon getIcon() {
		return Resources.getImageResource("find.png");
	}

	/**
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return this;
	}

	/**
	 * @see ca.odell.glazedlists.matchers.Matcher#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(T item) {
		return this.pattern.matcher(String.valueOf(item)).find();
	}

	protected EListRecord<T> nextMatchTryTop() {
		List<EListRecord<T>> records = this.eList.getRecords();

		if (records.isEmpty()) {
			return null;
		}

		int index = this.eList.getSelectedIndex();

		if (index == -1) {
			index = 0;
		}

		index++;

		while (index < records.size()) {
			EListRecord<T> record = records.get(index);
			if (this.pattern.matcher(record.getStringValue()).find()) {
				return record;
			}
			index++;
		}

		return null;
	}

	protected void onMatch(EListRecord<T> nextMatch) {
		this.eList.scrollToVisibleRecord(nextMatch);
		this.sList.setSelectedRecord(nextMatch);
	}

	protected void onNoMatch() {
		String message = Messages.getString(this.getLocale(), "EList.SearchComponent.nomatch");//$NON-NLS-1$
		String title = Messages.getString(this.getLocale(), "EList.SearchComponent.searchmatch");//$NON-NLS-1$
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.getButton().setToolTipText(Messages.getString(l, "EList.SearchComponent.search"));//$NON-NLS-1$
		this.getLabel().setText(Messages.getString(l, "EList.SearchComponent.search") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
	}

}
