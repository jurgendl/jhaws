package org.swingeasy;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

/**
 * @author Jurgen
 */
public class ScrollDemo {
	public static void main(String[] args) {
		UIUtils.systemLookAndFeel();
		JFrame f = new JFrame();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			sb.append(i).append("\n");
		}

		JTextPane p = new JTextPane();
		p.setText(sb.toString());
		JScrollPane jsp = new JScrollPane(p);

		int width = (int) jsp.getPreferredSize().getWidth();
		jsp.getHorizontalScrollBar().setUnitIncrement(width / 100);
		jsp.getHorizontalScrollBar().setBlockIncrement(width / 10);
		System.out.println(width);

		int height = (int) jsp.getPreferredSize().getHeight();
		jsp.getVerticalScrollBar().setUnitIncrement(height / 100);
		jsp.getVerticalScrollBar().setBlockIncrement(height / 10);
		System.out.println(height);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		f.getContentPane().add(split, BorderLayout.CENTER);

		split.setLeftComponent(jsp);

		JTextPane p2 = new JTextPane();
		p2.setText(sb.toString());
		JScrollPane jsp2 = new JScrollPane(p2);

		split.setRightComponent(jsp2);

		EComponentHelper.bindVerticalScrolling(jsp, jsp2);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 400);
		f.setVisible(true);

		split.setDividerLocation(.5d);
	}
}
