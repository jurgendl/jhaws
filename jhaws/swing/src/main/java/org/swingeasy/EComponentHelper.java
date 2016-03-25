package org.swingeasy;

import java.awt.Component;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import org.swingeasy.system.SystemSettings;

public class EComponentHelper {
	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	public static <T> void copySelectionToClipboard(ETable<T> table) {
		EComponentHelper.copySelectionToClipboard("\t", table, table.getSelectedRecords());
	}

	/**
	 * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
	 */
	public static <T> void copySelectionToClipboard(ETreeTable<T> table) {
		EComponentHelper.copySelectionToClipboard("\t", table, table.getSelectedRecords());
	}

	protected static <T> void copySelectionToClipboard(String seperator, JTable table, List<? extends ETableRecord<T>> selectedRecords) {
		StringBuilder sb = new StringBuilder();
		int[] selectedColumns = table.getSelectedColumns();
		if ((selectedColumns == null) || (selectedColumns.length == 0)) {
			for (ETableRecord<T> record : selectedRecords) {
				for (int i = 0; i < record.size(); i++) {
					sb.append(record.getStringValue(i));
					if ((i + 1) < record.size()) {
						sb.append(seperator);
					}
				}
				sb.append(SystemSettings.getNewline());
			}
		} else {
			for (ETableRecord<T> record : selectedRecords) {
				for (int i : selectedColumns) {
					sb.append(record.getStringValue(i));
					if ((i + 1) < record.size()) {
						sb.append(seperator);
					}
				}
				sb.append(SystemSettings.getNewline());
			}
		}
		EComponentPopupMenu.copyToClipboard(sb.toString());
	}

	public static JScrollPane inScrollPane(JTextComponent textComponent, boolean autoscroll) {
		JScrollPane pane = new JScrollPane(textComponent);
		if (autoscroll) {
			DefaultCaret caret = (DefaultCaret) textComponent.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			pane.setAutoscrolls(true);
		}
		return pane;
	}

	/**
	 * @see http://www.coderanch.com/t/339632/GUI/java/synchronize-ScrollPane
	 */
	public static void bindVerticalScrolling(JScrollPane jScrollPane1, JScrollPane jScrollPane2) {
		jScrollPane2.getVerticalScrollBar().setModel(jScrollPane1.getVerticalScrollBar().getModel());
	}

	public static void bindHorizontalScrolling(JScrollPane jScrollPane1, JScrollPane jScrollPane2) {
		jScrollPane2.getHorizontalScrollBar().setModel(jScrollPane1.getHorizontalScrollBar().getModel());
	}

	/**
	 * Sets the preferred width of the visible column specified by vColIndex. The column will be just wide enough to show the column head and the widest cell in the column. margin
	 * pixels are added to the left and right (resulting in an additional width of 2*margin pixels).
	 * 
	 * @param table
	 * @param vColIndex
	 * @param margin
	 */
	public static void packColumn(JTable table, int vColIndex, int margin) {
		TableColumnModel colModel = table.getColumnModel();
		TableColumn col = colModel.getColumn(vColIndex);
		int width = 0;

		// Get width of column header
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null) {
			renderer = table.getTableHeader().getDefaultRenderer();
		}
		Component comp = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		// Get maximum width of column data
		for (int r = 0; r < table.getRowCount(); r++) {
			renderer = table.getCellRenderer(r, vColIndex);
			comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		// Add margin
		width += 2 * margin;

		// Set the width
		col.setPreferredWidth(width);
		col.setWidth(width);
		col.setMaxWidth(width);
	}

	public static String removeHtml(String s) {
		return s.replaceAll("&nbsp;", " ").replaceAll("<br>", "\n").replaceAll("<br/>", "\n").replaceAll("<html>", "").replaceAll("</html>", "").replaceAll("<body>", "").replaceAll("</body>", "");
	}
}
