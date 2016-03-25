package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.swingeasy.EComponentPopupMenu.CheckEnabled;
import org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.list.renderer.BooleanListCellRenderer;
import org.swingeasy.list.renderer.ByteArrayListCellRenderer;
import org.swingeasy.list.renderer.ColorListCellRenderer;
import org.swingeasy.list.renderer.DateListCellRenderer;
import org.swingeasy.list.renderer.DateTimeListCellRenderer;
import org.swingeasy.list.renderer.EListCellRenderer;
import org.swingeasy.list.renderer.NumberListCellRenderer;
import org.swingeasy.list.renderer.TimeListCellRenderer;
import org.swingeasy.list.renderer.URIListCellRenderer;
import org.swingeasy.list.renderer.URLListCellRenderer;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

/**
 * @author Jurgen
 */
public class EList<T> extends JList<EListRecord<T>>implements EListI<T>, Iterable<EListRecord<T>>, ReadableComponent, HasValue<T> {
	private class DelegatingListCellRenderer implements ListCellRenderer<Object> {
		protected transient Hashtable<Class<?>, ListCellRenderer<?>> defaultRenderersByClass = new Hashtable<>();

		public DelegatingListCellRenderer(ListCellRenderer<?> defaultListCellRenderer, EComponentRenderer backgroundRenderer) {
			defaultRenderer(java.sql.Date.class, new DateListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(java.sql.Time.class, new TimeListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Date.class, new DateTimeListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Number.class, new NumberListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Float.class, new NumberListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Double.class, new NumberListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Color.class, new ColorListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Boolean.class, new BooleanListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Byte[].class, new ByteArrayListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(byte[].class, new ByteArrayListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(URL.class, new URLListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(URI.class, new URIListCellRenderer().setBackgroundRenderer(backgroundRenderer));
			defaultRenderer(Object.class, new EListCellRenderer<Object>().setBackgroundRenderer(backgroundRenderer));
		}

		@SuppressWarnings("unchecked")
		public <X> ListCellRenderer<X> getDefaultRenderer(Class<X> columnClass) {
			if (columnClass == null) {
				return null;
			}
			ListCellRenderer<X> renderer = (ListCellRenderer<X>) this.defaultRenderersByClass.get(columnClass);
			if (renderer != null) {
				return renderer;
			}
			return (ListCellRenderer<X>) this.getDefaultRenderer(columnClass.getSuperclass());
		}

		/**
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof EListRecord) {
				value = EListRecord.class.cast(value).get();
			}
			@SuppressWarnings("unchecked")
			Class<Object> clazz = (Class<Object>) value.getClass();
			ListCellRenderer<Object> _defaultRenderer = getDefaultRenderer(clazz);
			return _defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}

		protected void defaultRenderer(Class<?> columnClass, ListCellRenderer<?> renderer) {
			this.defaultRenderersByClass.put(columnClass, renderer);
		}

		public <X> void setDefaultRenderer(Class<X> columnClass, ListCellRenderer<X> renderer) {
			this.defaultRenderersByClass.put(columnClass, renderer);
		}

		public void setLocale(Locale l) {
			for (ListCellRenderer<?> renderer : this.defaultRenderersByClass.values()) {
				if (renderer instanceof Component) {
					Component.class.cast(renderer).setLocale(l);
				}
				if (renderer instanceof EComponentI) {
					EComponentI.class.cast(renderer).setLocale(l);
				}
			}
		}
	}

	private static class EListModel<T> extends DefaultEventListModel<EListRecord<T>> {
		protected EventList<EListRecord<T>> sourceList;

		protected EListFilterComponent<T> filtercomponent;

		public EListModel(EventList<EListRecord<T>> source) {
			super(source);
			this.sourceList = source;
		}
	}

	/**
	 * SelectAllAction
	 */
	protected class SelectAllAction extends EComponentPopupMenuAction<EList<T>> {
		private static final long serialVersionUID = -6873629703224034266L;

		public SelectAllAction(EList<T> component) {
			super(component, EComponentPopupMenu.SELECT_ALL, Resources.getImageResource("page_white_text_width.png"));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		}

		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int size = this.delegate.getRecords().size();
			int[] indices = new int[size];
			for (int i = 0; i < size; i++) {
				indices[i] = i;
			}
			this.delegate.setSelectedIndices(indices);
		}

		/**
		 * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
		 */
		@Override
		public boolean checkEnabled(CheckEnabled config) {
			return true;
		}
	}

	/**
	 * UnselectAction
	 */
	protected class UnselectAction extends EComponentPopupMenuAction<EList<T>> {
		private static final long serialVersionUID = 380268668990447297L;

		public UnselectAction(EList<T> component) {
			super(component, EComponentPopupMenu.UNSELECT, Resources.getImageResource("page_white_width.png"));
			this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		}

		/**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			this.delegate.setSelectedIndices(new int[0]);
		}

		/**
		 * @see org.swingeasy.EComponentPopupMenu.EComponentPopupMenuAction#checkEnabled(org.swingeasy.EComponentPopupMenu.CheckEnabled)
		 */
		@Override
		public boolean checkEnabled(CheckEnabled config) {
			return true;
		}
	}

	/**
	 * convert
	 */
	public static <T> List<EListRecord<T>> convert(Collection<T> records) {
		List<EListRecord<T>> list = new ArrayList<EListRecord<T>>();
		for (T r : records) {
			list.add(new EListRecord<T>(r));
		}
		return list;
	}

	/**
	 * convert
	 */
	@SafeVarargs
	public static <T> List<EListRecord<T>> convert(T... records) {
		return EList.convert(Arrays.asList(records));
	}

	/**
	 * convert
	 */
	public static <T> List<T> convertRecords(Collection<EListRecord<T>> records) {
		List<T> list = new ArrayList<T>();
		for (EListRecord<T> r : records) {
			list.add(r.get());
		}
		return list;
	}

	/**
	 * convert
	 */
	public static <T> List<T> convertRecords(EListRecord<T>[] records) {
		return EList.convertRecords(Arrays.asList(records));
	}

	private static <T> EListModel<T> createModel(EListConfig cfg) {
		EventList<EListRecord<T>> records = new BasicEventList<EListRecord<T>>();
		if (cfg.isSortable()) {
			records = new SortedList<EListRecord<T>>(records);
		}
		EListFilterComponent<T> filtercomponent = null;
		if (cfg.isFilterable()) {
			filtercomponent = new EListFilterComponent<T>(records);
			records = filtercomponent.grabRecords();
		}
		if (cfg.isThreadSafe()) {
			records = GlazedLists.threadSafeList(records);
		}
		EListModel<T> model = new EListModel<T>(records);
		model.filtercomponent = filtercomponent;
		return model;
	}

	private static final long serialVersionUID = -3602504810131193505L;

	protected EListConfig cfg;

	protected EventList<EListRecord<T>> records;

	protected DelegatingListCellRenderer delegatingListCellRenderer;

	protected EListSearchComponent<T> searchComponent = null;

	protected EListFilterComponent<T> filtercomponent = null;

	protected final List<ValueChangeListener<T>> valueChangeListeners = new ArrayList<ValueChangeListener<T>>();

	protected EList<T> stsi;

	protected Action[] actions;

	/**
	 * do not use, do not change access
	 */
	protected EList() {
		this.cfg = null;
		this.filtercomponent = null;
	}

	@SuppressWarnings("unchecked")
	public EList(EListConfig cfg) {
		super(EList.<T>createModel(cfg.lock()));
		this.init(this.cfg = cfg);
	}

	/**
	 * @see org.swingeasy.EListI#selectCell(java.awt.Point)
	 */
	@Override
	public void addCellSelection(Point point) {
		int idx = this.locationToIndex(point);
		if (idx != -1) {
			for (int sel : this.getSelectedIndices()) {
				if (sel == idx) {
					return;
				}
			}
			this.setSelectedIndex(idx);
		}
	}

	/**
	 * @see org.swingeasy.EListI#addRecord(org.swingeasy.EListRecord)
	 */
	@Override
	public void addRecord(EListRecord<T> r) {
		if (r == null) {
			throw new NullPointerException();
		}
		this.records.add(r);
	}

	/**
	 * @see org.swingeasy.EListI#addRecords(java.util.Collection)
	 */
	@Override
	public void addRecords(Collection<EListRecord<T>> r) {
		for (EListRecord<T> el : r) {
			if (el == null) {
				throw new NullPointerException();
			}
		}
		this.records.addAll(r);
	}

	/**
	 * add rowheader to scrollpane, 2 digit number
	 */
	public JScrollPane addRowHeader(JScrollPane scrollpane) {
		return this.addRowHeader(scrollpane, 2);
	}

	/**
	 * add rowheader to scrollpane
	 */
	public JScrollPane addRowHeader(JScrollPane scrollpane, int cw) {
		RowNumberList rowList = this.getRowHeader(cw);
		scrollpane.setRowHeaderView(rowList);
		return scrollpane;
	}

	/**
	 * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(ValueChangeListener<T> listener) {
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
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void copy(ActionEvent e) {
		EListRecord<T> selectedRecord = this.getSelectedRecord();
		if (selectedRecord == null) {
			return;
		}
		EComponentPopupMenu.copyToClipboard(selectedRecord.getStringValue());
	}

	/**
	 * @see javax.swing.JList#createSelectionModel()
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected ListSelectionModel createSelectionModel() {
		ListModel<EListRecord<T>> _model = this.getModel();
		if (_model instanceof EListModel) {
			return new DefaultEventSelectionModel<EListRecord<T>>(EListModel.class.cast(_model).sourceList);
		}
		return super.createSelectionModel();
	}

	public EListFilterComponent<T> getFiltercomponent() {
		return this.filtercomponent;
	}

	public EList<T> getOriginal() {
		return this;
	}

	/**
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return this;
	}

	public int getRecordCount() {
		return this.records.size();
	}

	/**
	 * @see org.swingeasy.EListI#getRecords()
	 */
	@Override
	public List<EListRecord<T>> getRecords() {
		return this.records;
	}

	/**
	 * gets row header that can be added to the RowHeaderView of a {@link JScrollPane} or can be docked WEST in a {@link BorderLayout}
	 */
	public RowNumberList getRowHeader(int cw) {
		return new RowNumberList(this, cw);
	}

	/**
	 * returns and creates if necessary {@link EListSearchComponent}
	 */
	public EListSearchComponent<T> getSearchComponent() {
		if (this.searchComponent == null) {
			this.searchComponent = new EListSearchComponent<T>(this);
			this.searchComponent.setLocale(this.getLocale());
		}
		return this.searchComponent;
	}

	/**
	 * @see org.swingeasy.EListI#getSelectedRecord()
	 */
	@Override
	public EListRecord<T> getSelectedRecord() {
		return this.getSelectedValue();
	}

	/**
	 * @see org.swingeasy.EListI#getSelectedRecords()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EListRecord<T>> getSelectedRecords() {
		List<EListRecord<T>> list = new ArrayList<EListRecord<T>>();
		for (Object o : this.getSelectedValuesList()) {
			list.add(EListRecord.class.cast(o));
		}
		return list;
	}

	/**
	 * JDOC
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public EList<T> getSimpleThreadSafeInterface() {
		try {
			if (this.stsi == null) {
				this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(EList.class, this, EListI.class);
			}
			return this.stsi;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * @see javax.swing.JList#getToolTipText(java.awt.event.MouseEvent)
	 */
	@Override
	public String getToolTipText(MouseEvent evt) {
		String toolTipText = super.getToolTipText(evt);

		if (toolTipText != null) {
			return toolTipText;
		}

		int index = this.locationToIndex(evt.getPoint());

		if (index == -1) {
			return null;
		}

		EListRecord<T> item = this.getModel().getElementAt(index);

		return item.getTooltip();
	}

	/**
	 * @see org.swingeasy.HasValue#getValue()
	 */
	@Override
	public T getValue() {
		EListRecord<T> selectedRecord = this.getSelectedRecord();
		return selectedRecord == null ? null : selectedRecord.get();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void init(EListConfig config) {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					EList.this.addCellSelection(new Point(e.getX(), e.getY()));
				}
			}
		});

		EListModel elistModel = EListModel.class.cast(this.getModel());
		this.records = elistModel.sourceList;
		this.filtercomponent = elistModel.filtercomponent;
		if (this.filtercomponent != null) {
			this.filtercomponent.setList(this);
		}
		elistModel.filtercomponent = null;
		this.delegatingListCellRenderer = new DelegatingListCellRenderer(this.getCellRenderer(), config.getBackgroundRenderer());
		this.setCellRenderer(this.delegatingListCellRenderer);

		// drag and drop test jvm internally, intra jvm, tostring
		this.setDragEnabled(true);
		this.setTransferHandler(new EListTransferHandler<T>());

		if (config.isLocalized()) {
			UIUtils.registerLocaleChangeListener((EComponentI) this);
		}

		if (config.isDefaultPopupMenu()) {
			this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
		}

		if (config.isTooltips()) {
			ToolTipManager.sharedInstance().registerComponent(this);
		}

		this.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					T value = EList.this.getValue();
					for (ValueChangeListener<T> valueChangeListener : EList.this.valueChangeListeners) {
						valueChangeListener.valueChanged(value);
					}
				}
			}
		});
	}

	/**
	 * @see org.swingeasy.EListI#insertRecord(int, org.swingeasy.EListRecord)
	 */
	@Override
	public void insertRecord(int index, EListRecord<T> r) {
		if (r == null) {
			throw new NullPointerException();
		}
		this.records.add(index, r);
	}

	/**
	 * JDOC
	 */
	protected void installPopupMenuAction(EComponentPopupMenu menu) {
		this.actions = new Action[] { new SelectAllAction(this), new UnselectAction(this) };
		for (Action action : this.actions) {
			if (action == null) {
				menu.addSeparator();
			} else {
				menu.add(action);
				EComponentPopupMenu.accelerate(this, action);
			}
		}
		menu.checkEnabled();
		menu.addSeparator();
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ServiceLoader<EListExporter<T>> exporterService = (ServiceLoader) ServiceLoader.load(EListExporter.class);
		Iterator<EListExporter<T>> iterator = exporterService.iterator();
		while (iterator.hasNext()) {
			try {
				EListExporter<T> exporter = iterator.next();
				EComponentExporterAction<EList<T>> action = new EComponentExporterAction<EList<T>>(exporter, this);
				menu.add(action);
			} catch (ServiceConfigurationError ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	/**
	 * threadsafe unmodifiable iterator
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<EListRecord<T>> iterator() {
		return Collections.unmodifiableCollection(this.getRecords()).iterator();
	}

	/**
	 * @see org.swingeasy.EListI#moveSelectedDown()
	 */
	@Override
	public void moveSelectedDown() {
		int selectedIndex = this.getSelectedIndex();

		// none selected
		if (selectedIndex == -1) {
			return;
		}

		// last selected
		if (this.records.size() == (selectedIndex + 1)) {
			return;
		}

		EListRecord<T> rec = this.records.get(selectedIndex);
		this.records.remove(rec);
		this.records.add(selectedIndex + 1, rec);
		this.setSelectedIndex(selectedIndex + 1);
	}

	/**
	 * @see org.swingeasy.EListI#moveSelectedDown()
	 */
	@Override
	public void moveSelectedUp() {
		int selectedIndex = this.getSelectedIndex();

		// none selected
		if (selectedIndex == -1) {
			return;
		}

		// first selected
		if (selectedIndex == 0) {
			return;
		}

		EListRecord<T> rec = this.records.get(selectedIndex);
		this.records.remove(rec);
		this.records.add(selectedIndex - 1, rec);
		this.setSelectedIndex(selectedIndex - 1);
	}

	/**
	 * @see org.swingeasy.EListI#removeAllRecords()
	 */
	@Override
	public void removeAllRecords() {
		this.records.clear();
	}

	/**
	 * @see org.swingeasy.EListI#removeRecord(org.swingeasy.EListRecord)
	 */
	@Override
	public void removeRecord(EListRecord<T> record) {
		this.records.remove(record);
	}

	/**
	 * @see org.swingeasy.EListI#removeRecords(java.util.Collection)
	 */
	@Override
	public void removeRecords(Collection<EListRecord<T>> recordsToRemove) {
		this.records.removeAll(recordsToRemove);
	}

	/**
	 * @see org.swingeasy.EListI#removeSelectedRecords()
	 */
	@Override
	public void removeSelectedRecords() {
		Collection<EListRecord<T>> selectedRecords = this.getSelectedRecords();
		this.records.removeAll(selectedRecords);
	}

	/**
	 * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(ValueChangeListener<T> listener) {
		this.valueChangeListeners.remove(listener);
	}

	/**
	 * @see org.swingeasy.EListI#scrollToVisibleRecord(org.swingeasy.EListRecord)
	 */
	@Override
	public void scrollToVisibleRecord(EListRecord<T> record) {
		if (!this.isDisplayable()) {
			throw new IllegalArgumentException("can only be used when list is displayable (visible)"); //$NON-NLS-1$
		}
		int index = this.records.indexOf(record);
		Rectangle cellbounds = this.getCellBounds(index, index);
		this.scrollRectToVisible(cellbounds);
	}

	/**
	 * @see org.swingeasy.EListI#selectCell(java.awt.Point)
	 */
	@Override
	public void selectCell(Point point) {
		int idx = this.locationToIndex(point);
		if (idx != -1) {
			this.setSelectedIndex(idx);
		}
	}

	/**
	 * JDOC
	 */
	public <R> void setDefaultRenderer(Class<R> columnClass, ListCellRenderer<R> renderer) {
		this.delegatingListCellRenderer.setDefaultRenderer(columnClass, renderer);
	}

	/**
	 * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.delegatingListCellRenderer.setLocale(l);
		this.repaint();
	}

	/**
	 * @see org.swingeasy.EListI#setSelectedRecord(org.swingeasy.EListRecord)
	 */
	@Override
	public void setSelectedRecord(EListRecord<T> record) {
		if (!this.records.contains(record)) {
			throw new IllegalArgumentException();
		}
		this.setSelectedValue(record, true);
	}

	/**
	 * @see org.swingeasy.EListI#setSelectedRecords(java.util.Collection)
	 */
	@Override
	public void setSelectedRecords(Collection<EListRecord<T>> eListRecords) {
		int[] indices = new int[eListRecords.size()];
		int i = 0;
		for (EListRecord<T> toSelect : eListRecords) {
			int index = this.records.indexOf(toSelect);
			if (index == -1) {
				throw new IllegalArgumentException();
			}
			indices[i++] = index;
		}
		this.setSelectedIndices(indices);
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public EList<T> stsi() {
		return this.getSimpleThreadSafeInterface();
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public EList<T> STSI() {
		return this.getSimpleThreadSafeInterface();
	}
}
