package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class ListDemo2 {
	// FIXME localization doesn't seem to work anymore
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		UIUtils.systemLookAndFeel();
		SystemSettings.setCurrentLocale(Locale.ENGLISH);
		EListConfig cfg = new EListConfig()
				.setBackgroundRenderer(new EComponentGradientRenderer(EComponentGradientRenderer.GradientOrientation.VERTICAL, Color.white, new Color(212, 212, 212)));
		cfg.setSortable(false);
		@SuppressWarnings("rawtypes")
		EList cc = new EList(cfg);
		{
			JFrame f = new JFrame();
			JScrollPane scroller = new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			cc.addRowHeader(scroller);
			f.getContentPane().add(scroller, BorderLayout.CENTER);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			cc = cc.stsi();
			f.setSize(400, 400);
			{
				JPanel jp = new JPanel(new FlowLayout());
				ButtonGroup bg = new ButtonGroup();
				{
					JRadioButton jrben = new JRadioButton("en");
					bg.add(jrben);
					jrben.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							SystemSettings.setCurrentLocale(Locale.ENGLISH);
						}
					});
					jp.add(jrben);
					jrben.setSelected(true);
				}
				{
					JRadioButton jrbnl = new JRadioButton("nl");
					bg.add(jrbnl);
					jrbnl.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							SystemSettings.setCurrentLocale(new Locale("nl"));
						}
					});
					jp.add(jrbnl);
				}
				f.getContentPane().add(jp, BorderLayout.NORTH);
			}
			{
				JButton btn = new JButton("non localized - dialog");
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						CustomizableOptionPane.showCustomDialog(null, new JLabel("non localized - dialogcomponent"), "non localized - title", MessageType.QUESTION,
								OptionType.YES_NO_CANCEL, null, null);
					}
				});
				f.getContentPane().add(btn, BorderLayout.SOUTH);
			}
			f.setVisible(true);
			f.setLocationRelativeTo(null);
		}

		cc.addRecord(new EListRecord<Date>(new Date()));
		cc.addRecord(new EListRecord<Color>(Color.red));
		cc.addRecord(new EListRecord<Long>(1000l));
		cc.addRecord(new EListRecord<Integer>(100));
		cc.addRecord(new EListRecord<Float>(100.01f));
		cc.addRecord(new EListRecord<Double>(1000.001d));
		cc.addRecord(new EListRecord<Boolean>(true));
		cc.addRecord(new EListRecord<byte[]>("bytes".getBytes()));

		for (int i = 0; i < 1000; i++) {
			cc.addRecord(new EListRecord<Integer>(i));
		}
	}
}
