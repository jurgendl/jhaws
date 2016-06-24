package org.swingeasy.list.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.SwingConstants;

/**
 * @see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6723524
 */
public class BooleanListCellRenderer extends EListCellRenderer<Boolean> {
	private static final long serialVersionUID = 2577869717107398445L;

	private JCheckBox renderer;

	public BooleanListCellRenderer() {
		renderer = new JCheckBox() {
			private static final long serialVersionUID = 2759192766733886746L;

			@Override
			protected void paintComponent(Graphics g) {
				BooleanListCellRenderer.this.renderBackground(this, g);
				super.paintComponent(g);
			}
		};
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		renderer.setBorderPaintedFlat(true);
		renderer.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}

	/**
	 * @see org.swingeasy.list.renderer.EListCellRenderer#render(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	protected Component render(JList<?> list, Boolean b, int index, boolean isSelected, boolean cellHasFocus) {
		super_getListCellRendererComponent(list, b, index, isSelected, cellHasFocus);
		if (b != null) {
			renderer.setSelected(b);
		} else {
			renderer.setSelected(false);
		}
		if (isSelected) {
			renderer.setForeground(list.getSelectionForeground());
			renderer.setBackground(list.getSelectionBackground());
			renderer.setOpaque(true);
		} else {
			Color bg = getBackground();
			renderer.setForeground(getForeground());
			// We have to create a new color object because Nimbus returns
			// a color of type DerivedColor, which behaves strange, not sure
			// why.
			renderer.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
			renderer.setOpaque(false);
		}
		return renderer;
	}
}