package org.swingeasy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Utilities;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;
import org.swingeasy.EComponentPopupMenu.ReadableTextComponent;

/**
 * TODO http://andreinc.net/2013/07/15/how-to-customize-the-font-inside-a-jtextpane-component-java-swing-highlight-java-keywords-inside-a-jtextpane/#more -1129
 *
 * @author Jurgen
 */
public class ETextArea extends JTextArea implements EComponentI, HasValue<String>, ETextComponentI, ReadableTextComponent {
	protected static class OpenAction extends EComponentPopupMenuAction<ETextArea> {
		private static final long serialVersionUID = 649772388750665266L;

		public OpenAction(ETextArea component) {
			super(component, "open", Resources.getImageResource("folder_page_white.png"));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		}

		/**
		 *
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			File file = CustomizableOptionPane.showFileChooserDialog(this.getParentComponent(), FileChooserType.OPEN, new FileChooserCustomizer() {
				@Override
				public void customize(Component parentComponent, JDialog dialog) {
					// dialog.setLocationRelativeTo(null);
				}

				@Override
				public void customize(JFileChooser jfc) {
					jfc.resetChoosableFileFilters();
					jfc.addChoosableFileFilter(
							new ExtensionFileFilter(UIUtils.getDescriptionForFileType(OpenAction.this.delegate.getFileExt()) + " (" + OpenAction.this.delegate.getFileExt() + ")",
									OpenAction.this.delegate.getFileExt()));
				}
			});
			if (file == null) {
				return;
			}
			try (FileInputStream in = new FileInputStream(file)) {
				// Document doc = this.delegate.getDocument();
				byte[] buffer = new byte[in.available()];
				in.read(buffer);
				in.close();
				this.delegate.setText(new String(buffer));
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 *
		 * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
		 */
		@Override
		public boolean checkEnabled(CheckEnabled cfg) {
			boolean e = this.delegate.isEditable() && this.delegate.isEnabled();
			this.setEnabled(e);
			return e;
		}
	}

	protected static class PrintAction extends EComponentPopupMenuAction<ETextArea> {
		private static final long serialVersionUID = 649772388750665266L;

		public PrintAction(ETextArea component) {
			super(component, "print", Resources.getImageResource("printer.png"));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		}

		/**
		 *
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			PrinterJob printJob = PrinterJob.getPrinterJob();
			Printable printable = this.delegate.getPrintable(null, null);
			printJob.setPrintable(printable);
			if (printJob.printDialog()) {
				try {
					printJob.print();
				} catch (PrinterException pe) {
					System.out.println("Error printing: " + pe);
				}
			}
		}

		/**
		 *
		 * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
		 */
		@Override
		public boolean checkEnabled(CheckEnabled cfg) {
			this.setEnabled(cfg.hasText);
			return cfg.hasText;
		}
	}

	protected static class SaveAction extends EComponentPopupMenuAction<ETextArea> {
		private static final long serialVersionUID = 344984528281010531L;

		public SaveAction(ETextArea component) {
			super(component, "save", Resources.getImageResource("bullet_disk.png"));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		}

		/**
		 *
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			final String fileExt = SaveAction.this.delegate.getFileExt();
			File file = CustomizableOptionPane.showFileChooserDialog(this.getParentComponent(), FileChooserType.SAVE, new FileChooserCustomizer() {
				@Override
				public void customize(Component parentComponent, JDialog dialog) {
					// dialog.setLocationRelativeTo(null);
				}

				@Override
				public void customize(JFileChooser jfc) {
					jfc.resetChoosableFileFilters();
					jfc.addChoosableFileFilter(new ExtensionFileFilter(UIUtils.getDescriptionForFileType(fileExt) + " (" + fileExt + ")", fileExt));
				}
			});
			if (file == null) {
				return;
			}
			if (!file.getName().endsWith(fileExt)) {
				file = new File(file.getParentFile(), file.getName() + "." + fileExt);
			}
			if (file.exists()) {
				if (ResultType.YES != CustomizableOptionPane.showCustomDialog(this.getParentComponent(),
						new JLabel(Messages.getString((Locale) null, "SaveAction.overwrite.warning.message")),
						Messages.getString((Locale) null, "SaveAction.overwrite.warning.title"), MessageType.WARNING, OptionType.YES_NO, null, null)) {
					return;
				}
			}
			try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
				Document doc = this.delegate.getDocument();
				out.write(doc.getText(0, doc.getLength()).getBytes());
				out.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			} catch (BadLocationException ex) {
				throw new RuntimeException(ex);
			}
		}

		/**
		 *
		 * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
		 */
		@Override
		public boolean checkEnabled(CheckEnabled cfg) {
			this.setEnabled(cfg.hasText);
			return cfg.hasText;
		}
	}

	protected class SearchHighlightPainter extends ETextAreaFillHighlightPainter {
		public SearchHighlightPainter() {
			super(new Color(245, 225, 145));
		}
	}

	private static final long serialVersionUID = -854993140855661563L;

	protected final List<ValueChangeListener<String>> valueChangeListeners = new ArrayList<ValueChangeListener<String>>();

	protected ETextAreaHighlightPainter highlightPainter;

	protected String lastSearch = null;

	protected final ETextAreaConfig cfg;

	protected Action[] actions;

	protected ETextArea() {
		this.cfg = null;
	}

	public ETextArea(ETextAreaConfig cfg) {
		super(cfg.lock().getRows(), cfg.getColumns());
		this.init(this.cfg = cfg);
	}

	public ETextArea(ETextAreaConfig cfg, String text) {
		super(text, cfg.lock().getRows(), cfg.getColumns());
		this.init(this.cfg = cfg);
	}

	public void addDocumentKeyListener(DocumentKeyListener listener) {
		this.getDocument().addDocumentListener(listener);
		this.addKeyListener(listener);
	}

	public Highlighter.Highlight addHighlight(int from, int to, ETextAreaHighlightPainter painter) throws BadLocationException {
		return (Highlighter.Highlight) this.getHighlighter().addHighlight(from, to, painter);
	}

	/**
	 *
	 * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(ValueChangeListener<String> listener) {
		this.valueChangeListeners.add(listener);
	}

	/**
	 *
	 * @see org.swingeasy.HasValue#clearValueChangeListeners()
	 */
	@Override
	public void clearValueChangeListeners() {
		this.valueChangeListeners.clear();
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void copy(ActionEvent e) {
		this.copy();
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableTextComponent#find(java.awt.event.ActionEvent)
	 */
	@Override
	public void find(ActionEvent e) {
		SearchDialog searchDialog = new SearchDialog(false, this);
		searchDialog.setVisible(true);
		searchDialog.updateFocus();
	}

	public void find(String find) {
		int start = this.getSelectionStart();
		Pattern pattern = Pattern.compile(find, Pattern.CASE_INSENSITIVE);
		String text = this.getText();
		Matcher matcher = pattern.matcher(text);
		if (matcher.find(start)) {
			// search from caret
			int selectionStart = matcher.start();
			if (selectionStart == start) {
				// search from caret +1
				if (matcher.find(start + 1)) {
					selectionStart = matcher.start();
				} else {
					selectionStart = -1;
				}
			}
			if (selectionStart != -1) {
				// result found
				int selectionEnd = matcher.end();
				this.select(selectionStart, selectionEnd);
				this.lastSearch = find;
			} else {
				// no result found: search from begin
				if (matcher.find(0)) {
					selectionStart = matcher.start();
					if (selectionStart != start) {
						// result found
						int selectionEnd = matcher.end();
						this.select(selectionStart, selectionEnd);
						this.lastSearch = find;
					}
				}
			}
		}
	}

	public void findNext() {
		if (this.lastSearch != null) {
			this.find(this.lastSearch);
		}
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableTextComponent#findNext(java.awt.event.ActionEvent)
	 */
	@Override
	public void findNext(ActionEvent e) {
		ETextArea.class.cast(this).findNext();
	}

	public void fireCaretUpdate() {
		this.fireCaretUpdate(new ObjectWrapper(this).get("caretEvent", CaretEvent.class));
	}

	public String getFileExt() {
		return "txt";
	}

	public ETextAreaHighlightPainter getHighlightPainter() {
		if (this.highlightPainter == null) {
			this.highlightPainter = new SearchHighlightPainter();
		}
		return this.highlightPainter;
	}

	/**
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return this;
	}

	public JToolBar getToolbar() {
		return new EToolBar(this.getComponentPopupMenu());
	}

	/**
	 * @see javax.swing.text.JTextComponent#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		if (this.cfg.isTooltips()) {
			try {
				int offs = this.viewToModel(event.getPoint());
				int startIndex = Utilities.getWordStart(this, offs);
				int endIndex = Utilities.getWordEnd(this, offs);
				String substring = this.getText().substring(startIndex, endIndex);
				return StringUtils.isBlank(substring) ? null : substring.trim();
			} catch (BadLocationException ex) {
				return null;
			}
		}
		return super.getToolTipText(event);
	}

	/**
	 *
	 * @see org.swingeasy.ValidationDemo.HasValue#getValue()
	 */
	@Override
	public String getValue() {
		String text = this.getText();
		return StringUtils.isBlank(text) ? null : text;
	}

	/** Creates highlights around all occurrences of pattern in textComp */
	public void highlightAll(String patternText) {
		// First remove all old highlights
		this.removeHighlights();

		try {
			Pattern pattern = Pattern.compile(patternText, Pattern.CASE_INSENSITIVE);
			String text = this.getText();
			// System.out.println("finding " + text);
			Matcher matcher = pattern.matcher(text);
			while (matcher.find()) {
				// System.out.println("highlighting: " + matcher.start() + " -> " + matcher.end());
				// Create highlighter using private painter and apply around pattern
				this.addHighlight(matcher.start(), matcher.end(), this.getHighlightPainter());
				this.lastSearch = patternText;
			}
		} catch (BadLocationException e) {
			//
		}
	}

	protected void init(ETextAreaConfig config) {
		this.setEditable(config.isEnabled());

		this.setAutoScroll(config.isAutoScroll());

		if (config.isDefaultPopupMenu()) {
			this.installPopupMenuAction(EComponentPopupMenu.installTextComponentPopupMenu(this));
		}

		if (config.isLocalized()) {
			UIUtils.registerLocaleChangeListener((EComponentI) this);
		}

		this.addDocumentKeyListener(new DocumentKeyListener() {
			@Override
			public void update(Type type, DocumentEvent e) {
				String value = ETextArea.this.getValue();
				for (ValueChangeListener<String> valueChangeListener : ETextArea.this.valueChangeListeners) {
					valueChangeListener.valueChanged(value);
				}
			}
		});

		if (config.getText() != null) {
			this.setText(config.getText());
		}

		if (config.isTooltips()) {
			ToolTipManager.sharedInstance().registerComponent(this);
		}
	};

	public JScrollPane inScrollPane(boolean autoscroll) {
		return EComponentHelper.inScrollPane(this, autoscroll);
	}

	protected void installPopupMenuAction(EComponentPopupMenu popupMenu) {
		popupMenu.addSeparator();
		this.actions = new Action[] { //
				//
				new OpenAction(this), //
				new SaveAction(this), //
				new PrintAction(this) //
		};
		for (Action action : this.actions) {
			if (action == null) {
				popupMenu.addSeparator();
			} else {
				popupMenu.add(action);
				EComponentPopupMenu.accelerate(this, action);
			}
		}
		popupMenu.checkEnabled();
		popupMenu.addSeparator();
		ServiceLoader<ETextAreaExporter> exporterService = ServiceLoader.load(ETextAreaExporter.class);
		Iterator<ETextAreaExporter> iterator = exporterService.iterator();
		while (iterator.hasNext()) {
			try {
				ETextAreaExporter exporter = iterator.next();
				EComponentExporterAction<ETextArea> action = new EComponentExporterAction<ETextArea>(exporter, this);
				popupMenu.add(action);
			} catch (ServiceConfigurationError ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public void removeDocumentKeyListener(DocumentKeyListener listener) {
		this.getDocument().removeDocumentListener(listener);
		this.removeKeyListener(listener);
	}

	/** Removes only our private highlights */
	public void removeHighlights() {
		this.removeHighlights(this.getHighlightPainter());
	}

	public void removeHighlights(ETextAreaHighlightPainter painter) {
		Highlighter hilite = this.getHighlighter();
		for (Highlight hi : hilite.getHighlights()) {
			if (hi.getPainter().equals(painter)) {
				hilite.removeHighlight(hi);
			}
		}
	}

	/**
	 *
	 * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(ValueChangeListener<String> listener) {
		this.valueChangeListeners.remove(listener);
	}

	public void replace(String find, String replace) {
		this.setText(this.getText().replace(find, replace));
	}

	public void replaceAll(String find, String replace) {
		this.setText(this.getText().replaceAll(find, replace));
	}

	/**
	 * enable/disable automatic scrolling to bottom when a line is added
	 */
	public boolean setAutoScroll(boolean enable) {
		Caret caret = this.getCaret();
		if (!(caret instanceof DefaultCaret)) {
			return false;
		}
		((DefaultCaret) caret).setUpdatePolicy(enable ? DefaultCaret.ALWAYS_UPDATE : DefaultCaret.NEVER_UPDATE);
		return true;
	}

	/**
	 *
	 * @see org.swingeasy.ETextComponentI#setCaret(int)
	 */
	@Override
	public void setCaret(int pos) {
		this.setCaretPosition(pos);
		this.fireCaretUpdate();
	}

	/**
	 *
	 * @see org.swingeasy.ETextComponentI#setCaret(int, int)
	 */
	@Override
	public void setCaret(int from, int to) {
		this.setCaretPosition(from);
		this.moveCaretPosition(to);
		this.fireCaretUpdate();
	}

	public void setHighlightPainter(ETextAreaHighlightPainter highlightPainter) {
		this.highlightPainter = highlightPainter;
	}

	/**
	 * shows line numbers
	 */
	public JScrollPane withLineNumbers() {
		JScrollPane scrollPane = new JScrollPane(this);
		this.withLineNumbers(scrollPane);
		return scrollPane;
	}

	/**
	 * shows line numbers
	 */
	public TextLineNumber withLineNumbers(JScrollPane scrollPane) {
		TextLineNumber tln = new TextLineNumber(this);
		tln.setCurrentLineForeground(javax.swing.UIManager.getDefaults().getColor("List.selectionBackground"));
		tln.setUpdateFont(true);
		scrollPane.setRowHeaderView(tln);
		return tln;
	}
}
