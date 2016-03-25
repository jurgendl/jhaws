package org.swingeasy;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class ETreeSearchComponent<T> extends ELabeledTextFieldButtonComponent implements Matcher<T> {
	private static final long serialVersionUID = 5196244125968828897L;

	protected final ETree<T> eTree;

	protected Pattern pattern = null;

	public ETreeSearchComponent(ETree<T> eTree) {
		this.eTree = eTree;
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
		TreePath nextMatch = this.nextMatchTryTop();
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
		return "search";//$NON-NLS-1$
	}

	/**
	 * @see org.swingeasy.ELabeledTextFieldButtonComponent#getIcon()
	 */
	@Override
	protected Icon getIcon() {
		return Resources.getImageResource("find.png");//$NON-NLS-1$
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

	protected TreePath nextMatchTryTop() {
		try {
			TreePath current = this.eTree.stsi().getSelectedOrTopNodePath();
			return this.eTree.stsi().getNextMatch(current, this);
		} catch (IllegalArgumentException ex1) {
			try {
				TreePath current = this.eTree.stsi().getTopNodePath();
				return this.eTree.stsi().getNextMatch(current, this);
			} catch (IllegalArgumentException ex2) {
				return null;
			}
		}
	}

	protected void onMatch(TreePath nextMatch) {
		this.eTree.scrollPathToVisible(nextMatch);
		this.eTree.stsi().setSelectionPath(nextMatch);
	}

	protected void onNoMatch() {
		String message = Messages.getString(this.getLocale(), "ETree.SearchComponent.nomatch");//$NON-NLS-1$
		String title = Messages.getString(this.getLocale(), "ETree.SearchComponent.searchmatch");//$NON-NLS-1$
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @see java.awt.Component#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.getButton().setToolTipText(Messages.getString(l, "ETree.SearchComponent.search"));//$NON-NLS-1$
		this.getLabel().setText(Messages.getString(l, "ETree.SearchComponent.search") + ": "); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
