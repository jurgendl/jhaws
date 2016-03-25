package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang3.StringUtils;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.table.editor.BooleanTableCellEditor;
import org.swingeasy.table.editor.ColorTableCellEditor;
import org.swingeasy.table.editor.DateTimeTableCellEditor;
import org.swingeasy.table.editor.NumberTableCellEditor;
import org.swingeasy.table.renderer.BooleanTableCellRenderer;
import org.swingeasy.table.renderer.ByteArrayTableCellRenderer;
import org.swingeasy.table.renderer.ColorTableCellRenderer;
import org.swingeasy.table.renderer.DateTimeTableCellRenderer;
import org.swingeasy.table.renderer.ETableCellRenderer;
import org.swingeasy.table.renderer.NumberTableCellRenderer;
import org.swingeasy.table.renderer.URITableCellRenderer;
import org.swingeasy.table.renderer.URLTableCellRenderer;

import ca.odell.glazedlists.DefaultExternalExpansionModel;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.ExpansionModel;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TreeTableSupport;

/**
 * @author Jurgen
 * @see http://publicobject.com/glazedlists/glazedlists-1.8.0/api/ca/odell/glazedlists/swing/TreeTableSupport.html
 */
public class ETreeTable<T> extends JTable implements ETreeTableI<T>, Iterable<ETreeTableRecord<T>>, ReadableComponent {
	private static final long serialVersionUID = -1389100924292211731L;

	protected ETreeTable<T> stsi;

	protected final ETreeTableConfig cfg;

	protected Action[] actions;

	protected EventList<ETreeTableRecord<T>> records;

	protected DefaultEventTableModel<ETreeTableRecord<T>> tableModel;

	protected DefaultEventSelectionModel<ETreeTableRecord<T>> tableSelectionModel;

	protected ETreeTableHeaders<T> tableFormat;

	protected TreeList<ETreeTableRecord<T>> treeList;

	protected Format<ETreeTableRecord<T>> format;

	protected static final int TREE_COL_INDEX = 0;

	protected ETreeTable() {
		this.cfg = null;
	}

	public ETreeTable(ETreeTableConfig cfg, ETreeTableHeaders<T> headers) {
		this.init(this.cfg = cfg.lock(), headers);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#addRecord(org.swingeasy.ETreeTableRecord)
	 */
	@Override
	public void addRecord(final ETreeTableRecord<T> record) {
		this.records.add(record);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#addRecords(java.util.Collection)
	 */
	@Override
	public void addRecords(final Collection<ETreeTableRecord<T>> r) {
		this.records.addAll(r);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#clear()
	 */
	@Override
	public void clear() {
		this.tableSelectionModel.clearSelection();
		this.records.clear();
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	@Override
	public void copy(ActionEvent e) {
		EComponentHelper.copySelectionToClipboard(this);
	}

	/**
	 * @see javax.swing.JTable#createDefaultEditors()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void createDefaultEditors() {
		super.createDefaultEditors();
		this.defaultEditorsByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue(BooleanTableCellEditor.class.getName()));
		this.defaultEditorsByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue(DateTimeTableCellEditor.class.getName()));
		this.defaultEditorsByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue(ColorTableCellEditor.class.getName()));
		this.defaultEditorsByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellEditor.class.getName()));
	}

	/**
	 * @see javax.swing.JTable#createDefaultRenderers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void createDefaultRenderers() {
		super.createDefaultRenderers();
		this.defaultRenderersByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue(BooleanTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue(DateTimeTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue(ColorTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Float.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Double.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(byte[].class, new javax.swing.UIDefaults.ProxyLazyValue(ByteArrayTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Byte[].class, new javax.swing.UIDefaults.ProxyLazyValue(ByteArrayTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(URL.class, new javax.swing.UIDefaults.ProxyLazyValue(URLTableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(URI.class, new javax.swing.UIDefaults.ProxyLazyValue(URITableCellRenderer.class.getName()));
		this.defaultRenderersByColumnClass.put(Object.class, new javax.swing.UIDefaults.ProxyLazyValue(ETableCellRenderer.class.getName()));
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getColumnValueAtVisualColumn(int)
	 */
	@Override
	public Object getColumnValueAtVisualColumn(int i) {
		return this.getColumnModel().getColumn(i).getHeaderValue();
	}

	/**
	 * @see javax.swing.JTable#getDefaultEditor(java.lang.Class)
	 */
	@Override
	public TableCellEditor getDefaultEditor(Class<?> columnClass) {
		TableCellEditor de = super.getDefaultEditor(columnClass);
		if (de instanceof EComponentI) {
			UIUtils.registerLocaleChangeListener(EComponentI.class.cast(de));
			// } else if (de instanceof Component) {
			// UIUtils.registerLocaleChangeListener(Component.class.cast(de));
		}
		return de;
	}

	/**
	 * @see javax.swing.JTable#getDefaultRenderer(java.lang.Class)
	 */
	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		TableCellRenderer dr = super.getDefaultRenderer(columnClass);

		if (dr instanceof EComponentI) {
			UIUtils.registerLocaleChangeListener(EComponentI.class.cast(dr));
			// } else if (dr instanceof Component) {
			// UIUtils.registerLocaleChangeListener(Component.class.cast(dr));
		}
		return dr;
	}

	protected Frame getFrame(Component comp) {
		if (comp == null) {
			comp = this;
		}
		if (comp.getParent() instanceof Frame) {
			return (Frame) comp.getParent();
		}
		return this.getFrame(comp.getParent());
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getHeadernames()
	 */
	@Override
	public List<String> getHeadernames() {
		List<String> columnNames = this.tableFormat.getColumnNames();
		return columnNames;
	}

	public ETreeTableHeaders<T> getHeaders() {
		return this.tableFormat;
	}

	public ETreeTable<T> getOriginal() {
		return this;
	}

	/**
	 * @see org.swingeasy.HasParentComponent#getParentComponent()
	 */
	@Override
	public JComponent getParentComponent() {
		return this;
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getRecordAtVisualRow(int)
	 */
	@Override
	public ETreeTableRecord<T> getRecordAtVisualRow(int i) {
		return this.records.get(i);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getRecords()
	 */
	@Override
	public List<ETreeTableRecord<T>> getRecords() {
		return this.records;
	}

	/**
	 * @see #getRowHeader()
	 */
	public RowNumberTable getRowHeader() {
		return this.getRowHeader(4);
	}

	/**
	 * gets row header that can be added to the RowHeaderView of a {@link JScrollPane} or can be docked WEST in a {@link BorderLayout}
	 */
	public RowNumberTable getRowHeader(int cw) {
		return new RowNumberTable(this, cw);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getSelectedCell()
	 */
	@Override
	public Object getSelectedCell() {
		ETableRecord<T> record = this.getSelectedRecord();
		if (record == null) {
			return null;
		}
		return record.get(this.getSelectedColumn());
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getSelectedCells()
	 */
	@Override
	public List<Object> getSelectedCells() {
		List<Object> cells = new ArrayList<Object>();
		for (ETreeTableRecord<T> record : this.getSelectedRecords()) {
			int selectedColumn = this.getSelectedColumn();
			if (selectedColumn != -1) {
				cells.add(record.get(selectedColumn));
			} else {
				for (int i = 0; i < record.size(); i++) {
					cells.add(record.get(i));
				}
			}
		}
		return cells;
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getSelectedRecord()
	 */
	@Override
	public ETreeTableRecord<T> getSelectedRecord() {
		EventList<ETreeTableRecord<T>> selected = this.tableSelectionModel.getSelected();
		return selected.isEmpty() ? null : selected.iterator().next();
	}

	/**
	 * @see org.swingeasy.ETreeTableI#getSelectedRecords()
	 */
	@Override
	public List<ETreeTableRecord<T>> getSelectedRecords() {
		return this.tableSelectionModel.getSelected();
	}

	/**
	 * JDOC
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ETreeTable<T> getSimpleThreadSafeInterface() {
		try {
			if (this.stsi == null) {
				this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETreeTable.class, this, ETreeTableI.class);
			}
			return this.stsi;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	protected void init(ETreeTableConfig config, ETreeTableHeaders<T> headers) {
		this.tableFormat = headers;

		Collection<ETreeTableRecord<T>> coll = new ArrayList<ETreeTableRecord<T>>();
		this.records = GlazedLists.threadSafeList(GlazedLists.eventList(coll));

		this.format = new Format<ETreeTableRecord<T>>() {
			@Override
			public boolean allowsChildren(ETreeTableRecord<T> element) {
				return true;
			}

			@Override
			public Comparator<ETreeTableRecord<T>> getComparator(int depth) {
				// return (Comparator) GlazedLists.beanPropertyComparator(ETreeTableRecord/* <Rec> */.class, "key", "value");
				return new Comparator<ETreeTableRecord<T>>() {
					@Override
					public int compare(ETreeTableRecord<T> o1, ETreeTableRecord<T> o2) {
						return o1.compareTo(o2);
					}
				};
			}

			@Override
			public void getPath(List<ETreeTableRecord<T>> path, ETreeTableRecord<T> element) {
				ArrayList<ETreeTableRecord<T>> stack = new ArrayList<ETreeTableRecord<T>>();
				ETreeTableRecord<T> current = element;
				while (current != null) {
					stack.add(current);
					current = current.getParent();
				}
				for (int i = stack.size() - 1; i >= 0; i--) {
					path.add(stack.get(i));
				}
			}
		};

		DefaultExternalExpansionModel<ETreeTableRecord<T>> expansionModel = new DefaultExternalExpansionModel<ETreeTableRecord<T>>(new ExpansionModel<ETreeTableRecord<T>>() {
			@Override
			public boolean isExpanded(ETreeTableRecord<T> element, List<ETreeTableRecord<T>> path) {
				return false; // all start of collapsed
			}

			@Override
			public void setExpanded(ETreeTableRecord<T> element, List<ETreeTableRecord<T>> path, boolean expanded) {
				//
			}
		});
		this.treeList = new TreeList<ETreeTableRecord<T>>(this.records, this.format, expansionModel);

		this.tableModel = new DefaultEventTableModel<ETreeTableRecord<T>>(this.treeList, this.tableFormat);
		this.setModel(this.tableModel);

		this.tableSelectionModel = new DefaultEventSelectionModel<ETreeTableRecord<T>>(this.records);
		this.tableSelectionModel.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
		this.setSelectionModel(this.tableSelectionModel);

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					try {
						TreeTableSupport support = TreeTableSupport.install(ETreeTable.this, ETreeTable.this.treeList, ETreeTable.TREE_COL_INDEX);
						support.setArrowKeyExpansionEnabled(true);
						support.setShowExpanderForEmptyParent(false);
						support.setSpaceKeyExpansionEnabled(true);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			});
		} catch (InterruptedException ex) {
			//
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getTargetException());
		}

		UIUtils.registerLocaleChangeListener((EComponentI) this);

		if (config.isDefaultPopupMenu()) {
			this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
		}
	}

	/**
	 * JDOC
	 */
	protected void installPopupMenuAction(EComponentPopupMenu menu) {
		this.actions = new Action[] {};
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
		ServiceLoader<ETreeTableExporter<T>> exporterService = (ServiceLoader) ServiceLoader.load(ETreeTableExporter.class);
		Iterator<ETreeTableExporter<T>> iterator = exporterService.iterator();
		while (iterator.hasNext()) {
			try {
				ETreeTableExporter<T> exporter = iterator.next();
				EComponentExporterAction<ETreeTable<T>> action = new EComponentExporterAction<ETreeTable<T>>(exporter, this);
				menu.add(action);
			} catch (ServiceConfigurationError ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	/**
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return this.cfg.isEditable() && (column != ETreeTable.TREE_COL_INDEX) && super.isCellEditable(row, column);
	}

	/**
	 * threadsafe unmodifiable iterator
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ETreeTableRecord<T>> iterator() {
		return Collections.unmodifiableCollection(this.getRecords()).iterator();
	}

	/**
	 * @see org.swingeasy.ETreeTableI#packColumn(int)
	 */
	@Override
	public void packColumn(int vColIndex) {
		this.packColumn(vColIndex, 4);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#packColumn(int, int)
	 */
	@Override
	public void packColumn(int vColIndex, int margin) {
		EComponentHelper.packColumn(this, vColIndex, margin);
	}

	/**
	 * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
	 */
	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = this.super_prepareRenderer(renderer, rowIndex, vColIndex);
		if (c instanceof JLabel) {
			String text = JLabel.class.cast(c).getText();
			JLabel.class.cast(c).setToolTipText(StringUtils.isNotBlank(text) ? text : null);
		}
		return c;
	}

	/**
	 * @see org.swingeasy.ETreeTableI#removeAllRecords()
	 */
	@Override
	public void removeAllRecords() {
		this.records.clear();
	}

	/**
	 * @see org.swingeasy.ETreeTableI#removeRecord(org.swingeasy.ETableRecord)
	 */
	@Override
	public void removeRecord(final ETableRecord<T> record) {
		this.records.remove(record);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#removeRecordAtVisualRow(int)
	 */
	@Override
	public void removeRecordAtVisualRow(final int i) {
		this.records.remove(this.records.get(i));
	}

	/**
	 * remove rowheader
	 */
	public void removeRowHeader(JScrollPane scrollpane) {
		if (scrollpane.getRowHeader().getView() instanceof RowNumberTable) {
			scrollpane.getRowHeader().removeAll();
			scrollpane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new JComponent() {
				private static final long serialVersionUID = -7233488612928322618L;
			});
		}
	}

	/**
	 * @see org.swingeasy.ETreeTableI#scrollToVisibleRecord(org.swingeasy.ETableRecord)
	 */
	@Override
	public void scrollToVisibleRecord(ETableRecord<T> record) {
		if (!this.isDisplayable()) {
			throw new IllegalArgumentException("can only be used when table is displayable (visible)"); //$NON-NLS-1$
		}
		int index = this.getRecords().indexOf(record);
		Rectangle cellbounds = this.getCellRect(index, index, true);
		this.scrollRectToVisible(cellbounds);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#selectCell(java.awt.Point)
	 */
	@Override
	public void selectCell(Point p) {
		int r = this.rowAtPoint(p);
		if (r == -1) {
			return;
		}
		int c = this.columnAtPoint(p);
		if (c == -1) {
			return;
		}
		// if already selected: do nothing (this keeps multiple selection when current cell is part of it
		for (int sr : this.getSelectedRows()) {
			if (sr == r) {
				for (int sc : this.getSelectedColumns()) {
					if (sc == c) {
						return;
					}
				}
			}
		}
		this.setColumnSelectionInterval(c, c);
		this.setRowSelectionInterval(r, r);
	}

	/**
	 * @see org.swingeasy.ETreeTableI#setLocale(java.util.Locale)
	 */
	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		this.repaint();
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public ETreeTable<T> stsi() {
		return this.getSimpleThreadSafeInterface();
	}

	/**
	 * @see #getSimpleThreadSafeInterface()
	 */
	public ETreeTable<T> STSI() {
		return this.getSimpleThreadSafeInterface();
	}

	/**
	 * calls original renderer
	 */
	protected Component super_prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		return super.prepareRenderer(renderer, rowIndex, vColIndex);
	}
}
