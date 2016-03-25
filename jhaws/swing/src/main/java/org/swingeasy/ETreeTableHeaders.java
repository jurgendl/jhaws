package org.swingeasy;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.builder.CompareToBuilder;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * @author Jurgen
 */
public class ETreeTableHeaders<T> implements AdvancedTableFormat<ETableRecord<T>>, WritableTableFormat<ETableRecord<T>> {
	protected final List<String> columnNames = new Vector<String>();

	@SuppressWarnings("rawtypes")
	protected final List<Class> columnClasses = new Vector<Class>();

	protected final List<Boolean> editable = new Vector<Boolean>();

	public ETreeTableHeaders() {
		super();
	}

	public ETreeTableHeaders(String... cols) {
		for (String col : cols) {
			this.add(col);
		}
	}

	/**
	 * JDOC
	 *
	 * @param column
	 */
	public void add(String column) {
		this.add(column, Object.class);
	}

	/**
	 * JDOC
	 *
	 * @param column
	 * @param clazz
	 */
	public void add(String column, Class<?> clazz) {
		this.add(column, clazz, Boolean.FALSE);
	}

	/**
	 * JDOC
	 *
	 * @param column
	 * @param clazz
	 * @param edit
	 */
	public void add(String column, Class<?> clazz, Boolean edit) {
		this.columnNames.add(column);
		this.columnClasses.add(clazz);
		this.editable.add(edit);
	}

	/**
	 * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (this.columnNames.isEmpty()) {
			return Object.class;
		}
		Class<?> clas = this.columnClasses.get(columnIndex);
		return clas;
	}

	/**
	 * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnComparator(int)
	 */
	@Override
	public Comparator<?> getColumnComparator(int column) {
		return new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (((o1 == null) || (o1 instanceof Comparable)) && ((o2 == null) || (o2 instanceof Comparable))) {
					return new CompareToBuilder().append(o1, o2).toComparison();
				}
				return new CompareToBuilder().append(String.valueOf(o1), String.valueOf(o2)).toComparison();
			}
		};
	}

	/**
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}

	/**
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return this.columnNames.get(column);
	}

	public List<String> getColumnNames() {
		return this.columnNames;
	}

	/**
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
	 */
	@Override
	public Object getColumnValue(ETableRecord<T> row, int column) {
		return row.get(column);
	}

	/**
	 * @see ca.odell.glazedlists.gui.WritableTableFormat#isEditable(java.lang.Object, int)
	 */
	@Override
	public boolean isEditable(ETableRecord<T> baseObject, int column) {
		return (column != ETreeTable.TREE_COL_INDEX) && Boolean.TRUE.equals(this.editable.get(column));
	}

	/**
	 * @see ca.odell.glazedlists.gui.WritableTableFormat#setColumnValue(java.lang.Object, java.lang.Object, int)
	 */
	@Override
	public ETableRecord<T> setColumnValue(ETableRecord<T> row, Object editedValue, int column) {
		row.set(column, editedValue);
		return row;
	}
}
