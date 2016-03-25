package org.swingeasy;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;

public class RowNumberList extends JList<Object>implements ChangeListener, PropertyChangeListener {
	public class RowNumberListModel implements ListModel<Object> {
		protected final ListModel<?> parentModel;

		public RowNumberListModel(ListModel<?> parentModel) {
			this.parentModel = parentModel;
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			this.parentModel.addListDataListener(l);
		}

		@Override
		public Object getElementAt(int index) {
			return String.valueOf(index + 1);
		}

		@Override
		public int getSize() {
			return this.parentModel.getSize();
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			this.parentModel.removeListDataListener(l);
		}
	}

	private static final long serialVersionUID = 1043313908886653227L;

	protected JList<?> main;

	public RowNumberList(JList<?> list) {
		this(list, 4);
	}

	protected RowNumberList(JList<?> list, double cw) {
		this.main = list;
		this.main.addPropertyChangeListener(this);

		this.setFocusable(false);
		this.setModel(new RowNumberListModel(this.main.getModel()));
		this.setSelectionModel(this.main.getSelectionModel());

		@SuppressWarnings("unchecked")
		ListCellRenderer<Object> renderer = ListCellRenderer.class.cast(list.getCellRenderer());
		if (renderer instanceof JLabel) {
			JLabel labelRenderer = JLabel.class.cast(renderer);
			labelRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		this.setCellRenderer(renderer);

		this.setFixedCellWidth((int) cw);
		this.setFixedCellHeight(list.getFixedCellHeight());
	}

	public RowNumberList(JList<?> list, int chars) {
		this(list, new JTextField(chars).getPreferredSize().getWidth());
	}

	@Override
	public void addNotify() {
		super.addNotify();

		Component c = this.getParent();
		if (c instanceof JViewport) {
			JViewport viewport = (JViewport) c;
			viewport.addChangeListener(this);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if ("selectionModel".equals(e.getPropertyName())) {
			this.setSelectionModel(this.main.getSelectionModel());
		}
		if ("model".equals(e.getPropertyName())) {
			this.setModel(new RowNumberListModel(this.main.getModel()));
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JViewport viewport = (JViewport) e.getSource();
		JScrollPane scrollPane = (JScrollPane) viewport.getParent();
		scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
	}
}