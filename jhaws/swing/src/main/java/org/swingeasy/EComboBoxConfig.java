package org.swingeasy;

/**
 * only autoResizePopup kan be changed after creation of the {@link EComboBox}
 *
 * @author Jurgen
 */
public class EComboBoxConfig extends EComponentConfig<EComboBoxConfig> {
	protected boolean autoComplete = true;

	protected boolean threadSafe = true;

	protected boolean scrolling = true;

	protected boolean sortable = true;

	protected boolean autoResizePopup = true;

	public EComboBoxConfig() {
		super();
	}

	public boolean isAutoComplete() {
		return autoComplete;
	}

	public boolean isAutoResizePopup() {
		return autoResizePopup;
	}

	public boolean isScrolling() {
		return scrolling;
	}

	public boolean isSortable() {
		return sortable;
	}

	public boolean isThreadSafe() {
		return threadSafe;
	}

	public EComboBoxConfig setAutoComplete(boolean autoComplete) {
		lockCheck();
		this.autoComplete = autoComplete;
		return this;
	}

	public EComboBoxConfig setAutoResizePopup(boolean autoResizePopup) {
		lockCheck();
		this.autoResizePopup = autoResizePopup;
		return this;
	}

	public EComboBoxConfig setScrolling(boolean scrolling) {
		lockCheck();
		this.scrolling = scrolling;
		return this;
	}

	public EComboBoxConfig setSortable(boolean sortable) {
		lockCheck();
		this.sortable = sortable;
		return this;
	}

	public EComboBoxConfig setThreadSafe(boolean threadSafe) {
		lockCheck();
		this.threadSafe = threadSafe;
		return this;
	}
}
