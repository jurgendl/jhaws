package org.swingeasy;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

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
		if (ResultType.OK == CustomizableOptionPane.showCustomDialog(parent, fc, Messages.getString(fc.getLocale(), "font-chooser-title"), MessageType.QUESTION,
				OptionType.OK_CANCEL, null, null)) {
			return fc.getSelectedFont();
		}
		return null;
	}

	public static Font showDialog(JComponent parent, String defaultFont) {
		EFontChooser fc = new EFontChooser(defaultFont);
		if (ResultType.OK == CustomizableOptionPane.showCustomDialog(parent, fc, Messages.getString(fc.getLocale(), "font-chooser-title"), MessageType.QUESTION,
				OptionType.OK_CANCEL, null, null)) {
			return fc.getSelectedFont();
		}
		return null;
	}

	protected FontChooserComboBox fc = new FontChooserComboBox();

	protected ESpinner<Integer> size = new ESpinner<>(new SpinnerNumberModel(12, 4, 48, 2));

	protected ECheckBox bold = new ECheckBox(new ECheckBoxConfig("Bold"));

	protected ECheckBox italic = new ECheckBox(new ECheckBoxConfig("Italic"));

	protected ETextField example = new ETextField(new ETextFieldConfig("Example text!"));

	public EFontChooser(String defaultFontFamily) {
		this(new Font(defaultFontFamily, Font.PLAIN, 12));
	}

	public EFontChooser(Font defaultFont) {
		super(new MigLayout());
		this.add(fc);
		if (defaultFont != null) {
			fc.setSelectedItem(defaultFont.getFamily());
		}
		Dimension cbsize = new Dimension(200, 20);
		fc.setSize(cbsize);
		fc.setPreferredSize(cbsize);
		fc.setMaximumSize(cbsize);
		this.add(size);
		this.add(bold);
		this.add(italic, "wrap");
		this.add(example, "growx");
		size.addChangeListener(e -> changeFont());
		bold.addActionListener(e -> changeFont());
		if (defaultFont != null) {
			bold.setSelected(defaultFont.isBold());
		}
		italic.addActionListener(e -> changeFont());
		if (defaultFont != null) {
			italic.setSelected(defaultFont.isItalic());
		}
		size.addChangeListener(e -> changeFont());
		if (defaultFont != null) {
			size.getModel().setValue(defaultFont.getSize());
		}
		changeFont();
		UIUtils.registerLocaleChangeListener((EComponentI) this);
	}

	protected void changeFont() {
		Font selectedFont = getSelectedFont();
		if (selectedFont != null) {
			example.setFont(selectedFont);
		}
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
