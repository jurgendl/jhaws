package org.swingeasy;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.xenoage.util.gui.FontChooserComboBox;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class EFontChooser extends JPanel implements EComponentI {
	private static final long serialVersionUID = 3686474630456833708L;

	public static Font showDialog(JComponent parent) {
		return showDialog(parent, (String) null);
	}

	public static Font showDialog(JComponent parent, Font defaultFont) {
		EFontChooser fc = new EFontChooser(defaultFont);
		if (ResultType.OK == CustomizableOptionPane.showCustomDialog(parent, fc, Messages.getString(fc.getLocale(), "font-chooser-title"), MessageType.QUESTION, OptionType.OK_CANCEL, null, null)) {
			return fc.getSelectedFont();
		}
		return null;
	}

	public static Font showDialog(JComponent parent, String defaultFont) {
		EFontChooser fc = new EFontChooser(defaultFont);
		if (ResultType.OK == CustomizableOptionPane.showCustomDialog(parent, fc, Messages.getString(fc.getLocale(), "font-chooser-title"), MessageType.QUESTION, OptionType.OK_CANCEL, null, null)) {
			return fc.getSelectedFont();
		}
		return null;
	}

	protected FontChooserComboBox fc = new FontChooserComboBox();

	protected ESpinner<Integer> size = new ESpinner<Integer>(new SpinnerNumberModel(12, 4, 48, 2));

	protected ECheckBox bold = new ECheckBox(new ECheckBoxConfig("Bold"));

	protected ECheckBox italic = new ECheckBox(new ECheckBoxConfig("Italic"));

	protected ETextField example = new ETextField(new ETextFieldConfig("Example text!"));

	public EFontChooser(String defaultFontFamily) {
		this(new Font(defaultFontFamily, Font.PLAIN, 12));
	}

	public EFontChooser(Font defaultFont) {
		super(new MigLayout());
		this.add(this.fc);
		if (defaultFont != null) fc.setSelectedItem(defaultFont.getFamily());
		Dimension cbsize = new Dimension(200, 20);
		this.fc.setSize(cbsize);
		this.fc.setPreferredSize(cbsize);
		this.fc.setMaximumSize(cbsize);
		this.add(this.size);
		this.add(this.bold);
		this.add(this.italic, "wrap");
		this.add(this.example, "growx");
		size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				changeFont();
			}
		});
		bold.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFont();
			}
		});
		if (defaultFont != null) bold.setSelected(defaultFont.isBold());
		italic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFont();
			}
		});
		if (defaultFont != null) italic.setSelected(defaultFont.isItalic());
		size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				changeFont();
			}
		});
		if (defaultFont != null) size.getModel().setValue(defaultFont.getSize());
		changeFont();
		UIUtils.registerLocaleChangeListener((EComponentI) this);
	}

	protected void changeFont() {
		Font selectedFont = getSelectedFont();
		if (selectedFont != null) example.setFont(selectedFont);
	}

	protected Font getSelectedFont() {
		if (bold.isSelected() && italic.isSelected()) {
			return new Font(fc.getSelectedFontName(), Font.BOLD | Font.ITALIC, size.get());
		} else if (bold.isSelected()) {
			return new Font(fc.getSelectedFontName(), Font.BOLD, size.get());
		} else if (italic.isSelected()) {
			return new Font(fc.getSelectedFontName(), Font.ITALIC, size.get());
		} else {
			return new Font(fc.getSelectedFontName(), Font.PLAIN, size.get());
		}
	}
}
